package com.brauma.withinyourmeans.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;

import java.util.ArrayList;
import java.util.Calendar;

import com.brauma.withinyourmeans.Utility.DateHelper;

public class AddExpenseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DatabaseHandler myDb;

    private static ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_expense_activity);

        myDb = new DatabaseHandler(this);
        categories = myDb.getCategories();
        ArrayList<String> cat = createArrayListFromObjects();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView dateText = (TextView) findViewById(R.id.date_textview);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Button add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer amount = null;
                String name = null;
                Long date = null;
                String category = null;

                EditText amountText = (EditText) findViewById(R.id.amount_textview);
                if (amountText.getText().toString().isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, AddExpenseActivity.this.getString(R.string.error_amount), Toast.LENGTH_SHORT).show();
                    return;
                }

                amount = Integer.parseInt(amountText.getText().toString());

                EditText nameText = (EditText) findViewById(R.id.name_textview);
                if (nameText.getText().toString().isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, AddExpenseActivity.this.getString(R.string.error_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                name = nameText.getText().toString();

                TextView dateText = (TextView) findViewById(R.id.date_textview);

                if (!dateText.getText().toString().equals(getResources().getString(R.string.date_text))) {
                    date = DateHelper.strDateToEpoch(dateText.getText().toString());
                } else {
                    date = DateHelper.dateToEpoch(Calendar.getInstance().getTime());
                }

                Log.e("DATE ORIGINAL", Calendar.getInstance().getTime().toString());

                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                if (spinner.getSelectedItem().toString().isEmpty()) {
                    return;
                }
                category = spinner.getSelectedItem().toString();

                Expense e = new Expense(amount, name, category, date);

                boolean success = myDb.insertExpense(e);

                if (success) {
                    Log.e("DB", "sikerült");
                    finish();
                } else {
                    Log.e("DB", "nem sikerült");
                    Toast.makeText(AddExpenseActivity.this, AddExpenseActivity.this.getString(R.string.error_database), Toast.LENGTH_LONG).show();
                }

            }
        });
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
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView dateText = (TextView) findViewById(R.id.date_textview);
        // month + 1 because the dialog counts months from 0 to 11
        dateText.setText(year + "-" + (month + 1)  + "-" + dayOfMonth);
    }
}
