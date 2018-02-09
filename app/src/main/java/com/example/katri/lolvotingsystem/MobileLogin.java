package com.example.katri.lolvotingsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MobileLogin extends AppCompatActivity {

    private EditText UserID;
    private EditText Password;
    private CheckBox Terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_login);

        UserID = (EditText) findViewById(R.id.UserID);
        Password = (EditText) findViewById(R.id.Password);
        Terms = (CheckBox) findViewById(R.id.Terms_checkbox);

    }

    public void checkLogin(final View arg0) {
        if((UserID.getText().toString()).equals("") || (Password.getText().toString()).equals("") || !Terms.isChecked()){
            Toast.makeText(getApplicationContext(), "Error: there is missing details!", Toast.LENGTH_LONG).show();
        }
        else {

            UserSync us = new UserSync(this);
            us.execute();
        }
    }
}
