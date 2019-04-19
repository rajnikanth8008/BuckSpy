package com.example.administrator.buckspy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.example.administrator.buckspy.entity.UtilizationDetails;

@Dao
public interface UtilizationDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UtilizationDetails utilizationDetails);
}
