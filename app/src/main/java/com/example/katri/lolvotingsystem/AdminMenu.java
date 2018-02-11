package com.example.katri.lolvotingsystem;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;

public class AdminMenu extends AppCompatActivity {


    User CurrentUser;
    public TextView data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        CurrentUser = CurrentUser.getInstance();
        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Hello " + CurrentUser.GetUserName());
    }

    public void ViewResults(final View arg0) {
        Intent intent = new Intent(AdminMenu.this,MobileVotingList.class);
        intent.putExtra("flag", "1");
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void LogOff(final View arg0) {
        CurrentUser.Reset();
        Intent intent = new Intent(AdminMenu.this,MobileLogin.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }

}
