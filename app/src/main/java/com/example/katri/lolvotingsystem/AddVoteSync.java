package com.example.katri.lolvotingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    private String answer;


    ArrayList<String> voteList;
    ArrayList<Voting> UserVotings;
    public TextView data;
    private ListView lv;


    //private ArrayList<String> listItems;

    private ProgressDialog pDialog;

    public AddVoteSync(Activity activity, ArrayList<String> listv){
        this.activity =  activity;
        this.VoteName = (EditText) activity.findViewById(R.id.VoteName);
        this.VoteDescription = (EditText) activity.findViewById(R.id.VoteDescription);

        this.Start = (EditText) activity.findViewById(R.id.start);
        this.Finish = (EditText) activity.findViewById(R.id.finish);


        UserVotings = new ArrayList<>();
        voteList = new ArrayList<>();
        this.voteList = listv;
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
        URL url;

        try {
            url = new URL("https://morning-anchorage-32230.herokuapp.com/addvoting");

            JSONObject postDataParams = new JSONObject();

            postDataParams.put("VoteName", VN);
            postDataParams.put("VoteDescription", VD);
            postDataParams.put("Finish", FN);
            postDataParams.put("Start", ST);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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
                answer = sb.toString();

            } else {
                return new String("False : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

        if (isNumeric(answer)) {
            HttpsURLConnection conn = null;
            OutputStream os;
            BufferedWriter writer;
            JSONObject postDataParams;
            String line;
            BufferedReader in;
            StringBuffer sb;
            String CN;
            String ID;
            String num;
            int responseCode;

            try {
                url = new URL("https://morning-anchorage-32230.herokuapp.com/addcandidate");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < voteList.size(); i++) {
                CN = "'" + voteList.get(i).toString() + "'";
                ID = "'" + Integer.toString(i+1) + "'";
                num = "'" + answer + "'";

                try {

                    postDataParams = new JSONObject();
                    postDataParams.put("CandidateName", CN);
                    postDataParams.put("CandidateID", ID);
                    postDataParams.put("VoteNum", num);

                    try {
                        conn = (HttpsURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    conn.setReadTimeout(20000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    os = conn.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    responseCode = conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuffer("");
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();
                    }

                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }

            }
        }
        else{

            this.VoteName.getText().clear();
            this.VoteDescription.getText().clear();
            this.Start.getText().clear();
            this.Finish.getText().clear();
            this.voteList.clear();
        }

        return "ok";
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
            Toast.makeText(activity.getApplicationContext(), "Voting Added!", Toast.LENGTH_LONG).show();
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
