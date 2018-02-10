package com.example.katri.lolvotingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class AdminMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);


        setUpAddUserButton();
        setUpRemoveUserButton();
        setUpAddVoteButton();
        setUpRemoveVoteButton();
        setUpPoolStatusButton();
        setUpFinishButton();

    }

    private void setUpAddUserButton() {

        ImageButton adm_btn = (ImageButton ) findViewById(R.id.btn_add_user);
        adm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMenu.this, "moved to Add User... ", Toast.LENGTH_SHORT).show();

                //lunch the admin menu
                Intent intent;
                intent = AddUser.makeIntent(AdminMenu.this);
                startActivity(intent);

            }

        });

    }

    private void setUpRemoveUserButton() {

        ImageButton  adm_btn = (ImageButton ) findViewById(R.id.btn_remove_user);
        adm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMenu.this, "moved to Remove User... ", Toast.LENGTH_SHORT).show();

                //lunch the admin menu
                Intent intent;
                intent = RemoveUser.makeIntent(AdminMenu.this);
                startActivity(intent);

            }

        });

    }

    private void setUpAddVoteButton() {

        ImageButton  adm_btn = (ImageButton ) findViewById(R.id.btn_Add_Vote);
        adm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMenu.this, "moved to Add vote... ", Toast.LENGTH_SHORT).show();

                //lunch the admin menu
                Intent intent;
                intent = AddVote.makeIntent(AdminMenu.this);
                startActivity(intent);

            }

        });

    }

    private void setUpRemoveVoteButton() {

        ImageButton  adm_btn = (ImageButton ) findViewById(R.id.btn_remove_vote);
        adm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminMenu.this, "moved to Remove vote... ", Toast.LENGTH_SHORT).show();

                //lunch the admin menu
                Intent intent;
                intent = RemoveVote.makeIntent(AdminMenu.this);
                startActivity(intent);

            }

        });

    }

    private void setUpPoolStatusButton() {



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

    public static Intent makeIntent(Context context) {
        return new Intent(context,AdminMenu.class);

    }


}
