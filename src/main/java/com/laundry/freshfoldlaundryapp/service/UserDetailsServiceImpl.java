package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.DAO.AdminDAO;
import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.model.admin.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    private AdminDAO adminDAO = new AdminDAO();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find admin by username
        Admin admin = adminDAO.getAdminByUsername(username);
        if (admin != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(admin.getRole()));
            return new CustomUserDetails(admin.getUserName(), admin.getPassword(), authorities, (long) admin.getAdminid());
        }

        // If not admin, try regular user by email
        User user = userService.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username/email: " + username);
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities, user.getId());
    }
}