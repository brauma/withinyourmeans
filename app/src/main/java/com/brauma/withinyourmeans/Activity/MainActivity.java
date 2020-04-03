package com.brauma.withinyourmeans.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brauma.withinyourmeans.Adapter.RVAdapter;
import com.brauma.withinyourmeans.Model.Balance;
import com.brauma.withinyourmeans.Model.Bar;
import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.brauma.withinyourmeans.Utility.DateHelper;
import com.brauma.withinyourmeans.Utility.TimeIntervall;
import com.brauma.withinyourmeans.View.BarIndicatorView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler myDb;
    private RVAdapter adapter;
    private static RecyclerView recyclerView;

    private static ArrayList<Expense> expenses;

    public static View.OnClickListener myOnClickListener;
    private static FloatingActionButton fab;
    private BarIndicatorView barIndicatorView;
    private long from, to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new DatabaseHandler(this);
        myOnClickListener = new MyOnClickListener(this);

        TimeIntervall currentMonth = DateHelper.getMonth(Calendar.getInstance().getTime());

        from = currentMonth.getFrom();
        to = currentMonth.getTo();

        if (myDb.getBalanceByDate(from) == null) {
            Balance balance = new Balance();
            balance.setSpent(0);
            balance.setBudget(70000);
            balance.setDate(from);

            myDb.insertBalance(balance);
        }

        expenses = myDb.getExpensesBetweenDates(from, to);

        Log.e("FROMTO", String.valueOf(expenses.size()));

        initBarIndicatorView();
        initRecycleView();
        initSwipeAction();
        initFAB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.swapDataSets(myDb.getExpensesBetweenDates(from, to));
        barIndicatorView.setBars(myDb.getCategorySumsBetweenDates(from, to));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Balance balance = myDb.getBalanceByDate(from);
        int sum = 0;
        for(Bar categorySum : myDb.getCategorySumsBetweenDates(from, to)){
            sum += categorySum.getValue();
        }

        balance.setSpent(sum);
        myDb.updateBalance(balance);
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
        fab = findViewById(R.id.floating_action_button);
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
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position);
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
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RVAdapter(this, expenses);
        recyclerView.setAdapter(adapter);
    }

    private void initBarIndicatorView() {
        barIndicatorView = findViewById(R.id.Barindicator);
        barIndicatorView.setMainValue(myDb.getBalanceByDate(from).getBudget());
        barIndicatorView.setBars(myDb.getCategorySumsBetweenDates(from, to));
        barIndicatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBudgetDialog();
            }
        });
    }

    private void showBudgetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edit = dialogView.findViewById(R.id.budget_edit);
        edit.setText(String.valueOf(barIndicatorView.getMainValue()));

        dialogBuilder.setTitle("Change monthly budget");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                barIndicatorView.setMainValue(Integer.parseInt(edit.getText().toString()));
                Balance balance = myDb.getBalanceByDate(from);
                balance.setBudget(Integer.parseInt(edit.getText().toString()));
                myDb.updateBalance(balance);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog budgetDialog = dialogBuilder.create();
        budgetDialog.show();
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
