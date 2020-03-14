package com.brauma.withinyourmeans;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAmount;
        TextView tvDesc;
        ImageView categoryIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvAmount = (TextView) itemView.findViewById(R.id.amount);
            this.tvDesc = (TextView) itemView.findViewById(R.id.description);
            this.categoryIcon = (ImageView) itemView.findViewById(R.id.cat_icon);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
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
        CardView cardView = holder.cardView;

        //Get category to find out what color the card should be
        int category = dataSet.get(listPosition).getCategoryIcon();

        Log.e("DATA SET", String.format("value = %d", dataSet.get(listPosition).getAmount()));

        tvAmount.setText(String.format("%d Ft", dataSet.get(listPosition).getAmount()));
        tvDesc.setText(dataSet.get(listPosition).getDescription());
        categoryIcon.setImageResource(dataSet.get(listPosition).getCategoryIcon());

        switch(category){
            case R.drawable.ic_food:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorFood));
                break;
            case R.drawable.ic_bad_food:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorBadFood));
                break;
            case R.drawable.ic_bill:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorBill));
                break;
            case R.drawable.ic_booze:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorBooze));
                break;
            case R.drawable.ic_clothing:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorClothing));
                break;
            case R.drawable.ic_smoking:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorSmoking));
                break;
            case R.drawable.ic_rent:
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorRent));
                break;
            default: break;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}