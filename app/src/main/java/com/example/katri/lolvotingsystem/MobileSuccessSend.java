package com.example.katri.lolvotingsystem;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MobileSuccessSend extends AppCompatActivity {
    User CurrentUser;
    public TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_success_send);

        CurrentUser = CurrentUser.getInstance();
        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Hello " + CurrentUser.GetUserName());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void OtherVotings(final View arg0) {
        Intent intent = new Intent(MobileSuccessSend.this,MobileVotingList.class);
        this.finishAffinity();
        intent.putExtra("flag", "0");
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Logoff(final View arg0) {
        CurrentUser.Reset();
        Intent intent = new Intent(MobileSuccessSend.this,MobileLogin.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }
}
