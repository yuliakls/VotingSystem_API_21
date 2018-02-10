package com.example.katri.lolvotingsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileCandidateList extends AppCompatActivity implements AsyncResponse {

    private ProgressDialog pDialog;
    private ListView lv;
    String User;
    String ExistingVote;
    String CandidateID;
    String CandidateName;
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CandidateID = candidateList.get(i).get("id");
                CandidateName = candidateList.get(i).get("name");
                //Toast.makeText(getApplicationContext()," "+ CandidateID,Toast.LENGTH_LONG).show();
                builder

                        .setMessage(Html.fromHtml("<font color='#e4e5e7'> You selected: "+ CandidateName +". Are you sure? </font>"))
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SendBalot(0);
                            }
                        }).create().show();
            }
        });
    }

    @Override
    public void processFinish(ArrayList<HashMap<String, String>> candidateList) {
        this.candidateList = candidateList;
    }

    private void SendBalot(int flag){
        if(flag == 0){
            SendBalotSync balot = new SendBalotSync(this, flag ,ExistingVote ,CandidateID);
            balot.execute();
        }
    }

}
