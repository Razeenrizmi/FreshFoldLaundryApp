package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.dto.Admin.UserManagementDTO;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserManagementDAO {
    public List<UserManagementDTO> getAllUsers() {
        List<UserManagementDTO> userList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.id, CONCAT(u.first_name, ' ', u.last_name) as name, u.email, ");
        sql.append("u.role as user_type, u.phone_number, u.address ");
        sql.append("FROM user u ");
        sql.append("WHERE u.role != 'ADMIN'");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserManagementDTO dto = new UserManagementDTO();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setEmail(rs.getString("email"));
                dto.setType(rs.getString("user_type"));
                dto.setPhoneNumber(rs.getString("phone_number"));
                dto.setAddress(rs.getString("address"));

                userList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public List<UserManagementDTO> filterUsers(String type, String searchTerm) {
        List<UserManagementDTO> allUsers = getAllUsers();
        List<UserManagementDTO> filteredUsers = new ArrayList<>();

        for (UserManagementDTO user : allUsers) {
            boolean typeMatch = (type == null || type.isEmpty() || user.getType().equals(type));
            boolean searchMatch = (searchTerm == null || searchTerm.isEmpty() ||
                    user.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(searchTerm.toLowerCase()));

            if (typeMatch && searchMatch) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
}