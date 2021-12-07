package com.example.myapp.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.databinding.FragmentNotificationsBinding;
import com.example.myapp.db.domain.User;
import com.example.myapp.db.viewModel.ResultViewModel;
import com.example.myapp.ui.LogActivity;

import share.ShareViewModel;

public class NotificationsFragment extends Fragment {
    private ShareViewModel shareViewModel;
    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ResultViewModel resultViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        User user1 = new User("李赛通","123","234");
        resultViewModel.insert(user1);
        shareViewModel.getLog().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == false){
                    binding.log.setText("点此处登录");
                }
                else{
                    binding.log.setText(shareViewModel.getUser());
                }
            }
        });
        binding.log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogActivity.class);
                startActivity(intent);
            }
        });
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("detail")
                        .setMessage("是否要退出登录？")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shareViewModel.setLog(false);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}