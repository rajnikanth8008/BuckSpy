package com.rajnikanth.app.buckspy.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rajnikanth.app.buckspy.dao.AccountDetailsDao;
import com.rajnikanth.app.buckspy.dao.CategoryDetailsDao;
import com.rajnikanth.app.buckspy.dao.ExpenseDetailsDao;
import com.rajnikanth.app.buckspy.dao.IncomeDetailsDao;
import com.rajnikanth.app.buckspy.dao.UserProfileDao;
import com.rajnikanth.app.buckspy.entity.AccountDetails;
import com.rajnikanth.app.buckspy.entity.CategoryDetails;
import com.rajnikanth.app.buckspy.entity.ExpenseDetails;
import com.rajnikanth.app.buckspy.entity.IncomeDetails;
import com.rajnikanth.app.buckspy.entity.UserProfile;

@Database(entities = {CategoryDetails.class, AccountDetails.class, UserProfile.class, ExpenseDetails.class, IncomeDetails.class},
        version = 8, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract AccountDetailsDao accountDetailsDao();

    public abstract CategoryDetailsDao categoryDetailsDao();

    public abstract ExpenseDetailsDao expenseDetailsDao();

    public abstract IncomeDetailsDao incomeDetailsDao();

    public abstract UserProfileDao userProfileDao();

}
