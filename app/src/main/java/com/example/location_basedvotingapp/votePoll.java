package com.example.location_basedvotingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class votePoll extends AppCompatActivity {


    int pollID = 0 ;
    int userID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_poll);

        Bundle extras = getIntent().getExtras();
        userID = extras.getInt("userID");
    }
}