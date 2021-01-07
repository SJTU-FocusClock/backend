package com.se.focusclock.config;

import com.se.focusclock.handler.MyAuthenticationFailureHandler;
import com.se.focusclock.handler.MyAuthenticationSuccessHandler;
import com.se.focusclock.security.filter.PhoneAuthenticationProcessingFilter;
import com.se.focusclock.security.filter.YepAuthenticationProcessingFilter;
import com.se.focusclock.security.provider.PhoneAuthenticationProvider;
import com.se.focusclock.security.provider.UsernameAuthenticationProvider;
import com.se.focusclock.security.provider.YepAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    @Lazy
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(phoneAuthenticationProvider())
                .authenticationProvider(yepAuthenticationProvider())
                .authenticationProvider(usernameAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/users/user","/users/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/Ulogin")
                .usernameParameter("username").passwordParameter("password")
                .permitAll()
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .permitAll()
                .and()
                .cors()
                .and()
                .csrf().disable();
        http.addFilterBefore(phoneAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(yepAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PhoneAuthenticationProcessingFilter phoneAuthenticationProcessingFilter() {
        PhoneAuthenticationProcessingFilter filter = new PhoneAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        return filter;
    }

    @Bean
    public YepAuthenticationProcessingFilter yepAuthenticationProcessingFilter() {
        YepAuthenticationProcessingFilter filter = new YepAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        return filter;
    }

    @Bean
    public UsernameAuthenticationProvider usernameAuthenticationProvider() {
        return new UsernameAuthenticationProvider();
    }

    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider() {
        return new PhoneAuthenticationProvider();
    }

    @Bean
    public YepAuthenticationProvider yepAuthenticationProvider() {
        return new YepAuthenticationProvider();
    }
}
