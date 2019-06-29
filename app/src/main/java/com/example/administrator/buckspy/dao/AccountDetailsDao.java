package com.example.administrator.buckspy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.administrator.buckspy.entity.AccountDetails;
import com.example.administrator.buckspy.entity.CategoryDetails;

import java.util.List;

@Dao
public interface AccountDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountDetails... accountDetails);

    @Query("Select * FROM AccountDetails")
    public List<AccountDetails> loadAll();
}
