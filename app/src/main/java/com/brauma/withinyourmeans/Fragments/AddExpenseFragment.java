package com.brauma.withinyourmeans.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.brauma.withinyourmeans.Utility.DateHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;


public class AddExpenseFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private DatabaseHandler myDb;
    private static View view;
    private static ArrayList<Category> categories;
    private TextView dateText;

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        myDb = new DatabaseHandler(getActivity());
        categories = myDb.getCategories();
        ArrayList<String> cat = createArrayListFromObjects();

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dateText =  view.findViewById(R.id.date_textview);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_check_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        return view;
    }

    private ArrayList<String> createArrayListFromObjects() {
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < categories.size(); i++){
            result.add(categories.get(i).get_name());
        }
        return result;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //TextView dateText =  view.findViewById(R.id.date_textview);
        // month + 1 because the dialog counts months from 0 to 11
        dateText.setText(year + "-" + (month + 1)  + "-" + dayOfMonth);
    }

    public void addExpense(){
        Integer amount = null;
        String name = null;
        Long date = null;
        String category = null;

        EditText amountText = (EditText) view.findViewById(R.id.amount_textview);
        if (amountText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        amount = Integer.parseInt(amountText.getText().toString());

        EditText nameText = (EditText) view.findViewById(R.id.name_textview);
        if (nameText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_name), Toast.LENGTH_SHORT).show();
            return;
        }

        name = nameText.getText().toString();

        TextView dateText = (TextView) view.findViewById(R.id.date_textview);

        if (!dateText.getText().toString().equals(getResources().getString(R.string.date_text))) {
            date = DateHelper.strDateToEpoch(dateText.getText().toString());
        } else {
            date = DateHelper.dateToEpoch(Calendar.getInstance().getTime());
        }

        Log.e("DATE ORIGINAL", Calendar.getInstance().getTime().toString());

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        if (spinner.getSelectedItem().toString().isEmpty()) {
            return;
        }
        category = spinner.getSelectedItem().toString();

        Expense e = new Expense(amount, name, category, date);

        boolean success = myDb.insertExpense(e);

        if (success) {
            Log.e("DB", "sikerült");
            /*getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                    .replace(R.id.fragment_container, listExpensesFragment)
                    .commit();
            */
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            Log.e("DB", "nem sikerült");
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_database), Toast.LENGTH_LONG).show();
        }
    }
}
