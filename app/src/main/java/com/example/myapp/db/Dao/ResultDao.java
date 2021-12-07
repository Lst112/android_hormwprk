package com.example.myapp.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapp.db.domain.Result;
import com.example.myapp.db.domain.User;

import java.util.List;

@Dao
public interface ResultDao {

    @Query("select * from result")
    LiveData<List<Result>> select();
    @Insert
    public void insert(Result...results);
    @Delete
    public void delete(Result...results);
    @Query("delete from result")
    public void deleteAll();
    @Update
    void update(Result...results);
    @Query("select * from result where `key` Like:s")
    LiveData<List<Result>> getSome(String s);
}
