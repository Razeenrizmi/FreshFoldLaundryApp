package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.DAO.AdminDAO;
import com.laundry.freshfoldlaundryapp.model.admin.Admin;

import java.util.List;

public class AdminService {
    AdminDAO adminDAO ;

    public AdminService (){
        adminDAO = new AdminDAO();
    }

    public boolean login(String username, String password){
        return  adminDAO.checkAdmin(username, password);
    }

    public boolean addAdmin(Admin admin) {
        return adminDAO.createAdmin(admin);
    }

    public List<Admin> getAllAdmin(){
        return adminDAO.getAllAdmin();
    }

    // new method for get admins by ID
    public Admin getAdminById(int id) {
        return adminDAO.getAdminById(id);
    }

    // method to delete admin
    public void deleteAdmin(int id) {
        adminDAO.deleteAdmin(id);
    }

    // method to update admin
    public void updateAdmin(Admin admin) {
        adminDAO.updateAdmin(admin);
    }



}