package com.brauma.withinyourmeans.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brauma.withinyourmeans.Adapter.CategoryRecyclerViewAdapter;
import com.brauma.withinyourmeans.Adapter.ExpenseRecyclerViewAdapter;
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

public class ListCategoryFragment extends Fragment {
    private View view;

    private DatabaseHandler myDb;
    private CategoryRecyclerViewAdapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<Category> categories;

    public ListCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        myDb = new DatabaseHandler(getActivity());

        categories = myDb.getCategories();

        initRecycleView();
        initSwipeAction();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewCategoryFraction();
            }
        });
        fab.show();

        return view;
    }

    private void openNewCategoryFraction() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, new AddCategoryFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.swapDataSets(myDb.getCategories());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initRecycleView() {
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CategoryRecyclerViewAdapter(getActivity(), categories);
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
}
