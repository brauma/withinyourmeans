package com.brauma.withinyourmeans.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brauma.withinyourmeans.Model.Bar;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.brauma.withinyourmeans.Utility.DateHelper;
import com.brauma.withinyourmeans.Utility.TimeIntervall;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsFragment extends Fragment {
    private View view;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);

        TimeIntervall currentMonth = DateHelper.getMonth(Calendar.getInstance().getTime());

        long from = currentMonth.getFrom();
        long to = currentMonth.getTo();

        DatabaseHandler myDb = new DatabaseHandler(getActivity());
        ArrayList<Bar> data = myDb.getCategorySumsBetweenDates(from, to);

        BarChart barChart = view.findViewById(R.id.barchart);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Integer> barColors = new ArrayList<>();

        int j = 1;

        for(Bar entry : data){
            barEntries.add(new BarEntry(j, entry.getValue()));
            barColors.add(entry.getColor());
            j++;
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Spent by category");
        BarData barData = new BarData(dataSet);
        dataSet.setColors(barColors);
        barChart.setData(barData);
        barChart.animateY(1000 );

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();

        return view;
    }
}
