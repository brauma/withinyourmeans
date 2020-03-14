package com.brauma.withinyourmeans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.brauma.withinyourmeans.Model.Expense;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;

public class AddExpenseActivity extends AppCompatActivity {
    private DatabaseHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_data_activity);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        myDb = new DatabaseHandler(this);

        Button add = (Button) findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer amount = null;
                String name = null;
                String description = null;
                String category = null;

                EditText amountText = (EditText) findViewById(R.id.amount_textview);
                if (amountText.getText().toString().isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "You have to specify the amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                amount = Integer.parseInt(amountText.getText().toString());

                EditText nameText = (EditText) findViewById(R.id.name_textview);
                if (nameText.getText().toString().isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "You have to specify a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                name = nameText.getText().toString();

                EditText descText = (EditText) findViewById(R.id.desc_textview);
                if (!descText.getText().toString().isEmpty()) {
                    description = descText.getText().toString();
                } else {
                    return;
                }

                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                if (spinner.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "You have to specify a category", Toast.LENGTH_SHORT).show();
                    return;
                }
                category = spinner.getSelectedItem().toString();

                Expense e = new Expense(amount, name, category, description);

                boolean success = myDb.insertData(e);

                if (success) {
                    Log.e("DB", "sikerült");
                    finish();
                } else {
                    Log.e("DB", "nem sikerült");
                    Toast.makeText(AddExpenseActivity.this, "Unexpected error with the database", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
