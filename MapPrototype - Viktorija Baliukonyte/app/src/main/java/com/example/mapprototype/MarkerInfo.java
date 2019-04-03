package com.example.mapprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MarkerInfo extends AppCompatActivity {
    EditText text, issueTitle;
    Button button;
    String description, title;
    Spinner titleSpinner;

    boolean isButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);

        text =  (EditText) findViewById(R.id.textField);
        //issueTitle = (EditText) findViewById(R.id.titlesSpinner);
        button = (Button) findViewById(R.id.button);

        titleSpinner = (Spinner) findViewById(R.id.titlesSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MarkerInfo.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.titles));
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
                description = text.getText().toString();
                title = titleSpinner.getSelectedItem().toString();

                Intent i = new Intent(MarkerInfo.this, MapsActivity.class);
                i.putExtra("String", description);
                i.putExtra("Title", title);

                setResult(1, i);
                finish();
            }
        });
    }
}
