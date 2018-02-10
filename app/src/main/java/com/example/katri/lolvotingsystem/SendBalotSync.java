package com.example.katri.lolvotingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Katri on 10/02/2018.
 */

public class SendBalotSync extends AsyncTask<String, Void, String> {
    private ProgressDialog pDialog;
    public Activity activity;
    int flag;
    User CurrentUser;
    String ExistingVote;
    String CandidateID;


    public SendBalotSync(Activity activity,int flag,String ExistingVote,String CandidateID){
        this.activity = activity;
        this.flag = flag;
        CurrentUser = CurrentUser.getInstance();
        this.ExistingVote = ExistingVote;
        this.CandidateID = CandidateID;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(activity,ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String doInBackground(String... arg0) {
        BallotKeyBuilder Key = new BallotKeyBuilder(activity.getApplicationContext());
        //Log.i("key------------------",obj.BuildKey());
        String ID = "'" + CurrentUser.GetUserID() + "'";
        String VN = "'" + ExistingVote + "'";
        String CID = "'" + CandidateID + "'";
        String answer = "false";

        try {
            URL url = new URL("https://morning-anchorage-32230.herokuapp.com/uservoted");

            JSONObject postDataParams = new JSONObject();

            postDataParams.put("UserID", ID);
            postDataParams.put("VoteNum", VN);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
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
                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

        if(answer.equals("true")) {
            try {
                Log.i("Yeahhhhhhhh",answer);
                URL url = new URL("https://morning-anchorage-32230.herokuapp.com/sendballot");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("CandidateID", CID);
                postDataParams.put("VoteNum", VN);
                postDataParams.put("VoteKey", "'totototototo'");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
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

                    Log.i("Yeahhhh2222222",sb.toString());

                    return sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        else return answer;
    }

    @Override
    protected void onPostExecute(String result) {
        if (pDialog.isShowing()) pDialog.dismiss();

        if(!result.equals("")){
            //boolean answer = Boolean.parseBoolean(result);
            Toast.makeText(activity.getApplicationContext()," ---OK--- "+result,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(activity.getApplicationContext(), "Error in server result!", Toast.LENGTH_LONG).show();
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
