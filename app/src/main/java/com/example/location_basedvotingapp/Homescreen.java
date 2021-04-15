package com.example.location_basedvotingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.location_basedvotingapp.DBHelper.POLL_ID;
import static com.example.location_basedvotingapp.DBHelper.POLL_LAG;
import static com.example.location_basedvotingapp.DBHelper.POLL_LAT;
import static com.example.location_basedvotingapp.DBHelper.POLL_OWNER;
import static com.example.location_basedvotingapp.DBHelper.POLL_TITLE;
import static com.example.location_basedvotingapp.DBHelper.TABLE_POLL;
import static com.example.location_basedvotingapp.DBHelper.USER_ID;
import static com.example.location_basedvotingapp.MainActivity.MyPREFERENCES;

public class Homescreen extends AppCompatActivity implements LocationListener {

    RecyclerView recyclerView;

    DBHelper db;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    double lat, lag;
    int userID =0;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    TextView noNearbyPolls;
    SharedPreferences sharedpreferences;


    ArrayList pollTitles = new ArrayList<>();
    ArrayList pollOwners = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        noNearbyPolls = (TextView) findViewById(R.id.noNearbyPolls);
        noNearbyPolls.setVisibility(View.GONE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userID = sharedpreferences.getInt("userID",0);
        System.out.println("user ID = "+userID);

        //checkLocationPermission();
       // setUserLocation();
        try {
            getPolls();
        } catch (IOException e) {
            e.printStackTrace();
            noNearbyPolls.setVisibility(View.VISIBLE);

        }
        try {
            setList();
        } catch (IOException e) {
            e.printStackTrace();
            noNearbyPolls.setVisibility(View.VISIBLE);
        }
        BottomNavigation();


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("Location permission message")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Homescreen.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                       // String provider = locationManager.getBestProvider(new Criteria(), false);

                        //Request location updates:
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }

    void setUserLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
        lag = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();

        System.out.println(" longitude IS "+lag);

        System.out.println("last known location: "+locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

    }

        void BottomNavigation(){

            BottomNavigationView nav = findViewById(R.id.bottom_navigation);

            nav.setSelectedItemId(R.id.home);

            nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()){
                        case R.id.home:
                            return true;
                        case R.id.Settings:
                            intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            //intent.putExtra("userID", userID);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.Polls:
                             intent = new Intent(getApplicationContext(), PollActivity.class);
                          //  intent.putExtra("userID", userID);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });

    }

        void setList() throws IOException {

        if (pollTitles.size()==0) //checking if there are no polls
            return;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Sending reference and data to Adapter
        Adapter adapter = new Adapter(Homescreen.this, pollTitles,pollOwners );

        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(Homescreen.this,"ok" , Toast.LENGTH_LONG).show();
                        TextView titleText  = (TextView) view.findViewById(R.id.pollTitle);
                        String title = titleText.getText().toString();

                        TextView ownerText  = (TextView) view.findViewById(R.id.pollOwner);
                        String owner = titleText.getText().toString();
                        System.out.println(owner);


                        int id = db.getPollId(title);
                        System.out.println(id);

                        System.out.println("poll title: "+title);


                        if (id!=-1){
                            Intent intent = new Intent(Homescreen.this, votePoll.class);

                            intent.putExtra("userID",userID);
                            intent.putExtra(POLL_ID,id);
                            startActivity(intent);

                        }
                        else
                        Toast.makeText(Homescreen.this,"Poll not available" , Toast.LENGTH_LONG).show();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));

         adapter.notifyDataSetChanged();


    }


        void getPolls() throws IOException {
            System.out.println("in getting polls");

            db = new DBHelper(this);


            ArrayList<HashMap<String, String>> polls = db.retrieveNearbyPolls(lat, lag);

            ArrayList<String> titles = new ArrayList<String>();
            ArrayList<String> owners = new ArrayList<String>();
            if (polls.size()==0) {
                noNearbyPolls.setVisibility(View.VISIBLE);
                return;
            }
            for (int i = 0; i < polls.size(); i++) {
                titles.add(polls.get(i).get(POLL_TITLE));
                owners.add(polls.get(i).get(POLL_OWNER));
            }

            pollTitles = titles;
            pollOwners = owners;

        }

    @Override
         public void onLocationChanged(@NonNull Location location) {

        lat = location.getLatitude();
    lag = location.getLongitude();

    System.out.println(lat+" latitude");
        System.out.println(lag+" longitude");


    }



}


