package com.example.location_basedvotingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    TextInputLayout text_input_layout_1, text_input_layout_2, text_input_layout_3;
    TextInputEditText edit_name, edit_email, edit_password;
    Button btn_register;
    TextView txt_2;

    Boolean HasUpperCase = false, HasNumber = false;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        dbHelper = new DBHelper(Register.this);

        text_input_layout_1 = (TextInputLayout) findViewById(R.id.text_input_layout_1);
        text_input_layout_2 = (TextInputLayout) findViewById(R.id.text_input_layout_2);
        text_input_layout_3 = (TextInputLayout) findViewById(R.id.text_input_layout_3);
        edit_name = (TextInputEditText) findViewById(R.id.edit_name);
        edit_email = (TextInputEditText) findViewById(R.id.edit_email);
        edit_password = (TextInputEditText) findViewById(R.id.edit_password);
        btn_register = (Button) findViewById(R.id.btn_register);
        txt_2 = (TextView) findViewById(R.id.txt_2);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_name.getText().length() == 0) {
                    text_input_layout_1.setHelperText("* Enter Your Name");
                    return;
                }
                if (edit_name.getText().length() < 2) {
                    text_input_layout_1.setHelperText("* Your Name Must Have 2 Letters At Least");
                    return;
                }
                text_input_layout_1.setHelperText("");
                if (edit_email.getText().length() == 0) {
                    text_input_layout_2.setHelperText("* Enter Your Email");
                    return;
                }
                if (edit_email.getText().toString().equals("@gmail.com") || edit_email.getText().toString().equals("@hotmail.com") || edit_email.getText().toString().equals("@yahoo.com")) {
                    text_input_layout_2.setHelperText("* Enter Your Email Correctly");
                    return;
                }
                if (!edit_email.getText().toString().contains("@gmail.com") && !edit_email.getText().toString().contains("@hotmail.com") && !edit_email.getText().toString().contains("@yahoo.com")) {
                    text_input_layout_2.setHelperText("* Enter Your Email Correctly");
                    return;
                }
                text_input_layout_2.setHelperText("");
                if (edit_password.getText().length() < 8) {
                    text_input_layout_3.setHelperText("* Password must contain at least 8 letters, numbers and an uppercase letter");
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
                    text_input_layout_3.setHelperText("* Password must contain at least 8 letters, numbers and an uppercase letter");
                    return;
                }
                if (!HasNumber) {
                    text_input_layout_3.setHelperText("* Password must contain at least 8 letters, numbers and an uppercase letter");
                    return;
                }
                text_input_layout_3.setHelperText("");
                if (!dbHelper.isEmailFound(edit_email.getText().toString())) {
                    dbHelper.registerUser(edit_name.getText().toString(), edit_email.getText().toString(), edit_password.getText().toString());
                    Toast.makeText(Register.this, "طھظ… طھط³ط¬ظٹظ„ ط§ظ„ط­ط³ط§ط¨ ط¨ظ†ط¬ط§ط­", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, Homescreen.class));
                    finish();
                } else {
                    Toast.makeText(Register.this, "ظ‡ط°ط§ ط§ظ„ط­ط³ط§ط¨ ظ…ط³ط¬ظ„ ظ…ط³ط¨ظ‚ط§ظ‹", Toast.LENGTH_LONG).show();
                }
            }
        });

        txt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Homescreen.class));
                finish();
            }
        });

    }
}
