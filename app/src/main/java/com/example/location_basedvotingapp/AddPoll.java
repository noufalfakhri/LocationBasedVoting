package com.example.location_basedvotingapp;



import android.Manifest;
//import android.content.ContentValues;
//import android.content.Intent;
import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission
                System.out.println("1");
                Log.i("ssd", "click");

                System.out.println("question:"+ question.getText().toString());
                System.out.println("Answer1:"+ answer1.getText().toString());
                System.out.println("Answer2:"+ answer2.getText().toString());

//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                ContentValues values = new ContentValues();
//                values.put(DBHelper.POLL_ID, 1);
//                values.put(DBHelper.POLL_TITLE, question.getText().toString());
//                values.put(DBHelper.POLL_OWNER, 1);
//                values.put(DBHelper.POLL_LAT, "-122.3234322");
//                values.put(DBHelper.POLL_LAG, "37.3234322");
//                values.put(DBHelper.POLL_ANSWER1, answer1.getText().toString());
//                values.put(DBHelper.POLL_ANSWER2, answer2.getText().toString());
//
//                long newRowId = db.insert(DBHelper.TABLE_POLL, null, values);

//                Cursor cursorCourses = db.rawQuery("SELECT * FROM " + "Poll", null);
//                System.out.println(cursorCourses.getColumnIndex("POLL_TITLE"));

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
