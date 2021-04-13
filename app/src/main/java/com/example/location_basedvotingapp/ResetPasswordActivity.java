package com.example.location_basedvotingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordActivity extends AppCompatActivity {

    TextInputLayout text_input_layout_1, text_input_layout_2;
    TextInputEditText edit_email, edit_password;
    Button btn_reset;

    DBHelper dbHelper;

    Boolean HasUpperCase = false, HasNumber = false;
    ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        dbHelper = new DBHelper(ResetPasswordActivity.this);
        goBack = (ImageView) findViewById(R.id.backImage) ;
        text_input_layout_1 = (TextInputLayout) findViewById(R.id.text_input_layout_1);
        text_input_layout_2 = (TextInputLayout) findViewById(R.id.text_input_layout_2);
        edit_email = (TextInputEditText) findViewById(R.id.edit_email);
        edit_password = (TextInputEditText) findViewById(R.id.edit_password);
        btn_reset = (Button) findViewById(R.id.btn_reset);


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_email.getText().length() == 0) {
                    text_input_layout_1.setHelperText("* Enter Your Email");
                    return;
                }
                if (!edit_email.getText().toString().contains("@gmail.com") && !edit_email.getText().toString().contains("@hotmail.com") && !edit_email.getText().toString().contains("@yahoo.com")) {
                    text_input_layout_1.setHelperText("* Enter Your Email Correctly");
                    return;
                }
                text_input_layout_1.setHelperText("");
                if (edit_password.getText().length() < 8) {
                    text_input_layout_2.setHelperText("* Password must contain at least 8 letters, numbers and an uppercase letter");
                    return;
                }
                for (int x = 0; x < edit_password.getText().length(); x++) {
                    if (Character.isUpperCase(edit_password.getText().charAt(x))) {
                        HasUpperCase = true;
                        break;
                    }
                }
                for (int x = 0; x < edit_password.getText().length(); x++) {
                    if (Character.isDigit(edit_password.getText().charAt(x))) {
                        HasNumber = true;
                        break;
                    }
                }
                if (!HasUpperCase) {
                    text_input_layout_2.setHelperText("* Password must contain at least 8 letters, numbers and an uppercase letter");
                    return;
                }
                if (!HasNumber) {
                    text_input_layout_2.setHelperText("* Password must contain at least 8 letters, numbers and an uppercase letter");
                    return;
                }
                text_input_layout_2.setHelperText("");
                if (dbHelper.isEmailFound(edit_email.getText().toString())) {
                    dbHelper.resetPassword(edit_email.getText().toString(), edit_password.getText().toString());
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "This Account Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}