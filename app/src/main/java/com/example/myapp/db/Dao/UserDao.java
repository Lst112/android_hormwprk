package com.example.myapp.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapp.db.domain.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("select * from user")
    LiveData<List<User>> selectUser();
    @Insert
    public void insert(User...users);
    @Delete
    public void delete(User...users);
}
