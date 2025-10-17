package com.laundry.freshfoldlaundryapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.laundry.freshfoldlaundryapp.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUserId(Long userId);
}