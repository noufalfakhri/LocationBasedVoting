package com.example.location_basedvotingapp;



import android.Manifest;
//import android.content.ContentValues;
//import android.content.Intent;
import android.content.ContentValues;
import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class AddPoll extends AppCompatActivity {
    Button backButton;
    Button addButton;
    private TextView question;
    private TextView answer1;
    private TextView answer2;

    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpoll);
        System.out.println("oncreate");
        Log.i("ssd", "oncreate");
        backButton = (Button) findViewById(R.id.back);
        addButton = (Button) findViewById(R.id.add);
        question = (TextView) findViewById(R.id.question);
        answer1 = (TextView) findViewById(R.id.answer1);
        answer2 = (TextView) findViewById(R.id.answer2);


        //DBHelper dbHelper = new DBHelper(this);
        db = new DBHelper(this);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission

//                System.out.println("question:"+ question.getText().toString());
//                System.out.println("Answer1:"+ answer1.getText().toString());
//                System.out.println("Answer2:"+ answer2.getText().toString());



                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(db.POLL_ID, 3);
                values.put(db.POLL_TITLE, question.getText().toString());
                values.put(db.POLL_OWNER, 1);
                values.put(db.POLL_LAT, "-122.3234322");
                values.put(db.POLL_LAG, "37.3234322");
                values.put(db.POLL_ANSWER1, answer1.getText().toString());
                values.put(db.POLL_ANSWER2, answer2.getText().toString());

                long newRowId = database.insert(DBHelper.TABLE_POLL, null, values);

                Cursor cursorCourses = database.rawQuery("SELECT * FROM " + "Poll", null);
                System.out.println("here");
               // System.out.println(cursorCourses.getString(5));

                if (cursorCourses.getCount()==0){
                    System.out.println("NO DATA");
                } else {
                    while (cursorCourses.moveToNext()){
                        System.out.println("Question: " + cursorCourses.getString(1));
                        System.out.println("lat: " + cursorCourses.getString(3));
                        System.out.println("long: " + cursorCourses.getString(4));
                        System.out.println("answer1: " + cursorCourses.getString(5));
                        System.out.println("answer2: " + cursorCourses.getString(6));
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
