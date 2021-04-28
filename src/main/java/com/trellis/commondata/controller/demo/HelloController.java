package com.trellis.commondata.controller.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class HelloController {
    @GetMapping("/public/noAuth")
    @ResponseBody
    public String hello() {
        logUser();
        return "Hello from Springboot2! " + new Date() + " :: " + getCurrentCsrfToken();
    }

    public static String getCurrentCsrfToken() {
        // quick-test
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(false);
        if (session == null) {
            return null;
        }

        String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");
        CsrfToken sessionToken = (CsrfToken) session.getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);

        return sessionToken == null ? "None" : sessionToken.toString();
    }

    @PostMapping("/public/publicPost")
    @ResponseBody
    public String helloPost() {
        logUser();
        return "helloPost from Springboot2! " + new Date() + " :: " + getCurrentCsrfToken();
    }

    @GetMapping("/authorize")
    @ResponseBody
    public String authorize(CsrfToken token) {
        logUser();
        System.out.println(token.getToken());
        return "Hello iAmSecured from Springboot2! " + new Date() + " :: "
                + token.getToken() + "/"
                + token.getHeaderName()  + "/"
                + token.getParameterName();
    }

    @GetMapping("/getAccessRights")
    @ResponseBody
    public String getAccessRights(CsrfToken token) {
        logUser();
        System.out.println(token.getToken());
        return "Hello iAmSecured from Springboot2! " + new Date() + " :: "
                + token.getToken() + "/"
                + token.getHeaderName()  + "/"
                + token.getParameterName();
    }



    @GetMapping("/iAmSecured")
    @ResponseBody
    public String iAmSecured(CsrfToken token) {
        logUser();
        System.out.println(token.getToken());
        return "Hello iAmSecured from Springboot2! " + new Date() + " :: "
                + token.getToken() + "/"
                + token.getHeaderName()  + "/"
                + token.getParameterName();
    }

    @PostMapping("/admin/createUser")
    @PreAuthorize("hasPermission(#dto, 'HUB_ADMIN_WORKFLOW')")
    @ResponseBody
    public String createUser(CsrfToken token, @RequestBody HelloDto dto) {
        logUser();
        return "Hello iAmSecured from Springboot2! " + new Date() + " :: "
                + token.getToken() + "/"
                + token.getHeaderName()  + "/"
                + token.getParameterName();
    }


    private void logUser() {
        System.out.println("\n\n=========================");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (!currentPrincipalName.equals("anonymousUser")) {
            DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
            showAttribute(userDetails, "id");
            showAttribute(userDetails, "name");
            showAttribute(userDetails, "email");
            showAttribute(userDetails, "preferred_username");
        }

        System.out.println("hello " + authentication.getPrincipal().toString());
        System.out.println("=========================\n\n");
    }

    private void showAttribute(DefaultOAuth2User userDetails, String id) {
        System.out.println(id + " is " + userDetails.getAttribute(id));
    }
}
