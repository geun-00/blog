package com.spring.blog.service;

import com.spring.blog.common.enums.SocialType;
import com.spring.blog.domain.User;
import com.spring.blog.dto.request.AddUserRequest;
import com.spring.blog.dto.request.EditUserRequest;
import com.spring.blog.dto.response.UserInfoResponse;
import com.spring.blog.model.ProviderUser;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.CommentRepository;
import com.spring.blog.repository.UserQueryRepository;
import com.spring.blog.repository.UserRepository;
import com.spring.blog.service.file.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final FileService fileService;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;
    private final UserQueryRepository userQueryRepository;

    @Transactional
    public void save(AddUserRequest request) {
        userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .nickname(request.getNickname())
                        .registrationId(SocialType.NONE)
                        .profileImageUrl(fileService.saveFile(request.getImageFile(), "user/"))
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
        );
    }

    @Transactional
    public void save(ProviderUser providerUser, SocialType socialType) {
        userRepository.save(
            User.builder()
                    .registrationId(socialType)
                    .email(providerUser.getEmail())
                    .profileImageUrl(providerUser.getProfileImageUrl())
                    .password(passwordEncoder.encode(providerUser.getPassword()))
                    .build()
        );
    }

    @Transactional
    public void updateOAuthUser(String nickname, MultipartFile imageFile, String email) {
        User user = findByEmail(email);
        user.updateNickname(nickname);

        if (imageFile == null || !StringUtils.hasText(imageFile.getOriginalFilename())) {
            return;
        }

        user.updateProfileImageUrl(fileService.saveFile(imageFile, "user/"));
    }

    @Transactional
    public void deleteUser(String email) {
        User user = findByEmail(email);

        commentRepository.deleteByUserId(user.getId());
        blogRepository.deleteByUserId(user.getId());

        fileService.deleteFile(user.getProfileImageUrl());

        userRepository.delete(user);
    }

    @Transactional
    public User editUser(String email, EditUserRequest request) {
        User user = findByEmail(email);
        user.edit(request.getEmail(), request.getNickname());

        String oldImageUrl = user.getProfileImageUrl();

        MultipartFile imageFile = request.getFile();

        if (imageFile != null && StringUtils.hasText(imageFile.getOriginalFilename())) {

            String newImageUrl = fileService.saveFile(imageFile, "user/");
            user.updateProfileImageUrl(newImageUrl);

            fileService.deleteFile(oldImageUrl);
        }

        return user;
    }

    public UserInfoResponse getUserInfo(String name) {
        return userQueryRepository.getUserInfo(name);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Unexpected User : " + userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Unexpected User : " + email));
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}