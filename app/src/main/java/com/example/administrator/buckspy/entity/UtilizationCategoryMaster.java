package com.example.administrator.buckspy.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity (tableName = "UtilizationCategoryMaster")
public class UtilizationCategoryMaster {
    @PrimaryKey()
    @ColumnInfo(name = "category_Id")
    @NonNull
    private String category_Id;

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @ColumnInfo(name = "category_name")
    private String category_name;
}
