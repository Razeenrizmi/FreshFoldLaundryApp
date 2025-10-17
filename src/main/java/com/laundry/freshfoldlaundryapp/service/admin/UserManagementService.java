package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.dto.Admin.UserManagementDTO;
import com.laundry.freshfoldlaundryapp.DAO.UserManagementDAO;

import java.util.List;

public class UserManagementService {
    private UserManagementDAO userManagementDAO = new UserManagementDAO();

    public List<UserManagementDTO> getAllUsers() {
        return userManagementDAO.getAllUsers();
    }

    public List<UserManagementDTO> filterUsers(String type, String searchTerm) {
        return userManagementDAO.filterUsers(type, searchTerm);
    }
}