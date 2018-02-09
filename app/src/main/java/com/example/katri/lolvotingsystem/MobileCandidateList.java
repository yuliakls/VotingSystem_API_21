package com.example.katri.lolvotingsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileCandidateList extends AppCompatActivity implements AsyncResponse {

    private ProgressDialog pDialog;//salilili
    private ListView lv;
    String User;
    String ExistingVote;
    ArrayList<HashMap<String, String>> candidateList;
    public TextView data;
    User CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_candidate_list);

        Intent intent = getIntent();
        CurrentUser = CurrentUser.getInstance();
        ExistingVote = intent.getStringExtra("VoteNum");

        candidateList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Hello " + CurrentUser.GetUserName());

        CandidateSync cs = new CandidateSync(this, ExistingVote);
        cs.delegate = this;
        cs.execute();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Yuppppp ",Toast.LENGTH_LONG).show();
                /*
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure?")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"Yuppppp ",Toast.LENGTH_LONG).show();
                            }
                        }).create().show();
                //new SendRequest().execute();
                */
            }
        });
    }

    @Override
    public void processFinish(ArrayList<HashMap<String, String>> candidateList) {
        this.candidateList = candidateList;
    }
}
