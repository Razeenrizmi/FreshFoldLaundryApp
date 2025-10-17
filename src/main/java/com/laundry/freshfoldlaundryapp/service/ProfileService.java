package com.laundry.freshfoldlaundryapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laundry.freshfoldlaundryapp.model.Profile;
import com.laundry.freshfoldlaundryapp.repository.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile getProfileByUserId(Long Id) {
        return profileRepository.findByUserId(Id);
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
}




