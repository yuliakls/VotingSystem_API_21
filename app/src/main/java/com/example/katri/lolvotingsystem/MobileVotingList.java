package com.example.katri.lolvotingsystem;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileVotingList extends AppCompatActivity implements AsyncResponse {
    User CurrentUser;
    public TextView data;
    private ListView lv;
    public ArrayList<HashMap<String, String>> voteList;
    Button menu;
    int flag;
    String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_voting_list);

        Intent intent = getIntent();
        admin = intent.getStringExtra("flag");

        CurrentUser = CurrentUser.getInstance();

        menu = (Button) findViewById(R.id.Menu);
        if(CurrentUser.GerStatus() == true && admin.equals("0")){
            menu.setVisibility(View.VISIBLE);
        }

        voteList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Hello " + CurrentUser.GetUserName());

        VotingSync vs;
        if(admin.equals("0")){
            vs = new VotingSync(this,0);
        }
        else{
            vs = new VotingSync(this,1);
        }
        vs.delegate = this;
        vs.execute();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(flag == 0){
                    Intent intent = new Intent(MobileVotingList.this, MobileCandidateList.class);
                    intent.putExtra("VoteNum", voteList.get(i).get("id"));
                    startActivity(intent);
                    //finish();
                }
                else {
//                    Intent intent = new Intent(MobileVotingList.this, MobileResults.class);
//                    intent.putExtra("VoteNum", voteList.get(i).get("id"));
//                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void processFinish(ArrayList<HashMap<String, String>> voteList,int flag) {
        this.voteList = voteList;
        this.flag = flag;
    }

    public void MenuBar(final View arg0) {
        Intent intent = new Intent(MobileVotingList.this, AdminMenu.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void LogOff(final View arg0) {
        CurrentUser.Reset();
        Intent intent = new Intent(MobileVotingList.this,MobileLogin.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }

}
