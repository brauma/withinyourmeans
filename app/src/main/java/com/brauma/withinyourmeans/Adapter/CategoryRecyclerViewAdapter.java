package com.brauma.withinyourmeans.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Category> dataSet;
    private Context context;
    private DatabaseHandler myDb;
    private Category recentlyDeleted;
    private int recentlyDeletedPosition;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<Category> data) {
        this.context = context;
        this.dataSet = data;
        myDb = new DatabaseHandler(context);
    }

    public void deleteItem(int position){
        recentlyDeleted = dataSet.get(position);
        recentlyDeletedPosition = position;
        dataSet.remove(position);
        //TODO database delete category
        //myDb.deleteExpense(recentlyDeleted);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataSet.size());
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = ((Activity) context).findViewById(R.id.my_recycler_view);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        dataSet.add(recentlyDeletedPosition,
                recentlyDeleted);
        myDb.insertCategory(recentlyDeleted);
        notifyItemInserted(recentlyDeletedPosition);
        //notifyItemRangeChanged(recentlyDeletedPosition, dataSet.size());
    }

    public void swapDataSets(ArrayList<Category> data) {
        // clear + addAll fixes indexarrayoutofbounds exception on deleting last element in the list
        this.dataSet.clear();
        this.dataSet.addAll(data);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView categoryIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            this.categoryIcon = (ImageView) itemView.findViewById(R.id.cat_icon);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView tvCategoryName = holder.tvCategoryName;
        ImageView categoryIcon = holder.categoryIcon;
        CardView cardView = holder.cardView;

        tvCategoryName.setText(dataSet.get(listPosition).get_name());
        categoryIcon.setImageResource(dataSet.get(listPosition).get_icon());
        cardView.setCardBackgroundColor(Integer.parseInt(dataSet.get(listPosition).get_color()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}