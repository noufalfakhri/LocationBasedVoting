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

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PollActivity extends AppCompatActivity {

    private TextView mTextView;
    Button buttonView;
    public static final int msg_request = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        mTextView = (TextView) findViewById(R.id.text);
        buttonView = (Button) findViewById(R.id.button);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Polls);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homescreen.class));
                        overridePendingTransition(0,0);
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
                Intent intent = new Intent (getApplicationContext() , AddPoll.class);
                // Bundle dataBundle = new Bundle();
                // dataBundle.putInt("id", 0);
                // intent.putExtras(dataBundle);
                startActivityForResult(intent, msg_request);

            }
        });
    }
}