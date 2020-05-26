package com.example.security.configuration;

import com.example.security.dto.AccountCredentials;
import com.example.security.service.impl.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String URILOGIN = "/login";

    private final AuthenticationService authenticationService;

    //asignamos tanto la url que utilizaremos como login de authentication como el Authentication manager encargado de la autenticacion
    public LoginFilter(AuthenticationManager authManager, AuthenticationService authenticationService) {
        super(new AntPathRequestMatcher(URILOGIN));
        setAuthenticationManager(authManager);
        this.authenticationService = authenticationService;
    }

    //Metodo que autentica los procesos de identificación
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        AccountCredentials credentials = new ObjectMapper()
                .readValue(request.getInputStream(), AccountCredentials.class);
        return getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(),
                    credentials.getPassword(),
                    Collections.emptyList()
            )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        authenticationService.addToken(response, authResult.getName());
    }

    @Override
    protected AuthenticationFailureHandler getFailureHandler() {
        return super.getFailureHandler();
    }
}
