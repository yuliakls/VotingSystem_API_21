package com.example.katri.lolvotingsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Katri on 09/02/2018.
 */

public class CandidateSync extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;
    public Activity activity;
    private ProgressDialog pDialog;
    User CurrentUser;
    ArrayList<HashMap<String, String>> candidateList;
    ArrayList<Voting> UserVotings;
    public TextView data;
    private ListView lv;
    String Url = "";
    String ExistingVote;
    int flag;

    public CandidateSync(Activity activity,String ExistingVote){
        this.activity = activity;
        UserVotings = new ArrayList<>();
        CurrentUser = CurrentUser.getInstance();
        candidateList = new ArrayList<>();
        lv = (ListView) activity.findViewById(R.id.list);
        this.flag = flag;
        this.ExistingVote = ExistingVote;
        Url = "https://morning-anchorage-32230.herokuapp.com/getcandidates";
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

    @Override
    protected String doInBackground(String... arg0) {
        String VoteNum = "'"+ExistingVote+"'";

        try {
            URL url = new URL(Url);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("VoteNum", VoteNum);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 );
            conn.setConnectTimeout(15000 );
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


    @Override
    protected void onPostExecute(String result) {
        if (pDialog.isShowing()) pDialog.dismiss();

        if(!result.equals("")){
            try {
                JSONArray UserVotings = new JSONArray(result);

                for (int i = 0; i < UserVotings.length(); i++) {
                    JSONObject c = UserVotings.getJSONObject(i);

                    String CandidateID = c.getString("CandidateID");
                    String CandidateName = c.getString("CandidateName");
/*
                    try {
                        AddVoting(id,start,end,name,description);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
*/
                    HashMap<String, String> contact = new HashMap<>();

                    contact.put("id", CandidateID);
                    contact.put("name", CandidateName);

                    candidateList.add(contact);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(activity.getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
        }

        ListAdapter adapter = new SimpleAdapter(
                activity, candidateList,
                R.layout.list_item, new String[]{"name"}, new int[]{R.id.name});

        lv.setAdapter(adapter);
        delegate.processFinish(candidateList);
    }
/*
    public  void AddVoting(String CandidateID, String CandidateName) throws ParseException {
        Candidate NewCandidate = new Candidate(String CandidateID, String CandidateName);
        UserVotings.add(NewVote);
    }
*/
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
