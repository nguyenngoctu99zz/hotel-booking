package com.nnt.hotelbooking.user.repository;

import com.nnt.hotelbooking.user.model.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, Long> {
}
