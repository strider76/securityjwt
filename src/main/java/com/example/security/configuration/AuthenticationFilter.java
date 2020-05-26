package com.example.security.configuration;


import com.example.security.controller.ExceptionController;
import com.example.security.exception.AccessDeniedException;
import com.example.security.service.impl.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//Maneja la autenticación en todas las peticiones menos en /login
@Component
public class AuthenticationFilter extends GenericFilterBean {

    private final AuthenticationService authenticationService;
    private final ExceptionController exceptionController;

    AuthenticationFilter(AuthenticationService authenticationService, ExceptionController exceptionController) {
        this.authenticationService = authenticationService;
        this.exceptionController = exceptionController;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = null;
        try {
            authentication = authenticationService.getAuthentication((HttpServletRequest) request);
        } catch (AccessDeniedException e) {
            exceptionController.accessDeniedException(e);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request,response);
    }
}
