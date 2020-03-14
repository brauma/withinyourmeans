package com.brauma.withinyourmeans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler myDb;
    private static RecyclerView.Adapter adapter;
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

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
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


        data = new ArrayList<>();
        expenses = (ArrayList) myDb.getExpenses();

        for (int i = 0; i < expenses.size(); i++) {
            data.add(new DataModel(
                    expenses.get(i).get_amount(),
                    expenses.get(i).get_description(),
                    expenses.get(i).get_id(),
                    getCategoryIcon(expenses.get(i).get_category())
            ));
        }

        /*
        for (int i = 0; i < MyData.amountArray.length; i++) {
            data.add(new DataModel(
                    MyData.amountArray[i],
                    MyData.descArray[i],
                    MyData.id_[i],
                    MyData.drawableArray[i])
            );
        }*/

        Log.e("DATA SIZE", String.format("value = %d", expenses.size()));

        adapter = new RVAdapter(data);
        recyclerView.setAdapter(adapter);
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
}
