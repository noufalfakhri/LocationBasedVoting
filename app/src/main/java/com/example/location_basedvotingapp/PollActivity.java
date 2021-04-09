package com.example.location_basedvotingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.example.location_basedvotingapp.DBHelper.POLL_OWNER;
import static com.example.location_basedvotingapp.DBHelper.POLL_TITLE;

public class PollActivity extends AppCompatActivity {

    private TextView mTextView;
    Button buttonView;

    RecyclerView recyclerView;
    DBHelper db;

    ArrayList<String> titles;
    ArrayList<String> owners;


    ArrayList pollTitles = new ArrayList<>(Arrays.asList("Poll 1", "Poll 2", "Poll3", "Poll 4", "Poll 5",
            "Poll 6 ", "Poll 7 ", "Poll 8"));
    ArrayList pollOwners = new ArrayList<>(Arrays.asList("Nouf", "Hessa", "Hessa", "Reema", "Ghada",
            "Hessa", "Reem", "Hessa"));

    public static final int msg_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        mTextView = (TextView) findViewById(R.id.text);
        buttonView = (Button) findViewById(R.id.button);
        titles = new ArrayList<String>();
        owners = new ArrayList<String>();
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Polls);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homescreen.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Polls:
                        return true;
                }
                return false;
            }
        });
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ssd", "onClick: Add poll");
                Intent intent = new Intent(getApplicationContext(), AddPoll.class);
                // Bundle dataBundle = new Bundle();
                // dataBundle.putInt("id", 0);
                // intent.putExtras(dataBundle);
                startActivityForResult(intent, msg_request);

            }
        });

        // getPolls();
        // setList();
    }

    void getPolls() {
        System.out.println("in getting polls");

        db = new DBHelper(this);


        ArrayList<HashMap<String, String>> polls = db.retrieveNearbyPolls(-122.3234322, 37.3234322, 1);


        for (int i = 0; i < polls.size(); i++) {
            titles.add(polls.get(i).get(POLL_TITLE));
            owners.add(polls.get(i).get(POLL_OWNER));
        }

//            pollTitles = titles;
//            pollOwners = owners;

    }

    void setList() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        System.out.println("linearLayoutManager");
        System.out.println(linearLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Sending reference and data to Adapter
        Adapter adapter = new Adapter(PollActivity.this, pollTitles, pollOwners);

        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
    }
}

