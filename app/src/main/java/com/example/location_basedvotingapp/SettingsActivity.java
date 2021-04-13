package com.example.location_basedvotingapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {
    Boolean isMuted =false;
    Button mute;
    Button signOut ;
    int userID=0;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        signOut = (Button) findViewById(R.id.SignOutbutton);
        mute = (Button) findViewById(R.id.mute);

        mute.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isMuted){
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    mute.setText("unmute");
                    isMuted=false;
                }
                else{
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
                    mute.setText("mute");
                    isMuted=true;
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mTextView = (TextView) findViewById(R.id.text);
        setNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigation();
    }

    private void setNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);

        nav.setSelectedItemId(R.id.Settings);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.home:
                        intent = new Intent(getApplicationContext(), Homescreen.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Settings:
                        return true;
                    case R.id.Polls:
                        intent = new Intent(getApplicationContext(), PollActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}