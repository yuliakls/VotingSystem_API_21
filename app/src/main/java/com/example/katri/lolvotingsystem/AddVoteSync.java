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

    private  ArrayList<String> voteList;

    private ProgressDialog pDialog;

    public AddVoteSync(Activity activity,ArrayList listv){
        this.activity =  activity;
        this.VoteName = (EditText) activity.findViewById(R.id.VoteName);
        this.VoteDescription = (EditText) activity.findViewById(R.id.VoteDescription);

        this.Start = (EditText) activity.findViewById(R.id.start);
        this.Finish = (EditText) activity.findViewById(R.id.finish);

        voteList = new ArrayList<>();
        this.voteList = listv;
        //Toast.makeText(activity.getApplicationContext(), listv.size(), Toast.LENGTH_LONG).show();

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

                if (!sb.toString().equals("")) {

                    if (isNumeric(sb.toString())) {
                        for (int i = 0; i < voteList.size(); i++) {
//jhuhu
                            String CN = "'" + voteList.get(i).toString() + "'";
                            String ID = "'" + Integer.toString(i+1) + "'";

                            try {
                                URL url2 = new URL("https://morning-anchorage-32230.herokuapp.com/addcandidate");

                                JSONObject postDataParams2 = new JSONObject();

                                postDataParams2.put("CandidateName", CN);
                                postDataParams2.put("CandidateID", ID);
                                postDataParams2.put("VoteNum", sb.toString());


                                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                                conn2.setReadTimeout(20000 /* milliseconds */);
                                conn2.setConnectTimeout(15000 /* milliseconds */);
                                conn2.setRequestMethod("POST");
                                conn2.setDoInput(true);
                                conn2.setDoOutput(true);

                                OutputStream os2 = conn2.getOutputStream();
                                BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                                writer.write(getPostDataString(postDataParams2));
                                writer.flush();
                                writer.close();
                                os2.close();

                                int responseCode2 = conn2.getResponseCode();
                                if (responseCode2 == HttpsURLConnection.HTTP_OK) {
                                    BufferedReader in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                                    StringBuffer sb2 = new StringBuffer("");
                                    String line2 = "";

                                    while ((line2 = in2.readLine()) != null) {
                                        sb.append(line2);
                                        break;
                                    }
                                    in2.close();
                                    Toast.makeText(activity.getApplicationContext(), sb2.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity.getApplicationContext(), responseCode, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(activity.getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else{
                        Toast.makeText(activity.getApplicationContext(), "Sorry!", Toast.LENGTH_LONG).show();
                    }
                }

                else {
                    Toast.makeText(activity.getApplicationContext(), "Error! We Dont Know Why :(", Toast.LENGTH_LONG).show();
                }

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
            if(result.equals("true")) {
                Toast.makeText(activity.getApplicationContext(), "The User Was Added!", Toast.LENGTH_LONG).show();


                // Clear
                this.VoteName.getText().clear();
                this.VoteDescription.getText().clear();
                this.Start.getText().clear();
                this.Finish.getText().clear();
                this.voteList.clear();

            }
            else {
                Toast.makeText(activity.getApplicationContext(), "Sorry!", Toast.LENGTH_LONG).show();


                // Clear
                this.VoteName.getText().clear();
                this.VoteDescription.getText().clear();
                this.Start.getText().clear();
                this.Finish.getText().clear();
                this.voteList.clear();
            }

        }
        else{
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
