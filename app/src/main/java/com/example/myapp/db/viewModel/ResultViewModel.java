package com.example.myapp.db.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapp.db.domain.Result;
import com.example.myapp.db.domain.User;
import com.example.myapp.db.service.ResultService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResultViewModel extends AndroidViewModel {
    private ResultService service;
    public ResultViewModel(@NonNull @NotNull Application application) {
        super(application);
        service = new ResultService(application.getApplicationContext());
    }


    public LiveData<List<Result>> getResults() {
        return service.getAll();
    }

    public void insert(Result... results) {
        service.insert(results);
    }

    public void delete(Result... results) {
        service.delete(results);
    }

    public void update(Result... results) {
        service.update(results);
    }
    public void deleteAll() {
        service.deleteAll();
    }

    public LiveData<List<Result>> getSomeWords(String s) {
        return service.getSome(s);
    }

    public LiveData<List<User>> getUsers() {
        return service.getAllUser();
    }
    public void insert(User... users) {
        service.InsertUser(users);
    }
    public void delete(User...users) {
        service.deleteUser(users);
    }
}
