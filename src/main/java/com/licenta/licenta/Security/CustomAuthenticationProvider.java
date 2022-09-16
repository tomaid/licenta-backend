package com.licenta.licenta.Security;

import com.licenta.licenta.dto.AuthDto;
import com.licenta.licenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Optional;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    private UserRepository userRepository;

    @Autowired
    public void setServices(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public Authentication authenticate(Authentication authentication){
        String user=authentication.getName().trim();
        String inputPassword=authentication.getCredentials().toString();
        Optional<AuthDto> optionalAuthDto=userRepository.getByUserWithPassAndRole(user);
        try{
        if(optionalAuthDto.isPresent()){
            AuthDto authDto = optionalAuthDto.get();
            String dbPassword= authDto.getPass();
            if(BCrypt.checkpw(inputPassword,dbPassword)){
                UserDetails userDetails=UserPrinciple.build(authDto);
                Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);
                return newAuth;
            }     throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials","Parola invalida"));
        } throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials","Userul nu este valid"));
    } catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }
}
