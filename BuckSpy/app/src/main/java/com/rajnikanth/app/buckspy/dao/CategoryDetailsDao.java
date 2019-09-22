package com.rajnikanth.app.buckspy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rajnikanth.app.buckspy.entity.CategoryDetails;

import java.util.List;

@Dao
public interface CategoryDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryDetails... categoryDetails);

    @Query("Select * FROM CategoryDetails WHERE category_type = :category_type")/*created_timestamp,modified_timestamp,category_id,category_type,"
            +"category_name,category_description,image_path*/
    public List<CategoryDetails> loadAll(String category_type);
}
