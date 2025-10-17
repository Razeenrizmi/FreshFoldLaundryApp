package com.laundry.freshfoldlaundryapp.DAO;



import com.laundry.freshfoldlaundryapp.model.admin.Admin;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    public boolean createAdmin(Admin admin) {
        Connection conn = DBConnect.connect();

        try {
            String sql = "INSERT INTO admins ( fullName, userName, password, email, phoneNumber, role) VALUES ( ?, ?, ? ,? ,? ,? )";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getUserName());
            stmt.setString(3, admin.getPassword());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPhoneNumber());
            stmt.setString(6, admin.getRole());


            stmt.executeUpdate();
            System.out.println("Admin created successfully!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // check login
    public boolean checkAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE userName = ? AND password = ?";
        try (Connection  con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e){
            e.printStackTrace();
            return false;

        }
    }

    // get all admins
    public List<Admin> getAllAdmin() {
        List<Admin> adminList = new ArrayList<>();
        String sql = "SELECT * FROM admins";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Admin admin= new Admin();
                admin.setAdminid(rs.getInt("adminid"));
                admin.setFullName(rs.getString("fullName"));
                admin.setUserName(rs.getString("userName"));
                admin.setPassword(rs.getString("password"));
                admin.setEmail(rs.getString("email"));
                admin.setPhoneNumber(rs.getString("phoneNumber"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getString("status"));
                adminList.add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminList;
    }

    // new method for get admin by id
    public Admin getAdminById(int id) {
        String sql = "SELECT * FROM admins WHERE adminid = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Admin admin= new Admin();
                admin.setAdminid(rs.getInt("adminid"));
                admin.setFullName(rs.getString("fullName"));
                admin.setUserName(rs.getString("userName"));
                admin.setPassword(rs.getString("password"));
                admin.setEmail(rs.getString("email"));
                admin.setPhoneNumber(rs.getString("phoneNumber"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getString("status"));
                return admin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    // method for delete admin
    public void deleteAdmin(int id) {
        String sql = "DELETE FROM admins WHERE adminid = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Admin deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method for get admin by username (needed for Spring Security)
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admins WHERE userName = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminid(rs.getInt("adminid"));
                admin.setFullName(rs.getString("fullName"));
                admin.setUserName(rs.getString("userName"));
                admin.setPassword(rs.getString("password"));
                admin.setEmail(rs.getString("email"));
                admin.setPhoneNumber(rs.getString("phoneNumber"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getString("status"));
                return admin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // method for update admin
    public void updateAdmin(Admin admin) {
        String sql = "UPDATE admins SET fullName= ?, userName = ?, password = ?, email = ?, phoneNumber = ?, role = ? , status = ? WHERE adminid = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,admin.getFullName());
            ps.setString(2,admin.getUserName());
            ps.setString(3,admin.getPassword());
            ps.setString(4,admin.getEmail());
            ps.setString(5,admin.getPhoneNumber());
            ps.setString(6,admin.getRole());
            ps.setString(7,admin.getStatus());
            ps.setInt(8,admin.getAdminid());
            ps.executeUpdate();
            System.out.println("Admin updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}