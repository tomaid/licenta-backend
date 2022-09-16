package com.licenta.licenta.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
       http.cors().and().csrf().disable()
               .formLogin().disable()
               .httpBasic().disable()
               .authorizeRequests()
               .antMatchers("/api/user/create","/api/tip/getAll", "/api/judete/getAll", "/api/user/reset-parola", "/api/localitati/**", "/", "/web/**").permitAll()
               .anyRequest().authenticated();
    }
}
