package com.laundry.freshfoldlaundryapp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/";

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("STAFF")) {
                redirectUrl = "/staff-dashboard";
                break;
            } else if (authority.getAuthority().equals("CUSTOMER")) {
                redirectUrl = "/customer-dashboard";
                break;
            } else if (authority.getAuthority().equals("MANAGER")) {
                redirectUrl = "manager/manager-dashboard";
                break;
            } else if (authority.getAuthority().equals("DELIVERY_STAFF")) {
                redirectUrl = "/delivery-staff-dashboard";
                break;
            } else if (authority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/admin/admin/AdminDashboard";
                break;
            }
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}