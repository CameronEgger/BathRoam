package com.example.flame.bathroam;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by flame on 10/28/2017.
 */

public class BRInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context c;
    AttributeSet as;
    @Override
    public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
        MarkerWindow mw = new MarkerWindow(c, as);
        Marker ma = (Marker) marker.getTag();
        System.out.println("Marker: " + ma);
        mw.setText(Html.fromHtml("<h1>" + ma.getName() + "</h1><p><b>Latitude</b>: " + ma.getLat() + "<br /><b>Longitutde</b>: " + ma.getLon() + "<br /><b>Type</b>: " + ma.getType() + "<br /><b>Description: " + ma.getInfo() + "</p></div>"));
        return mw;
    }

    @Override
    public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
        /*MarkerWindow mw = new MarkerWindow(c, as);
        Marker ma = (Marker) marker.getTag();
        System.out.println("Marker: " + ma);
        mw.setText(Html.fromHtml("<h1>" + ma.getName() + "</h1><p><b>Latitude</b>: " + ma.getLat() + "<br /><b>Longitutde</b>: " + ma.getLon() + "<br /><b>Type</b>: " + ma.getType() + "</p>"));
        return mw;*/
        return null;
    }
}
