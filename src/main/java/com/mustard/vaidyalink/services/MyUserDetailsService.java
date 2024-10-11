package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String aadhaarHash) throws UsernameNotFoundException {
        var user = userRepository.findByAadhaarNumberHash(aadhaarHash)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getAadhaarNumberHash())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
