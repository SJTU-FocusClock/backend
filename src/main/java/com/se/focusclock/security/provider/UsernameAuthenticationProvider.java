package com.se.focusclock.security.provider;

import com.se.focusclock.entity.User;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.CheckUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        String password = (String) authentication.getCredentials();
        if (CheckUtils.isEmpty(password)) {
            throw new BadCredentialsException("密码不能为空");
        }

        User user = userService.load(-1, username, null);
        if (null == user) {
            throw new BadCredentialsException("用户不存在");
        }

        if (password.length() != 32) {
            password = DigestUtils.md5Hex(password);
        }
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("用户名或密码不正确");
        }
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username, password, listUserGrantedAuthorities(user.getId()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        System.out.println(this.getClass().getName() + "---supports");
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Set<GrantedAuthority> listUserGrantedAuthorities(int uid) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        if (CheckUtils.isEmpty(uid)) {
            return authorities;
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}
