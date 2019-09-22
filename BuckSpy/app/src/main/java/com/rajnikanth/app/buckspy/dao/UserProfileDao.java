package com.rajnikanth.app.buckspy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rajnikanth.app.buckspy.entity.AccountDetails;
import com.rajnikanth.app.buckspy.entity.UserProfile;

import java.util.List;

@Dao
public interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserProfile userProfile);

    @Query("UPDATE UserProfile SET user_name = :userName WHERE mobile_number = :mobileNumber")
    void updateUserName(String mobileNumber, String userName);

    @Query("UPDATE UserProfile SET image_path = :imagePath WHERE mobile_number = :mobileNumber")
    void updateImagePath(String mobileNumber, String imagePath);

    @Query("UPDATE UserProfile SET mail_id = :mailId WHERE mobile_number = :mobileNumber")
    void updateMailId(String mobileNumber, String mailId);

    @Query("Select * FROM UserProfile")
    public UserProfile getUserProfileDetails();
}
