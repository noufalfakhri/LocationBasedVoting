package com.example.location_basedvotingapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LocationVoting.db";

    public static final String TABLE_USER = "Users";
    public static final String USER_ID = "ID";
    public static final String USER_USERNAME = "USERNAME";
    public static final String USER_PASSWORD = "PASSWORD";


    public static final String TABLE_POLL = "Poll";
    public static final String POLL_ID = "ID";
    public static final String POLL_TITLE = "TITLE";
    public static final String POLL_OWNER = "OWNER";
    public static final String POLL_LAT = "LAT";
    public static final String POLL_LAG = "LAG";

    public static final String TABLE_RESPONSE = "Response";
    public static final String RESPONSE_ID = "ID";
    public static final String RESPONSE_POLLID = "Poll";
    public static final String POLL_RESPONDER = "Responder";
    public static final String POLL_ANSWER = "Answer";


    public static final String TABLE_ANSWER = "Answer";
    public static final String ANSWER_ID = "ID";
    public static final String ANSWER_TITLE = "Title";
    public static final String ANSWER_POLL = "Poll";



    // create user table > id / username / password
    public static final String createUsersQuery =
            ("create table " + TABLE_USER+ "("+USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    USER_USERNAME +" TEXT," +
                    USER_PASSWORD+" TEXT)");

// create poll table > id / title / poll owner / poll location: lat/lag
    public static final String createPollQuery =
            ("create table " + TABLE_POLL+ "("+POLL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    POLL_TITLE +" TEXT," +
                    POLL_OWNER +" INTEGER,"+
                    POLL_LAT +" TEXT,"+
                    POLL_LAG +" TEXT,"
                    +" FOREIGN KEY ("+
                    POLL_OWNER+") REFERENCES "+TABLE_USER+" ("+USER_ID+"), )");


// create response table > id / reponse poll / responder / answer
    public static final String createResponseQuery =
            ("create table " + TABLE_RESPONSE+ "("+RESPONSE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RESPONSE_POLLID +" INTEGER," +
                    POLL_RESPONDER +" INTEGER,"+
                    POLL_ANSWER +" INTEGER,"
                    +" FOREIGN KEY ("+
                    POLL_RESPONDER+") REFERENCES "+TABLE_USER+" ("+USER_ID+"),"
                    +" FOREIGN KEY ("+
                    RESPONSE_POLLID+") REFERENCES "+TABLE_POLL+" ("+POLL_ID+"),"
                    +" FOREIGN KEY ("+
                    POLL_ANSWER+") REFERENCES "+TABLE_ANSWER+" ("+ANSWER_ID+"),)");


    // create answer table > id / title / poll
    public static final String createAnswerQuery =
            ("create table " + TABLE_ANSWER+ "("+ANSWER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ANSWER_TITLE +" TEXT," +
                    ANSWER_POLL +" INTEGER,"
                    +" FOREIGN KEY ("+
                    ANSWER_POLL+") REFERENCES "+TABLE_POLL+" ("+POLL_ID+"))");





    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    //Create tables
        db.execSQL(createUsersQuery);
        db.execSQL(createPollQuery);
        db.execSQL(createResponseQuery);
        db.execSQL(createAnswerQuery);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    public boolean registerUser (String Username, String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME, Username);
        contentValues.put(USER_PASSWORD, Password);


        long result = db.insert(TABLE_USER,null,contentValues);
        if(result == -1) return false;

        return true;
    }


    public boolean createPoll (String Title, int Owner , String lat , String lag ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POLL_TITLE, Title);
        contentValues.put(POLL_OWNER, Owner);
        contentValues.put(POLL_LAT, lat);
        contentValues.put(POLL_LAG, lag);

        long result = db.insert(TABLE_POLL,null,contentValues);
        if(result == -1) return false;

        return true;
    }


    public boolean createAnswer (String TextAnswer, int pollID  ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANSWER_TITLE, TextAnswer);
        contentValues.put(ANSWER_POLL, pollID);

        long result = db.insert(TABLE_ANSWER,null,contentValues);
        if(result == -1) return false;

        return true;
    }

    public boolean createResponse ( int pollID, int responderID, int answerID  ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESPONSE_POLLID, pollID);
        contentValues.put(POLL_RESPONDER, responderID);
        contentValues.put(POLL_ANSWER, answerID);


        long result = db.insert(TABLE_RESPONSE,null,contentValues);
        if(result == -1) return false;

        return true;
    }


    }


