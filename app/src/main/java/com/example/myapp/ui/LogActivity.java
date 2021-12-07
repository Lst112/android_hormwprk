package com.example.myapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapp.MainActivity;
import com.example.myapp.databinding.ActivityLogBinding;
import com.example.myapp.db.domain.User;
import com.example.myapp.db.viewModel.ResultViewModel;

import java.util.List;

import share.ShareViewModel;

public class LogActivity extends AppCompatActivity {
    private ActivityLogBinding binding;
    private ShareViewModel shareViewModel;
    private List<User> all;
    private ResultViewModel resultViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogBinding.inflate(getLayoutInflater());
        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        all = resultViewModel.getUsers().getValue();
        resultViewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                all = users;
            }
        });
        binding.buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.account.getText().toString() == null
                        || binding.password.getText().toString() == null){
                    Toast.makeText(getApplicationContext(),"输入不可为空",Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0;i < all.size();i++){
                    if(binding.account.getText().toString().equals(all.get(i).getAccount())
                            && binding.password.getText().toString().equals(all.get(i).getPassword())){
                        shareViewModel.setUser(all.get(i).getName());
                        shareViewModel.setLog(true);
                        Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LogActivity.this, MainActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(),"用户不存在或密码错误",Toast.LENGTH_LONG).show();
            }
        });
        setContentView(binding.getRoot());
    }
}