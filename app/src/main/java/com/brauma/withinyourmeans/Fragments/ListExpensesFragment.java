package com.brauma.withinyourmeans.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brauma.withinyourmeans.Adapter.ExpenseRecyclerViewAdapter;
import com.brauma.withinyourmeans.Model.Balance;
import com.brauma.withinyourmeans.Model.Bar;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.brauma.withinyourmeans.Utility.DateHelper;
import com.brauma.withinyourmeans.Utility.TimeIntervall;
import com.brauma.withinyourmeans.View.BarIndicatorView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;


public class ListExpensesFragment extends Fragment {
    private View view;

    private DatabaseHandler myDb;
    private ExpenseRecyclerViewAdapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<Expense> expenses;
    private BarIndicatorView barIndicatorView;
    private long from, to;

    public ListExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_expenses, container, false);

        myDb = new DatabaseHandler(getActivity());

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

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewExpenseFraction();
            }
        });
        fab.show();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.swapDataSets(myDb.getExpensesBetweenDates(from, to));
        barIndicatorView.setBars(myDb.getCategorySumsBetweenDates(from, to));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Balance balance = myDb.getBalanceByDate(from);
        int sum = 0;
        for(Bar categorySum : myDb.getCategorySumsBetweenDates(from, to)){
            sum += categorySum.getValue();
        }

        balance.setSpent(sum);
        myDb.updateBalance(balance);
    }

    private void initRecycleView() {
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExpenseRecyclerViewAdapter(getActivity(), expenses);
        recyclerView.setAdapter(adapter);
    }

    private void initSwipeAction() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
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

    private void initBarIndicatorView() {
        barIndicatorView = view.findViewById(R.id.Barindicator);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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

    public void openNewExpenseFraction() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, new AddExpenseFragment())
                .addToBackStack(null)
                .commit();
    }

}
