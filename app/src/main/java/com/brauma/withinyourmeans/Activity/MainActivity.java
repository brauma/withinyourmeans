package com.brauma.withinyourmeans.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.brauma.withinyourmeans.Fragments.AddExpenseFragment;
import com.brauma.withinyourmeans.Fragments.ListCategoryFragment;
import com.brauma.withinyourmeans.Fragments.ListExpensesFragment;
import com.brauma.withinyourmeans.Fragments.StatisticsFragment;
import com.brauma.withinyourmeans.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ListExpensesFragment expensesFragment;
    private AddExpenseFragment addExpenseFragment;
    private ListCategoryFragment categoryFragment;
    private StatisticsFragment statisticsFragment;
    private FloatingActionButton fab;

    private BottomAppBar bar;
    private int alignment = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expensesFragment = new ListExpensesFragment();
        addExpenseFragment = new AddExpenseFragment();
        categoryFragment = new ListCategoryFragment();
        statisticsFragment = new StatisticsFragment();

        bar = findViewById(R.id.bar);
        setSupportActionBar(bar);
        bar.replaceMenu(R.menu.bottom_bar_menu);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.fragment_container, expensesFragment)
                        .commit();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, expensesFragment)
                .commit();

        initFAB();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.e("MENU", String.valueOf(id));
        switch (id){
            case R.id.action_categories:
                Log.e("MENU", "its here");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.fragment_container, categoryFragment)
                        .addToBackStack(null)
                        .commit();

                return true;
            case R.id.action_statistics:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.fragment_container, statisticsFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFAB() {
        // Floating button on the bottom of the screen - > opens new activity for data insertion
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expensesFragment.isVisible())
                    expensesFragment.openNewExpenseFraction();
                if(addExpenseFragment.isVisible())
                    addExpenseFragment.addExpense();

            }
        });


    }



}
