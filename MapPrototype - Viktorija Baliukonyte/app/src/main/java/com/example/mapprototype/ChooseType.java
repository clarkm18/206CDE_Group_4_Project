package com.example.mapprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class ChooseType extends AppCompatActivity {
    Spinner titleSpinner;
    Button button;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        button = (Button) findViewById(R.id.back);

        titleSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ChooseType.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.types));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(myAdapter);

        sendInfo();
    }

    public void sendInfo()
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                type = titleSpinner.getSelectedItem().toString();

                Intent i = new Intent(ChooseType.this, MapsActivity.class);
                i.putExtra("Type", type);

                setResult(2, i);
                finish();
            }
        });
    }
}
