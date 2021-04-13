package com.example.location_basedvotingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.example.location_basedvotingapp.DBHelper.POLL_ID;
import static com.example.location_basedvotingapp.DBHelper.POLL_OWNER;
import static com.example.location_basedvotingapp.DBHelper.POLL_TITLE;
import static com.example.location_basedvotingapp.DBHelper.USER_ID;
import static com.example.location_basedvotingapp.MainActivity.MyPREFERENCES;

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

    int userID = 0;
    SharedPreferences sharedpreferences;
    public static final int msg_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);


        mTextView = (TextView) findViewById(R.id.text);
        buttonView = (Button) findViewById(R.id.button);
        titles = new ArrayList<String>();
        owners = new ArrayList<String>();
        setNavigation();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userID = sharedpreferences.getInt("userID",0);
        System.out.println("user ID = "+userID);
        System.out.println("on start user ID "+ userID);

        try {
            getPolls();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            setList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ssd", "onClick: Add poll");

                Intent intent = new Intent (getApplicationContext() , AddPoll.class);
                intent.putExtra("userID", userID);

                startActivityForResult(intent, msg_request);
                finish();

            }
        });


    }



    private void setNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.Polls);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch (item.getItemId()){
                    case R.id.home:
                        intent = new Intent(getApplicationContext(), Homescreen.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.Settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Polls:
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
//        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        userID = sharedpreferences.getInt("userID",0);
//        System.out.println("user ID = "+userID);
//        System.out.println("on start user ID "+ userID);
//
//        try {
//            getPolls();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            setList();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    void getPolls() throws IOException {
        System.out.println("in getting polls of users id = "+userID);

        db = new DBHelper(this);


        ArrayList<HashMap<String, String>> polls = db.retrieveUserPolls(userID);


        for (int i = 0; i < polls.size(); i++) {
            titles.add(polls.get(i).get(POLL_TITLE));
            owners.add(polls.get(i).get(POLL_OWNER));
        }

            pollTitles = titles;
            pollOwners = owners;

    }

    void setList() throws IOException {
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
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(PollActivity.this,"ok" , Toast.LENGTH_LONG).show();
                        TextView titleText  = (TextView) view.findViewById(R.id.pollTitle);
                        String title = titleText.getText().toString();

                        TextView ownerText  = (TextView) view.findViewById(R.id.pollOwner);
                        String owner = titleText.getText().toString();
                        System.out.println(owner);


                        int id = db.getPollId(title);



                        if (id!=-1){
                            Intent intent = new Intent(PollActivity.this, votePoll.class);
                            intent.putExtra("userID",userID);
                            intent.putExtra(POLL_ID,id);
                            startActivity(intent);

                        }
                        else
                            Toast.makeText(PollActivity.this,"Poll not available" , Toast.LENGTH_LONG).show();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));

        adapter.notifyDataSetChanged();
    }
}

