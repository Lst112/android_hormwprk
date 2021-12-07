package com.example.myapp.db.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapp.db.Dao.ResultDao;
import com.example.myapp.db.Dao.UserDao;
import com.example.myapp.db.domain.Result;
import com.example.myapp.db.domain.User;

@Database(entities = {Result.class, User.class},version = 1,exportSchema = false)
public abstract class ResultDatabase extends RoomDatabase {
    public abstract ResultDao getResultDao();
    public abstract UserDao getUserDao();
}
