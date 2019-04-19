package com.example.administrator.buckspy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.administrator.buckspy.entity.UtilizationCategoryMaster;

@Dao
public interface UtilizationCategoryMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UtilizationCategoryMaster... utilizationCategoryMaster);

    @Query("Select * FROM UtilizationCategoryMaster")
    UtilizationCategoryMaster[] loadAll();
}
