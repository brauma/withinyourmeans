package com.brauma.withinyourmeans.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brauma.withinyourmeans.Adapter.ImageAdapter;
import com.brauma.withinyourmeans.Model.Category;
import com.brauma.withinyourmeans.R;
import com.brauma.withinyourmeans.SQL.DatabaseHandler;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {

    ImageView color;
    ImageView icon;
    int chosenImage;
    String chosenColor;

    public Integer[] icons = {
            R.drawable.ic_bone, R.drawable.ic_booze,
            R.drawable.ic_bad_food, R.drawable.ic_food,
            R.drawable.ic_barbell, R.drawable.ic_bill,
            R.drawable.ic_rent, R.drawable.ic_brain,
            R.drawable.ic_clean, R.drawable.ic_clothing,
            R.drawable.ic_entertainment, R.drawable.ic_jogging,
            R.drawable.ic_train, R.drawable.ic_smoking,
            R.drawable.ic_pharmacy
    };

    private DatabaseHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_activity);

        color = findViewById(R.id.color_imageView);
        icon = findViewById(R.id.icon_imageView);

        myDb = new DatabaseHandler(this);

        //color.setBackgroundColor(Color.BLACK);
        icon.setBackgroundColor(Color.BLACK);

        final TextView name = findViewById(R.id.name_textview);

        Button button = findViewById(R.id.button);

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconPickerDialog();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                category.set_name(name.getText().toString());
                category.set_icon(chosenImage);
                category.set_color(chosenColor);

                myDb.insertCategory(category);
                finish();
            }
        });
    }

    private void setChosenIcon(int position){
        icon.setImageResource(icons[position]);
        chosenImage = icons[position];
    }

    private void showIconPickerDialog() {
        // Prepare grid view
        GridView gridView = new GridView(this);
        final ImageAdapter imageAdapter = new ImageAdapter(this, icons);
        gridView.setAdapter(imageAdapter);
        gridView.setNumColumns(3);


        // Set grid view to alertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle("Choose icon");
        final AlertDialog dialog = builder.create();

        dialog.show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setChosenIcon(position);
                dialog.dismiss();
            }
        });
    }

    private void showColorPickerDialog() {
        new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Choose category color")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.ok),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                color.setBackgroundColor(envelope.getColor());
                                chosenColor = String.valueOf(envelope.getColor());
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(false) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                .show();
    }
}
