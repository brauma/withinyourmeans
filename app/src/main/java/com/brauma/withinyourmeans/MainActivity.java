package com.brauma.withinyourmeans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler myDb;
    private static RVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    private static ArrayList<DataModel> data;
    private static ArrayList<Expense> expenses;

    static View.OnClickListener myOnClickListener;
    private static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHandler(this);

        myOnClickListener = new MyOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Floating button on the bottom of the screen - > opens new activity for data insertion
        fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, NewDataActivity.class);
                startActivity(myIntent);
            }
        });


        expenses = (ArrayList) myDb.getExpenses();
        data = (ArrayList) getDataModel(expenses);


        //TODO fix this shit with the data and expenses

        adapter = new RVAdapter(data);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                myDb.deleteData(expenses.get(viewHolder.getAdapterPosition()));
                data.remove(viewHolder.getAdapterPosition());
                expenses.remove(viewHolder.getAdapterPosition());
                //adapter.swapDataSets(data);

                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                adapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(), adapter.getItemCount());

            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        expenses = (ArrayList) myDb.getExpenses();
        data = (ArrayList) getDataModel(expenses);
        adapter.swapDataSets(data);
        adapter.notifyDataSetChanged();
    }

    // This runs when you tap on an item on the list
    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "You are doing this in the right order!", Toast.LENGTH_SHORT).show();
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

    private List<DataModel> getDataModel(List<Expense> expenses){
        data = new ArrayList<>();

        for (int i = 0; i < expenses.size(); i++) {
            data.add(new DataModel(
                    expenses.get(i).get_amount(),
                    expenses.get(i).get_description(),
                    expenses.get(i).get_id(),
                    getCategoryIcon(expenses.get(i).get_category())
            ));
        }
        return data;
    }
}
