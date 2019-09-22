package com.rajnikanth.app.buckspy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rajnikanth.app.buckspy.entity.DashBoardDetails;
import com.rajnikanth.app.buckspy.entity.IncomeDetails;

@Dao
public interface IncomeDetailsDao {
    @Insert
    void insert(IncomeDetails incomeDetails);

    @Query("SELECT SUM(income_amount)income_amount,SUM(expense_amount)expense_amount FROM ("
            + "SELECT SUM(expense_amount)expense_amount,0 income_amount,strftime('%m', created_timestamp/1000.0, 'unixepoch')created_timestamp FROM ExpenseDetails"
            + " WHERE strftime('%m', created_timestamp/1000.0, 'unixepoch') = :currentMonth "
            + " UNION ALL "
            + "SELECT 0 expense_amount,SUM(income_amount)income_amount,strftime('%m', created_timestamp/1000.0, 'unixepoch')created_timestamp FROM IncomeDetails"
            + " WHERE strftime('%m', created_timestamp/1000.0, 'unixepoch') = :currentMonth "
            + ")AA ")//GROUP BY created_timestamp
    public DashBoardDetails getCurrentMonthData(String currentMonth);
}
