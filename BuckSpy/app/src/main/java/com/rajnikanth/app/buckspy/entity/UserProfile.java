package com.rajnikanth.app.buckspy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "UserProfile")
public class UserProfile {
    public long getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public long getModified_timestamp() {
        return modified_timestamp;
    }

    public void setModified_timestamp(long modified_timestamp) {
        this.modified_timestamp = modified_timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public long created_timestamp;
    public long modified_timestamp;
    public String user_id;
    public String user_name;
    @PrimaryKey
    @NonNull
    public String mobile_number;
    public String mail_id;
    public String image_path;
}
