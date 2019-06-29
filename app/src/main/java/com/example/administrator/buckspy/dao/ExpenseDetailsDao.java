package com.example.administrator.buckspy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.example.administrator.buckspy.entity.ExpenseDetails;
@Dao
public interface ExpenseDetailsDao  {
    @Insert
    void insert(ExpenseDetails expenseDetails);
}
