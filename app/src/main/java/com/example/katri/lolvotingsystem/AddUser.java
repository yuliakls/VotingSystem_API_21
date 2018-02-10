package com.example.katri.lolvotingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddUser extends AppCompatActivity {

//
//    private EditText UserID;
//    private EditText Name;
//    private EditText Email;
//    private EditText Password;
//    private boolean Admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        //setUpAddButton();
        setUpFinishButton();

    }


    public void doIt(final View arg)
    {
        AddUserSync aus = new AddUserSync( this);
        aus.execute();
    }


//    private void setUpAddButton() {
//
//        Button add_btn = (Button) findViewById(R.id.add_btn);
//
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//    }

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
}
