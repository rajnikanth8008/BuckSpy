package com.rajnikanth.app.buckspy.database;

import androidx.room.Room;
import android.content.Context;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDataBase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDataBase.class, "BuckSpy").fallbackToDestructiveMigration().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDataBase getAppDatabase() {
        return appDatabase;
    }
}
