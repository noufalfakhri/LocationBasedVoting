package com.example.location_basedvotingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    TextInputLayout text_input_layout_1, text_input_layout_2;
    TextInputEditText edit_email, edit_password;
    Button btn_login;
    TextView txt_2, txt_3;

    DBHelper dbHelper;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(MainActivity.this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        text_input_layout_1 = (TextInputLayout) findViewById(R.id.text_input_layout_1);
        text_input_layout_2 = (TextInputLayout) findViewById(R.id.text_input_layout_2);
        edit_email = (TextInputEditText) findViewById(R.id.edit_email);
        edit_password = (TextInputEditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        txt_2 = (TextView) findViewById(R.id.txt_2);
        txt_3 = (TextView) findViewById(R.id.txt_3);

        txt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
            }
        });

        txt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_email.getText().length() == 0) {
                    text_input_layout_1.setHelperText("* Enter Your Email");
                    return;
                }
                text_input_layout_1.setHelperText("");
                if (!edit_email.getText().toString().contains("@gmail.com") && !edit_email.getText().toString().contains("@hotmail.com") && !edit_email.getText().toString().contains("@yahoo.com")) {
                    text_input_layout_1.setHelperText("* Enter Your Email Correctly");
                    return;
                }
                text_input_layout_1.setHelperText("");
                if (edit_password.getText().length() == 0) {
                    text_input_layout_2.setHelperText("* Enter Your Password");
                    return;
                }
                text_input_layout_2.setHelperText("");
                int userID = dbHelper.checkEmailAndPassword(edit_email.getText().toString(), edit_password.getText().toString());
                if (userID != -1) {
                    Intent intent = new Intent(MainActivity.this, Homescreen.class);
                  //  intent.putExtra("userID", userID);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("userID", userID);
                    editor.commit();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Email Or Password Is Wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}