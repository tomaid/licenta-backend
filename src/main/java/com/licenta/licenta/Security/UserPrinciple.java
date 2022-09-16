package com.licenta.licenta.Security;

import com.licenta.licenta.dto.AuthDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {
    private String user;
    private Collection<? extends  GrantedAuthority> authorities;

    public UserPrinciple(String user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    public static UserPrinciple build(AuthDto authDto){
        String user = authDto.getUser();
        String role = authDto.getRole();
        List<GrantedAuthority> authorityList = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(role);
        authorityList.add(simpleGrantedAuthority);
        return new UserPrinciple(user,authorityList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
