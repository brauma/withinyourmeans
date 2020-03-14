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

import com.brauma.withinyourmeans.MainActivity;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    private ArrayList<Expense> dataSet;
    private Context context;

    public RVAdapter(Context context, ArrayList<Expense> data) {
        this.context = context;
        this.dataSet = data;
    }

    public void swapDataSets(ArrayList<Expense> data) {
        // clear + addAll fixes indexarrayoutofbounds exception on deleting last element in the list
        this.dataSet.clear();
        this.dataSet.addAll(data);
        //this.dataSet = data;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount;
        TextView tvName;
        TextView tvDesc;
        ImageView categoryIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.name);
            this.tvAmount = (TextView) itemView.findViewById(R.id.amount);
            this.tvDesc = (TextView) itemView.findViewById(R.id.description);
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
        TextView tvDesc = holder.tvDesc;
        TextView tvName = holder.tvName;
        ImageView categoryIcon = holder.categoryIcon;
        CardView cardView = holder.cardView;

        Log.e("DATA SET", String.format("value = %d", dataSet.get(listPosition).get_amount()));

        tvAmount.setText(String.format("%d Ft", dataSet.get(listPosition).get_amount()));
        tvName.setText(dataSet.get(listPosition).get_name());
        tvDesc.setText(dataSet.get(listPosition).get_description());
        categoryIcon.setImageResource(getCategoryIcon(dataSet.get(listPosition).get_category()));

        setBackgroundColorByCategory(cardView, dataSet.get(listPosition).get_category());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void setBackgroundColorByCategory(CardView cardView, String category){
        switch (category) {
            case "Food":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorFood));
                break;
            case "Unhealthy Food":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorBadFood));
                break;
            case "Bill":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorBill));
                break;
            case "Alcohol":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorBooze));
                break;
            case "Clothing":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorClothing));
                break;
            case "Tobacco":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorSmoking));
                break;
            case "Rent":
                cardView.setCardBackgroundColor(cardView.getResources().getColor(R.color.colorRent));
                break;
            default:
                break;
        }
    }

    private Integer getCategoryIcon(String category){
        switch(category){
            case "Bill": return R.drawable.ic_bill;
            case "Rent": return R.drawable.ic_rent;
            case "Alcohol": return R.drawable.ic_booze;
            case "Tobacco": return R.drawable.ic_smoking;
            case "Unhealthy Food": return R.drawable.ic_bad_food;
            case "Clothing": return R.drawable.ic_clothing;
            default: return R.drawable.ic_food;
        }
    }
}