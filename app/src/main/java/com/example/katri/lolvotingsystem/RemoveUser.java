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
import android.widget.EditText;
import android.widget.Toast;

public class RemoveUser extends AppCompatActivity {

    User CurrentUser;
    private EditText UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        CurrentUser = CurrentUser.getInstance();

        setUpFinishButton();
        UserID = (EditText) findViewById(R.id.UserID);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context,RemoveUser.class);
    }


    public void doIt(final View arg)
    {
        if((UserID.getText().toString()).equals("")) {
            Toast.makeText(getApplicationContext(), "Error: there is missing details!", Toast.LENGTH_LONG).show();
        }

        else {
            RemoveUserSync rus = new RemoveUserSync(this);
            rus.execute();

            // Clear
            //this.UserID.getText().clear();
        }
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
        Intent intent = new Intent(RemoveUser.this,MobileLogin.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }

}