package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.openclassrooms.realestatemanager.model.Flat;

public class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private final View mContents;

    CustomInfoAdapter(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mWindow = inflater.inflate(R.layout.marker_custom_info, null);
        mContents = inflater.inflate(R.layout.marker_customer_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {

        String picPath = (String) marker.getTag();
        if (picPath.equals("")) ((ImageView) view.findViewById(R.id.pic)).setImageResource(R.drawable.default_flat);
        else ((ImageView) view.findViewById(R.id.pic)).setImageBitmap(BitmapFactory.decodeFile(picPath));

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null && snippet.length() > 12) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.LTGRAY), 0, 10, 0);
            snippetText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 12, snippet.length(), 0);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText(snippet);
        }
    }
}
