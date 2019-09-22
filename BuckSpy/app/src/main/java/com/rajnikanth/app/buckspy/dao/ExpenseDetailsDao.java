package com.rajnikanth.app.buckspy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rajnikanth.app.buckspy.entity.ExpenceLocationDetails;
import com.rajnikanth.app.buckspy.entity.ExpenseDetails;

import java.util.List;

@Dao
public interface ExpenseDetailsDao  {
    @Insert
    void insert(ExpenseDetails expenseDetails);

    @Query("SELECT ED.expence_location,CB.category_name,CB.image_path FROM ExpenseDetails ED LEFT OUTER JOIN CategoryDetails CB ON ED.category_id = CB.category_id")
    public List<ExpenceLocationDetails> getExpenceLocations();
}
