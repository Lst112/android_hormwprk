package com.example.myapp.ui.logIn;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.databinding.LogInFragmentBinding;
import com.example.myapp.db.domain.User;

import java.util.List;

import share.ShareViewModel;

public class LogInFragment extends Fragment {
    private LogInFragmentBinding binding;
    private LogInViewModel mViewModel;
    private ShareViewModel shareViewModel;

    private List<User> all;

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);

        binding = LogInFragmentBinding.inflate(inflater, container, false);

        binding.buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.account.getText().toString() == null
                        || binding.password.getText().toString() == null){
                    Toast.makeText(getContext(),"输入不可为空",Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0;i < all.size();i++){
                    if(binding.account.getText().toString().equals(all.get(i).getAccount())
                    && binding.password.getText().toString().equals(all.get(i).getPassword())){
                        shareViewModel.setUser(all.get(i).getName());
                        shareViewModel.setLog(true);
                    }
                }
            }
        });
        return inflater.inflate(R.layout.log_in_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        // TODO: Use the ViewModel
    }

}