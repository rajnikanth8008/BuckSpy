package com.example.administrator.buckspy.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.administrator.buckspy.dao.UtilizationCategoryMasterDao;
import com.example.administrator.buckspy.dao.UtilizationDetailsDao;
import com.example.administrator.buckspy.entity.UserProfile;
import com.example.administrator.buckspy.entity.UtilizationCategoryMaster;
import com.example.administrator.buckspy.entity.UtilizationDetails;

@Database(entities = {UtilizationCategoryMaster.class, UtilizationDetails.class, UserProfile.class}, version = 3, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UtilizationDetailsDao utilizationDetailsDao();
    public abstract UtilizationCategoryMasterDao utilizationCategoryMasterDao();

}
