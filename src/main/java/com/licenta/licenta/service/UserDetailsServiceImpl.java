package com.licenta.licenta.service;

import com.licenta.licenta.dto.AuthDto;
import com.licenta.licenta.Security.UserPrinciple;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException{
        Optional<AuthDto> optionalAuthDto = userRepository.getByUserWithPassAndRole(user);
        if(optionalAuthDto.isPresent()){
            return UserPrinciple.build(optionalAuthDto.get());
        } throw new UsernameNotFoundException("No user found with email" + user);
    }
}
