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

public class RemoveVote extends AppCompatActivity {
    User CurrentUser;
    private EditText VoteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_vote);


        CurrentUser = CurrentUser.getInstance();
        VoteID = (EditText) findViewById(R.id.vote_num);

        setUpFinishButton();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context,RemoveVote.class);

    }


    public void doIt(final View arg)
    {
        if((VoteID.getText().toString()).equals("")) {
            Toast.makeText(getApplicationContext(), "Error: there is missing details!", Toast.LENGTH_LONG).show();
        }

        else {
            RemoveVoteSync rvs = new RemoveVoteSync(this);
            rvs.execute();

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
        Intent intent = new Intent(RemoveVote.this,MobileLogin.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }
}