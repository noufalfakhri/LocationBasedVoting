package com.example.location_basedvotingapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;

import static com.example.location_basedvotingapp.DBHelper.POLL_ANSWER1;
import static com.example.location_basedvotingapp.DBHelper.POLL_ANSWER2;
import static com.example.location_basedvotingapp.DBHelper.POLL_ID;
import static com.example.location_basedvotingapp.DBHelper.POLL_OWNER;
import static com.example.location_basedvotingapp.DBHelper.POLL_RESPONDER;
import static com.example.location_basedvotingapp.DBHelper.POLL_TITLE;
import static com.example.location_basedvotingapp.DBHelper.RESPONSE_POLLID;
import static com.example.location_basedvotingapp.DBHelper.TABLE_POLL;
import static com.example.location_basedvotingapp.DBHelper.TABLE_RESPONSE;
import static com.example.location_basedvotingapp.DBHelper.USER_ID;

public class votePoll extends AppCompatActivity {


    int pollID = 0 ;
    DBHelper db;
    int userID = 0;
    static TextView pollTitle;
    TextView screenTitle;
    TextView voteResult1;
    TextView voteResult2;
    RadioButton answer1;
    RadioButton answer2;
    RadioGroup radioGroup;
    HashMap<Integer,Integer> results;
    Button submit;

    private NotificationHelper mNotificationHelper;
    private Calendar calendar;

    ImageView delete;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_poll);

        db = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        userID = extras.getInt("userID");
        pollID = extras.getInt(POLL_ID);

        System.out.println(pollID +"< in create");
        pollTitle = (TextView) findViewById(R.id.pollQuestion);
        screenTitle = (TextView) findViewById(R.id.votePollTitle);

        answer1 = (RadioButton) findViewById(R.id.answer1);
        answer2 = (RadioButton) findViewById(R.id.answer2);
        submit = (Button) findViewById(R.id.submitButton) ;
        goBack = (ImageView) findViewById(R.id.backImage) ;
        delete = (ImageView)  findViewById(R.id.deleteView);
        radioGroup = (RadioGroup) findViewById(R.id.answerGroup);
        voteResult1 = (TextView) findViewById(R.id.voteResult1);
        voteResult2 = (TextView) findViewById(R.id.voteResult2);
        voteResult1.setVisibility(View.GONE);
        voteResult2.setVisibility(View.GONE);
        delete.setVisibility((View.GONE));

        mNotificationHelper = new NotificationHelper(this);
        calendar = Calendar.getInstance();
        System.out.println('1');
        sendOnChannel1();

        getPoll();
        hasAlreadyVoted();

        if ( hasAlreadyVoted()|| userIsOwner())
            showResults();



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answer = radioGroup.getCheckedRadioButtonId() == R.id.answer1? 1:2;
                db.createResponse(pollID,userID,answer);
                Toast.makeText(votePoll.this,"You have voted successfully!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deletePoll(pollID);
                finish();
            }
        });

    }

    private boolean userIsOwner() {
        boolean isOwner = false;

        SQLiteDatabase db_ = db.getWritableDatabase();
        Cursor res =  db_.rawQuery("  SELECT * FROM "+TABLE_POLL+" WHERE "+POLL_ID+" = "+pollID+" AND "+POLL_OWNER+" = "+userID,null);
        if (res != null && res.getCount() >0) {
            isOwner = true;
            delete.setVisibility(View.VISIBLE);
        }


        return isOwner;
    }

    private void getPoll() {
        System.out.println("poll id = "+pollID);
        SQLiteDatabase db_ = db.getWritableDatabase();
        Cursor res =  db_.rawQuery("  SELECT * FROM "+TABLE_POLL+" WHERE "+POLL_ID+" = "+pollID+"",null);

        if (res != null) {
            res.moveToFirst();

            String title = res.getString(res.getColumnIndex(POLL_TITLE));
            System.out.println("poll title = "+title);

            String A1 = res.getString(res.getColumnIndex(POLL_ANSWER1));
            String A2 = res.getString(res.getColumnIndex(POLL_ANSWER2));

            System.out.println(A1);
            System.out.println(A2);


            pollTitle.setText(title);
            answer1.setText(A1);
            answer2.setText(A2);
        }

    }

    public boolean hasAlreadyVoted(){
        boolean voted = false;

        SQLiteDatabase db_ = db.getWritableDatabase();
        Cursor res =  db_.rawQuery("  SELECT * FROM "+TABLE_RESPONSE+" WHERE "+RESPONSE_POLLID+" = "+pollID+" AND "+POLL_RESPONDER+" = "+userID+"",null);

        if (res.getCount() >0) {
            voted = true;
        }
        results = db.getPollResult(pollID);
        System.out.println(results);
        voteResult1.setText(answer1.getText().toString()+ ": "+results.get(1));
        voteResult2.setText(answer2.getText().toString()+ ": "+results.get(2));




        return voted;

    }

    private void showResults() {
        radioGroup.setVisibility(View.GONE);
        screenTitle.setText("Voting Result");
        submit.setVisibility(View.GONE);


        voteResult1.setVisibility(View.VISIBLE);
        voteResult2.setVisibility(View.VISIBLE);
        goBack.setVisibility(View.VISIBLE);
    }

    public void sendOnChannel1(){
        //Calendar c, int id
        System.out.println('2');
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this,NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1 ,intent1,0);

        System.out.println('3');
//
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            System.out.println('4');
        }
        System.out.println('5');
        System.out.println(calendar.getTimeInMillis());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,1000,pendingIntent);
        System.out.println('6');
    }

    public static String getTit(){

        return  pollTitle.getText().toString();
    }
}