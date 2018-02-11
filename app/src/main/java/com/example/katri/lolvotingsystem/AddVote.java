package com.example.katri.lolvotingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class AddVote extends AppCompatActivity {

    private EditText VoteName;
    private EditText VoteDescription;
    private EditText Start;
    private EditText Finish;

    private EditText editText;
    Button addButton;
    private ListView listView;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;


    public static Date Parse( String input ) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = formatter.parse(input.replaceAll("Z$", "+0000"));

        return date;
    }

    public static String toString( Date date ) {

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        df.setTimeZone( tz );
        String output = df.format( date );

        int inset0 = 9;
        int inset1 = 6;

        String s0 = output.substring( 0, output.length() - inset0 );
        String s1 = output.substring( output.length() - inset1, output.length() );
        String result = s0 + s1;
        result = result.replaceAll( "UTC", "+00:00" );
        return result;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vote);

        setUpFinishButton();

        editText = (EditText) findViewById(R.id.editText);
        addButton = (Button) findViewById(R.id.addItem);
        listView = (ListView) findViewById(R.id.list);
        listItems = new ArrayList<String>();

        VoteName = (EditText) findViewById(R.id.VoteName);
        VoteDescription = (EditText) findViewById(R.id.VoteDescription);
        Start = (EditText) findViewById(R.id.start);
        Finish = (EditText) findViewById(R.id.finish);


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                listItems.add(editText.getText().toString());
                Toast.makeText(AddVote.this, "Good", Toast.LENGTH_LONG).show();
                editText.getText().clear();
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                Toast.makeText(AddVote.this, "Good", Toast.LENGTH_LONG).show();
            }
        });
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, AddVote.class);
    }

    public void doIt(final View arg)
    {
        Pattern p = Pattern.compile("\\d{42}-\\d{2}-\\d{2}");

        if((VoteName.getText().toString()).equals("") || (VoteDescription.getText().toString()).equals("") || (Start.getText().toString()).equals("") || (Finish.getText().toString()).equals("") ) {
            Toast.makeText(getApplicationContext(), "Error: there is missing details!", Toast.LENGTH_LONG).show();
        }
        else if(listItems.size() < 2 ) {
            Toast.makeText(getApplicationContext(), "You Need To enter at Least 2 candidates!", Toast.LENGTH_LONG).show();
        }
//        else if(!isValidDate(Start.toString())) {
//            Toast.makeText(getApplicationContext(), "The Start Date Is Not Valid", Toast.LENGTH_LONG).show();
//        }
//
//        else if(!isValidDate(Finish.toString())) {
//            Toast.makeText(getApplicationContext(), "The Finish Date Is Not Valid", Toast.LENGTH_LONG).show();
//        }
        else {
            AddVoteSync aus = new AddVoteSync(this, listItems);
            aus.execute();

        }
    }


    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
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
