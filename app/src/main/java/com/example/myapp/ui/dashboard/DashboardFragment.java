package com.example.myapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.adapter.MyAdapter;
import com.example.myapp.databinding.FragmentDashboardBinding;
import com.example.myapp.db.domain.Result;
import com.example.myapp.db.viewModel.ResultViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import share.ShareViewModel;

public class DashboardFragment extends Fragment {
    private List<Result> all = new ArrayList<>();
    private ShareViewModel shareViewModel;
    private ResultViewModel resultViewModel;
    private MyAdapter adapter;
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private List<Result> results = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        resultViewModel =new ViewModelProvider(this).get(ResultViewModel.class);
        resultViewModel.getResults().observe(getViewLifecycleOwner(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                all = results;
                adapter.setResults(results);
                adapter.notifyDataSetChanged();
            }
        });
        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        shareViewModel.getLog().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.data.setEnabled(aBoolean);
            }
        });
        binding.data.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        adapter = new MyAdapter();
        binding.data.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                Result result = all.get(viewHolder.getPosition());
                resultViewModel.delete(result);
            }
        }).attachToRecyclerView(binding.data);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}