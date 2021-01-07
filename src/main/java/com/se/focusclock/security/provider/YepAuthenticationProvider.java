package com.se.focusclock.security.provider;

import com.se.focusclock.entity.User;
import com.se.focusclock.security.token.YepAuthenticationToken;
import com.se.focusclock.service.UserService;
import com.se.focusclock.utils.CheckUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class YepAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        String password = (String) authentication.getCredentials();

//        if (CheckUtils.isEmpty(password)) {
//            throw new BadCredentialsException("密码不能为空");
//        }

        User user = userService.load(-1, null, phone);
        if (user == null) {
            throw new BadCredentialsException("用户不存在");
        }

//        if (password.length() != 32) {
//            password = DigestUtils.md5Hex(password);
//        }

//        if (!password.equals(user.getPassword())) {
//            throw new BadCredentialsException("用户名或密码不正确");
//        }

        password = user.getPassword();

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.getUsername(), password, listUserGrantedAuthorities(user.getId()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        System.out.println(this.getClass().getName() + "---supports");
        return (YepAuthenticationToken.class.isAssignableFrom(authentication));
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
