package com.consulting.service;

import com.consulting.model.User;
import com.consulting.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmailId(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Convert Set<Role> to Collection<GrantedAuthority>
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        // Return Spring Security User object
        return new org.springframework.security.core.userdetails.User(
                user.getUserEmailId(),  // Username is email
                user.getPassword(),     // Encoded password
                authorities
        );
    }
}