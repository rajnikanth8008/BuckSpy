package com.rajnikanth.app.buckspy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity (tableName = "AccountDetails")
public class AccountDetails {
    public long created_timestamp;
    public long modified_timestamp;
    @PrimaryKey
    @NonNull
    public String account_id;
    public String account_name;
    public String account_description;
    public long expire_timestamp;
    public String image_path;

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

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_description() {
        return account_description;
    }

    public void setAccount_description(String account_description) {
        this.account_description = account_description;
    }

    public long getExpire_timestamp() {
        return expire_timestamp;
    }

    public void setExpire_timestamp(long expire_timestamp) {
        this.expire_timestamp = expire_timestamp;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
