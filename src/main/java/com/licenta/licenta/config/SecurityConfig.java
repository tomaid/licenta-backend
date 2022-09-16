package com.licenta.licenta.config;

import com.licenta.licenta.Security.CustomAuthenticationProvider;
import com.licenta.licenta.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled=true,
        jsr250Enabled=true,
        prePostEnabled=true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService){
        this.userDetailsService=userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider())
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(){
        CustomAuthenticationProvider authenticationProvider=new CustomAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

}
