package com.se.focusclock.security.filter;

import com.se.focusclock.security.token.PhoneAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PhoneAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_PHONE_KEY = "phone";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    private String phoneParameter = SPRING_SECURITY_FORM_PHONE_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;

    public PhoneAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/users/Plogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !httpServletRequest.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + httpServletRequest.getMethod());
        }

        String phone = obtainPhone(httpServletRequest);
        String password = obtainPassword(httpServletRequest);

        if (phone == null) {
            phone = "";
        }

        if (password == null) {
            password = "";
        }

        phone = phone.trim();
        password = password.trim();

        PhoneAuthenticationToken authRequest = new PhoneAuthenticationToken(phone, password);

        // Allow subclasses to set the "details" property
        setDetails(httpServletRequest, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(phoneParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
