package com.example.administrator.buckspy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.example.administrator.buckspy.entity.IncomeDetails;
@Dao
public interface IncomeDetailsDao {
    @Insert
    void insert(IncomeDetails incomeDetails);
}
