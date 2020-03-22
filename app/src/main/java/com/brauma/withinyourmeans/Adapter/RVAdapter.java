package com.brauma.withinyourmeans.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.brauma.withinyourmeans.Activity.MainActivity;
import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.brauma.withinyourmeans.Utility.DateHelper;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private ArrayList<Expense> dataSet;
    private Context context;
    private DatabaseHandler myDb;
    private ArrayList<Category> categories;

    public RVAdapter(Context context, ArrayList<Expense> data) {
        this.context = context;
        this.dataSet = data;
        myDb = new DatabaseHandler(context);
        categories = myDb.getCategories();
    }

    public void swapDataSets(ArrayList<Expense> data) {
        // clear + addAll fixes indexarrayoutofbounds exception on deleting last element in the list
        this.dataSet.clear();
        this.dataSet.addAll(data);

        // this might be overkill
        this.categories.clear();
        this.categories = myDb.getCategories();

        //this.dataSet = data;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount;
        TextView tvName;
        TextView tvDate;
        ImageView categoryIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.name);
            this.tvAmount = (TextView) itemView.findViewById(R.id.amount);
            this.tvDate= (TextView) itemView.findViewById(R.id.date);
            this.categoryIcon = (ImageView) itemView.findViewById(R.id.cat_icon);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
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
        TextView tvDate = holder.tvDate;
        TextView tvName = holder.tvName;
        ImageView categoryIcon = holder.categoryIcon;
        CardView cardView = holder.cardView;

        tvAmount.setText(String.format("%d Ft", dataSet.get(listPosition).get_amount()));
        tvName.setText(dataSet.get(listPosition).get_name());
        tvDate.setText(DateHelper.epochToStrDate(dataSet.get(listPosition).get_date()));

        String category = dataSet.get(listPosition).get_category();

        for (int i = 0; i < categories.size(); i++){
            if(categories.get(i).get_name().equals(category)){
                categoryIcon.setImageResource(categories.get(i).get_icon());
                cardView.setCardBackgroundColor(Integer.parseInt(categories.get(i).get_color()));
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}