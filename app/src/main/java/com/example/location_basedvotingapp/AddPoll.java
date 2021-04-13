package com.example.location_basedvotingapp;


import android.Manifest;
//import android.content.ContentValues;
//import android.content.Intent;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static com.example.location_basedvotingapp.MainActivity.MyPREFERENCES;


public class AddPoll extends AppCompatActivity {
    ImageView goBack;
    Button addButton;
    private TextView question;
    private TextView answer1;
    private TextView answer2;
    DBHelper db;

    private DatePicker datePicker;
    private TimePicker timePicker;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    double lat, lag;

    private TextView dateView;
    private TextView timeView;
    int userID = 0;

    private int year, month, day, hour, min;
    private Calendar calendar;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpoll);
        System.out.println("oncreate");
        Log.i("ssd", "oncreate");
        goBack = (ImageView) findViewById(R.id.backImage);
        addButton = (Button) findViewById(R.id.add);
        question = (TextView) findViewById(R.id.question);
        answer1 = (TextView) findViewById(R.id.answer1);
        answer2 = (TextView) findViewById(R.id.answer2);


        dateView = (TextView) findViewById(R.id.dateView);
        timeView = (TextView) findViewById(R.id.timeView);

        calendar = Calendar.getInstance();


        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
        showDate(year, month + 1, day);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userID = sharedpreferences.getInt("userID",0);
        System.out.println("user ID = "+userID);

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
        lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
        lag = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();

        //DBHelper dbHelper = new DBHelper(this);
        db = new DBHelper(this);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission


                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(db.POLL_TITLE, question.getText().toString());
                values.put(db.POLL_OWNER, userID);
                values.put(db.POLL_LAT, lat);
                values.put(db.POLL_LAG,lag);
                values.put(db.POLL_ANSWER1, answer1.getText().toString());
                values.put(db.POLL_ANSWER2, answer2.getText().toString());
                values.put(db.POLL_TIME, timeView.getText().toString());
                values.put(db.POLL_DATE, dateView.getText().toString());
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
                        System.out.println("TIME: " + cursorCourses.getString(7));
                        System.out.println("DATE: " + cursorCourses.getString(8));
                    }
                }

                finish();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //show date dialog
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });

        //show time dialog
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);


            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id ==0 ){
            return new TimePickerDialog(this,
                    timeListener, hour, min, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private TimePickerDialog.OnTimeSetListener timeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    showTime(hourOfDay,minute);

                }
            };





    public void showTime(int hour, int min) {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        timeView.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));


    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

    }




}
