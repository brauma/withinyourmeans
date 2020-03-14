package com.brauma.withinyourmeans;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAmount;
        TextView tvDesc;
        ImageView categoryIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvAmount = (TextView) itemView.findViewById(R.id.amount);
            this.tvDesc = (TextView) itemView.findViewById(R.id.description);
            this.categoryIcon = (ImageView) itemView.findViewById(R.id.cat_icon);
        }
    }

    public RVAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView tvAmount = holder.tvAmount;
        TextView tvDesc = holder.tvDesc;
        ImageView categoryIcon = holder.categoryIcon;

        Log.e("DATA SET", String.format("value = %d", dataSet.get(listPosition).getAmount()));

        tvAmount.setText(String.format("%d Ft", dataSet.get(listPosition).getAmount()));
        tvDesc.setText(dataSet.get(listPosition).getDescription());
        categoryIcon.setImageResource(dataSet.get(listPosition).getCategoryIcon());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}