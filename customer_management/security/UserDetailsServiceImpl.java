package com.pramaindia.customer_management.security;

import com.pramaindia.customer_management.dao.AdminLoginDAO;
import com.pramaindia.customer_management.model.AdminLoginInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminLoginDAO adminLoginDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        AdminLoginInfo adminInfo = adminLoginDAO.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        log.info("User found: {}, Role: {}", adminInfo.getUsername(), adminInfo.getRole());

        if (!Boolean.TRUE.equals(adminInfo.getIsActive())) {
            throw new UsernameNotFoundException("User account is inactive: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + adminInfo.getRole()));

        return new User(
                adminInfo.getUsername(),
                adminInfo.getPasswordHash(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}