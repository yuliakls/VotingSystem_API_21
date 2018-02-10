package com.example.katri.lolvotingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by salib on 06/02/2018.
 */

public class AddVoteSync extends AsyncTask<String, Void, String> {
    public Activity activity;

    private EditText VoteName;
    private EditText VoteDescription;
    private EditText Start;
    private EditText Finish;


    ArrayList<HashMap<String, String>> voteList;
    ArrayList<Voting> UserVotings;
    public TextView data;
    private ListView lv;


    //private ArrayList<String> listItems;

    private ProgressDialog pDialog;

    public AddVoteSync(Activity activity){
        this.activity =  activity;
        this.VoteName = (EditText) activity.findViewById(R.id.VoteName);
        this.VoteDescription = (EditText) activity.findViewById(R.id.VoteDescription);

        this.Start = (EditText) activity.findViewById(R.id.start);
        this.Finish = (EditText) activity.findViewById(R.id.finish);


        UserVotings = new ArrayList<>();
        voteList = new ArrayList<>();
        lv = (ListView) activity.findViewById(R.id.list);


    }

    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity,ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }

    protected String doInBackground(String... arg0) {

        String VN = "'" + VoteName.getText().toString() + "'";
        String VD = "'" + VoteDescription.getText().toString() + "'";
        String ST = "'" + Start.getText().toString() + "'";
        String FN = "'" + Finish.getText().toString() + "'";

        try {
            URL url = new URL("https://morning-anchorage-32230.herokuapp.com/addvoting");

            JSONObject postDataParams = new JSONObject();

            postDataParams.put("VoteName", VN);
            postDataParams.put("VoteDescription", VD);
            postDataParams.put("Finish", FN);
            postDataParams.put("Start", ST);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            } else {
                return new String("False : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }


    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }



    @Override
    protected void onPostExecute(String result) {
        if (pDialog.isShowing()) pDialog.dismiss();

        if (!result.equals("")) {

            if (isNumeric(result)) {


                for (int i = 0; i < voteList.size(); i++) {

                    String CN = "'" + voteList.get(voteList.indexOf(i)).toString() + "'";
                    String ID = "'" + Integer.toString(i) + "'";

                    try {
                        URL url = new URL("https://morning-anchorage-32230.herokuapp.com/addcandidate");

                        JSONObject postDataParams = new JSONObject();

                        postDataParams.put("CandidateName", CN);
                        postDataParams.put("CandidateID", ID);
                        postDataParams.put("VoteNum", result);


                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(20000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));
                        writer.flush();
                        writer.close();
                        os.close();

                        int responseCode = conn.getResponseCode();
                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuffer sb = new StringBuffer("");
                            String line = "";

                            while ((line = in.readLine()) != null) {
                                sb.append(line);
                                break;
                            }
                            in.close();
                            Toast.makeText(activity.getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity.getApplicationContext(), responseCode, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity.getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }

                    // Clear
                    this.VoteName.getText().clear();
                    this.VoteDescription.getText().clear();
                    this.Start.getText().clear();
                    this.Finish.getText().clear();
                    this.voteList.clear();

//                for(int i=0 ; i<voteList.size(); i++ ) {
//                    this.voteList.remove(i);
//                }

                }
            }
            else{
                    Toast.makeText(activity.getApplicationContext(), "Sorry!", Toast.LENGTH_LONG).show();

                    // Clear
                    this.VoteName.getText().clear();
                    this.VoteDescription.getText().clear();
                    this.Start.getText().clear();
                    this.Finish.getText().clear();
                    this.voteList.clear();
                }

            }

            else {
                Toast.makeText(activity.getApplicationContext(), "Error! We Dont Know Why :(", Toast.LENGTH_LONG).show();
            }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }




}
