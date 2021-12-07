package com.example.myapp.adapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.db.domain.Result;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Result> results = new ArrayList<>();
    public MyAdapter(List<Result> results) {
        this.results = results;
    }
    public MyAdapter() { }
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder =  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parent.getContext())
                        .setTitle("detail")
                        .setMessage(holder.detail.getText().toString())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.textId.setText(Integer.toString(result.getId()));
        holder.Key.setText(result.getKey());
        holder.detail.setText(result.getDetail());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textId;
        TextView Key;
        TextView detail;
        public ViewHolder(View itemView) {
            super(itemView);
            Key = itemView.findViewById(R.id.Key);
            textId = itemView.findViewById(R.id.textId);
            detail = itemView.findViewById(R.id.detail);
        }
    }
}
