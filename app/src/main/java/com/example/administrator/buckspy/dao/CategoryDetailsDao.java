package com.example.administrator.buckspy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.administrator.buckspy.entity.CategoryDetails;

import java.util.List;

@Dao
public interface CategoryDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryDetails... categoryDetails);

    @Query("Select * FROM CategoryDetails WHERE category_type = :category_type")/*created_timestamp,modified_timestamp,category_id,category_type,"
            +"category_name,category_description,image_path*/
    public List<CategoryDetails> loadAll(String category_type);
}
