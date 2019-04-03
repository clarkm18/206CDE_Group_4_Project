package com.example.mapprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MapType extends AppCompatActivity {
    Spinner mapSpinner;
    Button button;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("mapTypeButton", "Success 3!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_map_type);

        button = (Button) findViewById(R.id.confirm);

        mapSpinner = (Spinner) findViewById(R.id.mapSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MapType.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.mapTypes));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(myAdapter);

        sendInfo();
    }

    public void sendInfo()
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                type = mapSpinner.getSelectedItem().toString();

                Intent i = new Intent(MapType.this, MapsActivity.class);
                i.putExtra("MapType", type);

                setResult(3, i);
                finish();
            }
        });
    }
}
