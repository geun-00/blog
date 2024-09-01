package com.spring.blog.controller;

import com.spring.blog.common.annotation.CurrentUser;
import com.spring.blog.controller.validator.AddUserValidator;
import com.spring.blog.controller.validator.EditUserValidator;
import com.spring.blog.domain.User;
import com.spring.blog.dto.request.AddUserRequest;
import com.spring.blog.dto.request.EditUserRequest;
import com.spring.blog.dto.response.UserInfoResponse;
import com.spring.blog.model.PrincipalUser;
import com.spring.blog.service.BlogService;
import com.spring.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;
    private final BlogService blogService;
    private final AddUserValidator addUserValidator;
    private final EditUserValidator editUserValidator;

    @InitBinder("addUserRequest")
    public void initAddUserBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(addUserValidator);
    }

    @InitBinder("editUserRequest")
    public void initEditUserBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(editUserValidator);
    }

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("addUserRequest", new AddUserRequest());
        return "signup";
    }

    @PostMapping("/user")
    public String signup(@Validated @ModelAttribute AddUserRequest request, BindingResult bindingResult,
                         @CurrentUser Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        //폼 인증 가입
        if (authentication == null) {
            userService.save(request);
        }
        //OAuth 인증 가입, 별명만 재설정
        else {
            PrincipalUser principalUser = getPrincipal(authentication);
            userService.updateNickname(request.getNickname(), principalUser.providerUser().getEmail());
        }

        return "redirect:/login?success";
    }

    @GetMapping("/myPage")
    public String myPage(@RequestParam(value = "author", required = false) String nickname,
                         @CurrentUser Authentication authentication, Model model) {

        String targetName;

        if (StringUtils.hasText(nickname)) {
            targetName = nickname;
        } else {
            PrincipalUser principalUser = getPrincipal(authentication);
            targetName = principalUser.providerUser().getUsername();
        }

        UserInfoResponse response = userService.getUserInfo(targetName);

        model.addAttribute("user", response);

        return "myPage";
    }

    @GetMapping("/myPage/edit")
    public String editProfile(Model model) {

        model.addAttribute("editUserRequest", new EditUserRequest());

        return "myPageEdit";
    }

    @PostMapping("/user/edit")
    public String userEdit(@Validated @ModelAttribute EditUserRequest request, BindingResult bindingResult,
                           @CurrentUser Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "myPageEdit";
        }

        PrincipalUser principalUser = getPrincipal(authentication);
        User updatedUser = userService.editUser(principalUser.providerUser().getEmail(), request);

        updateContext(principalUser, authentication, updatedUser);

        return "redirect:/myPage";
    }

    private void updateContext(PrincipalUser principalUser, Authentication authentication, User user) {

        PrincipalUser updatedprincipalUser = principalUser.withUpdatedUser(user);

        Authentication newAuth = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            newAuth = new OAuth2AuthenticationToken(
                    updatedprincipalUser,
                    authentication.getAuthorities(),
                    updatedprincipalUser.providerUser().getClientRegistration().getRegistrationId()
            );
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            newAuth = new UsernamePasswordAuthenticationToken(
                    updatedprincipalUser,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
        }

        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(newAuth);
    }

    private PrincipalUser getPrincipal(Authentication authentication) {
        return (PrincipalUser) authentication.getPrincipal();
    }
}