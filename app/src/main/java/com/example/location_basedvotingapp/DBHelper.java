package com.example.location_basedvotingapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.location.Address;

import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LocationVoting.db";

    public static final String TABLE_USER = "Users";
    public static final String USER_ID = "ID";
    public static final String USER_USERNAME = "USERNAME";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PASSWORD = "PASSWORD";


    public static final String TABLE_POLL = "Poll";
    public static final String POLL_ID = "ID";
    public static final String POLL_TITLE = "TITLE";
    public static final String POLL_OWNER = "OWNER";
    public static final String POLL_LAT = "LAT";
    public static final String POLL_LAG = "LAG";
    public static final String POLL_ANSWER1 = "ANSWER1";
    public static final String POLL_ANSWER2 = "ANSWER2";
    public static final String POLL_TIME = "TIME";
    public static final String POLL_DATE = "DATE";


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
            ("create table " + TABLE_USER + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_USERNAME + " TEXT," + USER_EMAIL + " TEXT," + USER_PASSWORD + " TEXT)");


    // create poll table > id / title / poll owner / poll location: lat/lag
    public static final String createPollQuery =
            ("create table " + TABLE_POLL+ "("+POLL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    POLL_TITLE +" TEXT," +
                    POLL_OWNER +" INTEGER,"+
                    POLL_LAT +" REAL,"+
                    POLL_LAG +" REAL,"+
                    POLL_ANSWER1 +" TEXT,"+
                    POLL_ANSWER2 +" TEXT,"+
                    POLL_TIME +" TEXT,"+
                    POLL_DATE +" TEXT,"
                    +" FOREIGN KEY ("+
                    POLL_OWNER+") REFERENCES "+TABLE_USER+" ("+USER_ID+") )");


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
                    POLL_ANSWER+") REFERENCES "+TABLE_ANSWER+" ("+ANSWER_ID+"))");


    // create answer table > id / title / poll
    public static final String createAnswerQuery =
            ("create table " + TABLE_ANSWER+ "("+ANSWER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ANSWER_TITLE +" TEXT," +
                    ANSWER_POLL +" INTEGER,"
                    +" FOREIGN KEY ("+
                    ANSWER_POLL+") REFERENCES "+TABLE_POLL+" ("+POLL_ID+"))");




 Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    //Create tables
        db.execSQL(createUsersQuery);
        db.execSQL(createPollQuery);
        db.execSQL(createResponseQuery);
        db.execSQL(createAnswerQuery);
        System.out.println("in creating tables");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    public boolean registerUser(String Username, String Email, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_USERNAME, Username);
        contentValues.put(USER_EMAIL, Email);
        contentValues.put(USER_PASSWORD, Password);



        long result = db.insert(TABLE_USER,null,contentValues);
        if(result == -1) return false;

        return true;
    }

    public boolean isEmailFound(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + USER_EMAIL + " from " + TABLE_USER, null);
        String existEmail;
        if (cursor.moveToFirst()) {
            do {
                existEmail = cursor.getString(0);
                if (existEmail.equals(Email)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

    public void resetPassword(String Email, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_USER + " SET " + USER_PASSWORD + " = ?" + " WHERE " + USER_EMAIL + " = ?", new Object[]{Password, Email});
        db.close();
    }

    public int checkEmailAndPassword(String Email, String Password) {
        SQLiteDatabase database = this.getReadableDatabase();
        int result = -1;
        Cursor cursor = database.rawQuery("select * from " + TABLE_USER + " where " + USER_EMAIL + "=?" + " and " + USER_PASSWORD + "=?", new String[]{Email, Password});
        cursor.moveToFirst();
        result = cursor.getInt(cursor.getColumnIndex(USER_ID));
        System.out.println("User ID = "+result);

        return  result;
    }








//
    public boolean createPoll (String Title, int Owner , double lat , double lag ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POLL_TITLE, Title);
        contentValues.put(POLL_OWNER, Owner);
        contentValues.put(POLL_LAT, lat);
        contentValues.put(POLL_LAG, lag);
        System.out.println("in creating polls");


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


    //This method returns only polls that are nearby and unvoted by user

    public ArrayList<HashMap<String,String>> retrieveNearbyPolls(double lat , double lag ){

        ArrayList<HashMap<String,String>> polls = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(" SELECT * FROM "+TABLE_POLL+" ORDER BY ABS(("+lat+"-"+POLL_LAT+")*("+lat+"-"+POLL_LAT+")) + ABS(("+lag+"-"+POLL_LAG+")*("+lag+"-"+POLL_LAG+")) ASC",null);


        System.out.println("in nearby polls");
        res.moveToFirst();
        while (res.isAfterLast()==false){

           // if (res.getInt(res.getColumnIndex(POLL_RESPONDER)) != user)  // user has already voted for this poll

                if(res.getInt(res.getColumnIndex(POLL_ID)) != -1) {// if poll exists


                    HashMap< String, String> poll = new HashMap<>();
                    String title = res.getString(res.getColumnIndex(POLL_TITLE));
                    int owner = res.getInt(res.getColumnIndex(POLL_OWNER));

                    poll.put(POLL_TITLE, title);
                    poll.put(POLL_OWNER, getPollOwner(owner));


                    polls.add(poll);

            }
            res.moveToNext();
        }
        return polls;
    }



    public String getPollOwner(int user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(" SELECT "+USER_USERNAME+" FROM "+TABLE_USER+" WHERE "+USER_ID+"="+user,null);
        res.moveToFirst();

        String username = res.getString(res.getColumnIndex(USER_USERNAME));
        return username;

    }
    public int numberOfPollRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_POLL);
        return numRows;
    }



    public ArrayList<HashMap<String,String>> retrieveUserPolls(int userID ) throws IOException {

        ArrayList<HashMap<String,String>> polls = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery(" SELECT * FROM "+TABLE_POLL+" WHERE "+POLL_OWNER+" = "+userID+"",null);


        System.out.println("in users own polls");
        res.moveToFirst();
        while (res.isAfterLast()==false){

            // if (res.getInt(res.getColumnIndex(POLL_RESPONDER)) != user)  // user has already voted for this poll

            if(res.getString(res.getColumnIndex(POLL_ID)) != null) {// if poll exists


                HashMap< String, String> poll = new HashMap<>();
                String title = res.getString(res.getColumnIndex(POLL_TITLE));
                String owner = res.getString(res.getColumnIndex(POLL_OWNER));

                double lat = res.getDouble(res.getColumnIndex(POLL_LAT));
                double lag = res.getDouble(res.getColumnIndex(POLL_LAG));

                String location = retrieveAddressLine(lat/100000,lag);
                poll.put(POLL_TITLE, title);

                poll.put(POLL_OWNER, " ");


                polls.add(poll);

            }
            res.moveToNext();
        }
        return polls;
    }

    public String retrieveAddressLine( double lat, double lag ) throws IOException {

        //get poll coordinates

        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList = myLocation.getFromLocation(lat,lag, 1);
        String addressStr = "";
        try{
        Address address = (Address) myList.get(0);

        addressStr += address.getSubLocality() ;
    }catch (IndexOutOfBoundsException e){
        System.out.println(e);
    }


        return addressStr;
    }


    public int getPollId(String title){
        int id = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("  SELECT * FROM "+TABLE_POLL+" WHERE "+POLL_TITLE+" = '"+title+"'",null);

        res.moveToFirst();

        if (res.getCount() > 0 )
            id = res.getInt(res.getColumnIndex(POLL_ID));
        System.out.println("Poll ID: "+id);
        return id;
    }

    public HashMap<Integer,Integer> getPollResult (int pollID ){

        HashMap<Integer,Integer> results = new  HashMap<Integer,Integer>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("  SELECT "+POLL_ANSWER+" , COUNT("+POLL_ANSWER+") FROM "+TABLE_RESPONSE+" WHERE "+RESPONSE_POLLID+" = '"+pollID+"' GROUP BY "+POLL_ANSWER+" ORDER BY COUNT ("+POLL_ANSWER+") DESC",null);
        res.moveToFirst();



        int answer1Result = 0;
        int answer2Result = 0;

        while(res.isAfterLast()==false) {
            if (res.getInt(res.getColumnIndex(POLL_ANSWER)) == 1)
                answer1Result = res.getInt(res.getColumnIndex("COUNT(" + POLL_ANSWER + ")"));
            else
                answer2Result = res.getInt(res.getColumnIndex("COUNT(" + POLL_ANSWER + ")"));
            res.moveToNext();
        }

        System.out.println("answer1 = "+ answer1Result);
        System.out.println("answer2 = "+ answer2Result);

        results.put(1,answer1Result);
        results.put(2,answer2Result);

        return results;
    }

    public Integer deletePoll (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_POLL, POLL_ID+ " = " +id,null);
    }


}


