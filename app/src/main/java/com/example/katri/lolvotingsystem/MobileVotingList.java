package com.example.katri.lolvotingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileVotingList extends AppCompatActivity implements AsyncResponse {
    User CurrentUser;
    public TextView data;
    private ListView lv;
    public ArrayList<HashMap<String, String>> voteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_voting_list);

        voteList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        CurrentUser = CurrentUser.getInstance();
        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Hello " + CurrentUser.GetUserName());

        VotingSync vs = new VotingSync(this,0);
        vs.delegate = this;
        vs.execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MobileVotingList.this, MobileCandidateList.class);
                intent.putExtra("VoteNum", voteList.get(i).get("id"));
                startActivity(intent);
                finish();
            }
        });

        setupAdminMenuButton();



    }

    private void setupAdminMenuButton() {

        Button adm_btn = (Button) findViewById(R.id.admin_menu_button);
        adm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MobileVotingList.this, "moved to Admin Menu... ", Toast.LENGTH_SHORT).show();

                //lunch the admin menu
                Intent intent;
                intent = AdminMenu.makeIntent(MobileVotingList.this);
                startActivity(intent);

            }

        });
    }



    @Override
    public void processFinish(ArrayList<HashMap<String, String>> voteList) {
        this.voteList = voteList;
    }
}
