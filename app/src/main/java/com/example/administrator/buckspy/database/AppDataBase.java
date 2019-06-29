package com.example.administrator.buckspy.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.administrator.buckspy.dao.AccountDetailsDao;
import com.example.administrator.buckspy.dao.CategoryDetailsDao;
import com.example.administrator.buckspy.dao.ExpenseDetailsDao;
import com.example.administrator.buckspy.dao.IncomeDetailsDao;
import com.example.administrator.buckspy.entity.AccountDetails;
import com.example.administrator.buckspy.entity.CategoryDetails;
import com.example.administrator.buckspy.entity.ExpenseDetails;
import com.example.administrator.buckspy.entity.IncomeDetails;
import com.example.administrator.buckspy.entity.UserProfile;

@Database(entities = {CategoryDetails.class, AccountDetails.class, UserProfile.class, ExpenseDetails.class, IncomeDetails.class},
        version = 5, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract AccountDetailsDao accountDetailsDao();

    public abstract CategoryDetailsDao categoryDetailsDao();

    public abstract ExpenseDetailsDao expenseDetailsDao();

    public abstract IncomeDetailsDao incomeDetailsDao();

}
