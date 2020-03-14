package com.brauma.withinyourmeans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewDataActivity extends AppCompatActivity {
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
                Log.e(" ONCLICK", " ide bejutott");


                EditText amountText = (EditText) findViewById(R.id.amount_textview);
                int amount = Integer.parseInt(amountText.getText().toString());


                EditText nameText = (EditText) findViewById(R.id.name_textview);
                String name = nameText.getText().toString();



                EditText descText = (EditText) findViewById(R.id.desc_textview);
                String description = descText.getText().toString();



                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String category = spinner.getSelectedItem().toString();
                Log.e("GOTTEN VALUES: ", category);

                /*
                Log.e("GOTTENS: ",  "valami itt nem jó");

                /*
                Expense e = new Expense(amount, name, category, description);
                myDb.insertData(e);
                */
            }
        });
    }

}
