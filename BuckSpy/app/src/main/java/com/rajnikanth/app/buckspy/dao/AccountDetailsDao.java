package com.rajnikanth.app.buckspy.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rajnikanth.app.buckspy.entity.AccountDetails;

import java.util.List;

@Dao
public interface AccountDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountDetails... accountDetails);

    @Query("Select * FROM AccountDetails")
    public List<AccountDetails> loadAll();

}
