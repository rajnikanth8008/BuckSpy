package com.rajnikanth.app.buckspy.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity (tableName = "CategoryDetails")
public class CategoryDetails {
    public long created_timestamp;
    public long modified_timestamp;
    @PrimaryKey
    @NonNull
    public String category_id;
    public String category_type;
    public String category_name;
    public String category_description;

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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String image_path;
}
