package com.example.security.jwt.service;

import com.example.security.jwt.config.ErrorCode;
import com.example.security.jwt.repo.AccRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccRepository accRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = accRepository.findByEmail(username);
        if (user == null) {
            throw new RuntimeException(ErrorCode.UsernameOrPasswordNotFoundException.getMsg());
        }
        return user;
    }
}
