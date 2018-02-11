package com.example.katri.lolvotingsystem;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddUser extends AppCompatActivity {

    User CurrentUser;
    private EditText UserID;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private boolean Admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        setUpFinishButton();


        CurrentUser = CurrentUser.getInstance();
        UserID = (EditText) findViewById(R.id.UserID);
        Password = (EditText) findViewById(R.id.Password);
        Name = (EditText) findViewById(R.id.userName);
        Email = (EditText) findViewById(R.id.UserEmail);

    }


    public void doIt(final View arg)
    {
        if((UserID.getText().toString()).equals("") || (Password.getText().toString()).equals("") || (Name.getText().toString()).equals("") || (Email.getText().toString()).equals("")) {
            Toast.makeText(getApplicationContext(), "Error: there is missing details!", Toast.LENGTH_LONG).show();
        }

        else {
            AddUserSync aus = new AddUserSync(this);
            aus.execute();

        }
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context,AddUser.class);

    }


    private void setUpFinishButton() {

        Button finish_btn = (Button) findViewById(R.id.finish_btn);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void LogOff(final View arg0) {
        CurrentUser.Reset();
        Intent intent = new Intent(AddUser.this,MobileLogin.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }
}