package com.brauma.withinyourmeans.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brauma.withinyourmeans.Adapter.RVAdapter;
import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.brauma.withinyourmeans.View.BarIndicatorView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler myDb;
    private RVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    private static ArrayList<Expense> expenses;

    public static View.OnClickListener myOnClickListener;
    private static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHandler(this);
        myOnClickListener = new MyOnClickListener(this);

        expenses = myDb.getExpenses();

        initBarIndicatorView();
        initRecycleView();
        initSwipeAction();
        initFAB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.swapDataSets(myDb.getExpenses());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.change_month_item:
                //TODO: change month dialog window + function
                return true;
            case R.id.add_category_item:
                Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFAB() {
        // Floating button on the bottom of the screen - > opens new activity for data insertion
        fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void initSwipeAction() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                myDb.deleteExpense(expenses.get(viewHolder.getAdapterPosition()));

                Log.e("ERROR", String.valueOf(viewHolder.getAdapterPosition()));
                expenses.remove(viewHolder.getAdapterPosition());
                adapter.swapDataSets(myDb.getExpenses());
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RVAdapter(this, expenses);
        recyclerView.setAdapter(adapter);
    }

    private void initBarIndicatorView() {
        BarIndicatorView barIndicatorView = (BarIndicatorView) findViewById(R.id.Barindicator);

        //TODO initialize Bar indicator

    }

    // This runs when you tap on an item on the list
    public class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "You are doing this in the right order!", Toast.LENGTH_SHORT).show();
        }

    }
}
