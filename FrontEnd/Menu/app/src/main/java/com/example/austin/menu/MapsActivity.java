package com.example.austin.menu;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.nearby.messages.Distance;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Random rng;
    LocationListener updater;
    LatLng lastLL = null;
    GoogleApiClient.ConnectionCallbacks gcc = null;
    Location loc;

    public Button getBut()
    {
        return (Button)findViewById(R.id.button);
    }

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    selectedFragment = ItemOneFragment.newInstance();
                    break;
                case R.id.navigation_create:
                    selectedFragment = ItemTwoFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = ItemThreeFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    updater = new LocationListener() {
        @Override
        public void onLocationChanged(Location l) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(l.getLatitude(), l.getLongitude())));
            LatLng currentLL = new LatLng(l.getLatitude(), l.getLongitude());
            if(lastLL != null) {
                mMap.addPolyline(new PolylineOptions().add(lastLL, currentLL));
            }
            lastLL = currentLL;
        }
    };
    rng = new Random();
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    gcc = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            //alert("HI!!!!!!");
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        LocationRequest lr = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5);
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, lr, updater);
                        loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if(loc!=null) {
                            TextView t = (TextView) findViewById(R.id.textDummy);
                            if(t!=null)
                                t.setText(loc.getLatitude() + " " + loc.getLongitude() + " " + loc.getAltitude());
                        }

                        //alert("Location: " + l)
                    }catch(SecurityException e){
                        alert("Not privileged to send location requests");
                    }catch(IllegalStateException e){
                        alert("Program found in an illegal state");
                    }
                    //h.postDelayed(this, 1000);
                }
            }, 1000);
        }

        @Override
        public void onConnectionSuspended(int i) {
            alert("Suspension " + i);
        }
    };
    GoogleApiClient.OnConnectionFailedListener failList = new GoogleApiClient.OnConnectionFailedListener(){
        @Override
        public void onConnectionFailed(ConnectionResult cr){
            alert("CONNECTION FAILURE");
            mGoogleApiClient.connect();
        }
    };
    // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(gcc)
                .addOnConnectionFailedListener(failList)
                .addApi(LocationServices.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, 33);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, mapFragment.newInstance());
        transaction.commit();

    }

    private void alert(String s){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Message");
        b.setMessage(s);
        b.show();
    }

    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 33) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                //alert("You might be a success");
                GoogleSignInAccount acc = result.getSignInAccount();
                //TODO: Something fancy with the id.
            } else {
                alert("Failure of Google Sign In");
            }
        }
    }

    @Override
    protected void onStop(){
        if(gcc != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, updater);
        }
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        /*LocationManager locMan =  (LocationManager) this.getApplicationContext().getSystemService(LOCATION_SERVICE);
        if(locMan != null) {
            LocationListener updater = new LocationListener() {
                @Override
                public void onStatusChanged(String s, int i, Bundle b) {
                    alert("s: " + s + " " + i);
                }

                @Override
                public void onProviderEnabled(String s) {
                    alert("Enabled: " + s);
                }

                @Override
                public void onProviderDisabled(String s) {
                    alert("Disabled: " + s);
                }

                @Override
                public void onLocationChanged(Location l) {
                    alert("LOC" + l.getLatitude() + " " + l.getLongitude());
                }
            };
            Criteria cri = new Criteria();
            cri.setAccuracy(Criteria.ACCURACY_FINE);
            cri.setAltitudeRequired(true);
            String provider = locMan.getBestProvider(cri, false);
            try {
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("LOL");
                b.setMessage("Provider: " + provider);
                b.show();
                if(provider != null) {
                    locMan.requestLocationUpdates(provider, 1000, 1, updater);
                }
            } catch (SecurityException e) {
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("LOL");
                b.setMessage("D:");
                b.show();
            }
        } else {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Location Manager does not exist");
            b.setMessage("This app is not usable since you do not have a location manager");
            b.show();
        }*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    protected void setupMarkers(Marker[] markers){
        for(Marker marker : markers){
            com.google.android.gms.maps.model.Marker ma = mMap.addMarker(new MarkerOptions().position(new LatLng(marker.getLat(), marker.getLon())).title(marker.getName()).icon(BitmapDescriptorFactory.defaultMarker(marker.getHue())).snippet(marker.getInfo()));
            ma.setTag(marker);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        Marker[] fakes = new Marker[90];
        for(int i = 0; i < fakes.length; i++){
            fakes[i] = new Marker(rng.nextDouble() % .1 + 30.21, rng.nextDouble() % .1 + -97.79, "Marker " + i, (i % 3 == 0 ? Marker.TYPE.MALE : (i % 3 == 1 ? Marker.TYPE.FEMALE : Marker.TYPE.NEUTRAL)), "AB", 10);
        }
        BRInfoWindow br = new BRInfoWindow();
        br.c = this;
        br.as = null;
        mMap.setInfoWindowAdapter(br);
        setupMarkers(fakes);
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34,151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
