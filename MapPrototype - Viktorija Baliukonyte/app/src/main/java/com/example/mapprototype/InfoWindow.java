package com.example.mapprototype;

import android.view.LayoutInflater;
import android.view.View;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public InfoWindow(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_infowindow, null);
    }

    private void renderWindowText(Marker marker, View view)
    {
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
       // Button button = (Button) view.findViewById(R.id.infoWindowButton);
        //final EditText text =  (EditText) view.findViewById(R.id.infoWindowEditText);

        String snippet = marker.getSnippet();
        final TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

        if(!title.equals("")) {
            tvTitle.setText(title);
        }

        if(!snippet.equals("")) {
            tvSnippet.setText(snippet);
        }

       // button.setOnClickListener(new View.OnClickListener()
      //  {
       //     @Override
       //     public void onClick(View v)
       //     {
                //tvTitle = text.getText().toString();

        //        tvSnippet.setText(text.getText().toString());
        //    }
        //});
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
