package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.Institution;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.InstitutionRepository;
import com.mustard.vaidyalink.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public MyUserDetailsService(UserRepository userRepository, InstitutionRepository institutionRepository) {
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        Institution institution = institutionRepository.findByEmail(email).orElse(null);

        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities("USER")
                    .build();
        } else if (institution != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(institution.getEmail())
                    .password(institution.getPassword())
                    .authorities("INSTITUTION")
                    .build();
        } else {
            throw new UsernameNotFoundException("User or institution not found with email: " + email);
        }
    }
}
