package com.spring.blog.service;

import com.spring.blog.common.annotation.DuplicateCheck;
import com.spring.blog.common.enums.SocialType;
import com.spring.blog.common.events.UserDeletedEvent;
import com.spring.blog.domain.Article;
import com.spring.blog.domain.ArticleImages;
import com.spring.blog.domain.User;
import com.spring.blog.exception.ResponseStatusException;
import com.spring.blog.exception.duplicate.NicknameDuplicateException;
import com.spring.blog.mapper.UserMapper;
import com.spring.blog.model.OAuth2ProviderUser;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.ArticleImagesRepository;
import com.spring.blog.repository.ArticleLikesRepository;
import com.spring.blog.repository.BlogQueryRepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserQueryRepository;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.dto.request.EditUserServiceRequest;
import com.spring.blog.service.dto.request.FormAddUserServiceRequest;
import com.spring.blog.service.dto.request.NewPasswordServiceRequest;
import com.spring.blog.service.dto.request.OAuthAddUserServiceRequest;
import com.spring.blog.service.dto.response.UserInfo;
import com.spring.blog.service.dto.response.UserInfoResponse;
import com.spring.blog.service.file.FileService;
import com.spring.blog.service.oauth.unlink.OAuth2UnlinkService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    @Value("${profile.default.image-url}")
    private String defaultImageUrl;

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserQueryRepository userQueryRepository;
    private final BlogQueryRepository blogQueryRepository;
    private final ArticleLikesRepository articleLikesRepository;
    private final ArticleImagesRepository articleImagesRepository;

    private final UserMapper userMapper;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2UnlinkService oAuth2UnlinkService;
    private final ApplicationEventPublisher eventPublisher;

    @DuplicateCheck
    @Transactional
    public String save(FormAddUserServiceRequest request) {
        User savedUser = userRepository.save(
                userMapper.toEntity(request, SocialType.NONE, defaultImageUrl)
        );

        return savedUser.getNickname();
    }

    @Transactional
    public void save(ProviderUser providerUser, SocialType socialType) {
        userRepository.save(
                userMapper.toEntity(providerUser, socialType)
        );
    }

    @DuplicateCheck
    @Transactional
    public String updateOAuthUser(OAuthAddUserServiceRequest request, String email) {
        User user = findByEmail(email);

        user.updateNickname(request.nickname());
        user.updatePhoneNumber(request.phoneNumber());

        return user.getNickname();
    }

    @Transactional
    public void deleteUser(PrincipalUser principalUser) {

        CompletableFuture<Void> unlinkFuture = null;
        if (principalUser.providerUser() instanceof OAuth2ProviderUser) {
            unlinkFuture = sendUnlinkRequest();
        }

        String email = principalUser.providerUser().getEmail();
        User user = findByEmail(email);

        List<Article> articles = blogRepository.findByUser(user);
        List<ArticleImages> articleImages = articleImagesRepository.findAllByArticles(articles);

        deleteAndUpdateEntities(articles, user);

        eventPublisher.publishEvent(new UserDeletedEvent(user.getProfileImageUrl(), articleImages));

        if (unlinkFuture != null) {
            try {
                unlinkFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new ResponseStatusException(e);
            }
        }
    }

    @Transactional
    public void editUser(EditUserServiceRequest request, String email) {
        User user = findByEmail(email);

        if (!user.getNickname().equals(request.nickname()) &&
                userRepository.existsByNickname(request.nickname())) {
            throw new NicknameDuplicateException();
        }

        user.updateNickname(request.nickname());

        String oldImageUrl = user.getProfileImageUrl();

        MultipartFile imageFile = request.file();

        if (imageFile != null && StringUtils.hasText(imageFile.getOriginalFilename())) {

            String newImageUrl = fileService.saveFile(imageFile, "user/");
            user.updateProfileImageUrl(newImageUrl);

            if (!oldImageUrl.equals(defaultImageUrl)) {
                fileService.deleteFile(oldImageUrl);
            }
        }

        updateContext(user);
    }

    @Transactional
    public void setNewPassword(NewPasswordServiceRequest request) {
        User user = findByEmail(request.email());
        user.updatePassword(passwordEncoder.encode(request.newPassword()));
    }

    public UserInfoResponse getUserInfo(String name) {
        UserInfo userInfo = userQueryRepository.getUserInfo(name);
        if (userInfo == null) {
            throw new EntityNotFoundException("not found user from " + name);
        }
        return userMapper.toUserInfoResponse(userInfo);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("not found user from " + email));
    }

    private CompletableFuture<Void> sendUnlinkRequest() {
        OAuth2AuthenticationToken oAuth2AuthenticationToken =
                (OAuth2AuthenticationToken) SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        String name = oAuth2AuthenticationToken.getPrincipal().getName();
        return oAuth2UnlinkService.unlink(registrationId, name);
    }

    private void deleteAndUpdateEntities(List<Article> articles, User user) {
        articleImagesRepository.deleteByArticles(articles);
        articleLikesRepository.deleteByArticles(articles);
        commentRepository.deleteByArticles(articles);

        blogQueryRepository.decreaseArticleLikesByUser(user);

        articleLikesRepository.deleteByUser(user);
        commentRepository.deleteByUser(user);
        blogRepository.deleteByUser(user);

        userRepository.delete(user);
    }

    private void updateContext(User updatedUser) {

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof PrincipalUser principalUser) {

            log.info("사용자 정보 수정, 세션 업데이트");

            PrincipalUser updatedPrincipalUser = principalUser.withUpdatedUser(updatedUser);

            Authentication newAuth = null;

            if (authentication instanceof OAuth2AuthenticationToken) {
                newAuth = new OAuth2AuthenticationToken(
                        updatedPrincipalUser,
                        authentication.getAuthorities(),
                        updatedPrincipalUser.providerUser().getClientRegistration().getRegistrationId()
                );
            } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
                newAuth = new UsernamePasswordAuthenticationToken(
                        updatedPrincipalUser,
                        authentication.getCredentials(),
                        authentication.getAuthorities()
                );
            }

            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(newAuth);
        }
    }
}