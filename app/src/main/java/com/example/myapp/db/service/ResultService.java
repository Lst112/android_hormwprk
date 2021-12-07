package com.example.myapp.db.service;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.myapp.db.Dao.ResultDao;
import com.example.myapp.db.Dao.UserDao;
import com.example.myapp.db.database.ResultDatabase;
import com.example.myapp.db.domain.Result;
import com.example.myapp.db.domain.User;

import java.util.List;

public class ResultService {
    private ResultDao resultDao;
    private UserDao userDao;
    private ResultDatabase resultDatabase;


    public ResultService(Context context) {
        this.resultDatabase = Room.databaseBuilder(context.getApplicationContext(), ResultDatabase.class, "resultDatabase").build();
        this.resultDao = resultDatabase.getResultDao();
        this.userDao = resultDatabase.getUserDao();
    }
    public LiveData<List<Result>> getSome(String s){ return resultDao.getSome("%"+s+"%"); }
    public LiveData<List<Result>> getAll(){ return resultDao.select(); }
    public void insert(Result...results){ new Insert(resultDao).execute(results); }
    public void delete(Result...results){ new Delete(resultDao).execute(results); }
    public void update(Result...results){ new Update(resultDao).execute(results); }
    public void deleteAll() { resultDao.deleteAll(); }

//    user相关方法
    public LiveData<List<User>> getAllUser() { return userDao.selectUser(); }
    public void InsertUser(User... users) { new InsertUser(this.userDao).execute(users); }
    public void deleteUser(User...users){ new DeleteUser(this.userDao).execute(users); }

    static class Insert extends AsyncTask<Result,Void,Void> {
        ResultDao resultDao;
        public Insert(ResultDao resultDao) {
            this.resultDao = resultDao;
        }
        @Override
        protected Void doInBackground(Result... words) {
            resultDao.insert(words);
            return null;
        }
    }
    static class Delete extends AsyncTask<Result,Void,Void>{
        private ResultDao resultDao;
        public Delete(ResultDao resultDao) {
            this.resultDao = resultDao;
        }
        @Override
        protected Void doInBackground(Result... words) {
            resultDao.delete(words);
            return null;
        }
    }

    static class Update extends AsyncTask<Result,Void,Void>{
        private ResultDao resultDao;
        public Update(ResultDao resultDao) {
            this.resultDao = resultDao;
        }
        @Override
        protected Void doInBackground(Result... results) {
            resultDao.update(results);
            return null;
        }
    }

    static class DeleteAll extends AsyncTask<Void,Void,Void>{
        private ResultDao resultDao;
        public DeleteAll(ResultDao resultDao) {
            this.resultDao = resultDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            resultDao.deleteAll();
            return null;
        }
    }

    static class InsertUser extends AsyncTask<User, Void, Void> {
        UserDao userDao;
        public InsertUser(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users);
            return null;
        }
    }

    static class DeleteUser extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public DeleteUser(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users);
            return null;
        }
    }
}
