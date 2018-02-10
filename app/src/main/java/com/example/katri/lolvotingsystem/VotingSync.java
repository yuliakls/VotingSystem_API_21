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
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Katri on 03/02/2018.
 */

public class VotingSync extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    public Activity activity;
    private ProgressDialog pDialog;
    User CurrentUser;
    ArrayList<HashMap<String, String>> voteList;
    ArrayList<Voting> UserVotings;
    public TextView data;
    private ListView lv;
    String Url = "";

    public VotingSync(Activity activity, int flag) {
        this.activity = activity;
        UserVotings = new ArrayList<>();
        CurrentUser = CurrentUser.getInstance();
        voteList = new ArrayList<>();
        lv = (ListView) activity.findViewById(R.id.list);

        if(flag == 1 && CurrentUser.GerStatus() == true)
            Url = "https://morning-anchorage-32230.herokuapp.com/getallvotes";
        else
            Url = "https://morning-anchorage-32230.herokuapp.com/getvotes";
            //Url = "https://morning-anchorage-32230.herokuapp.com/getvotes"+ "?UserID="+"'"+ CurrentUser.GetUserID() + "'";
    }

    @Override
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
         String ID = "'" + CurrentUser.GetUserID() + "'";

        try {
            URL url = new URL(Url);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("UserID", ID);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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

                    String id = c.getString("VoteNum");
                    String start = c.getString("Start");
                    String end = c.getString("Finish");
                    String name = c.getString("VoteName");
                    String description = c.getString("VoteDescription");

                    Log.i("error------------",start);

                    if(Compare(start, end) == false)
                        continue;

                    HashMap<String, String> contact = new HashMap<>();
                    // adding each child node to HashMap key => value
                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("start", start);
                    contact.put("end", end);
                    contact.put("description", description);
                    // adding vote to vote list
                    voteList.add(contact);
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
                activity, voteList,
                R.layout.list_item, new String[]{"name", "start",
                "end","description"}, new int[]{R.id.name,
                R.id.start,R.id.end, R.id.description});

        lv.setAdapter(adapter);

        delegate.processFinish(voteList);
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

    public static boolean Compare( String start , String finish) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = formatter.format(new Date());

            Date s = formatter.parse(start.replaceAll("Z$", "+0000"));
            Date f = formatter.parse(finish.replaceAll("Z$", "+0000"));
            Date c = formatter.parse(current.replaceAll("Z$", "+0000"));
            if (f.compareTo(c)>0 && c.compareTo(s)>0)
                return true;
        }
        catch (ParseException e)
        {
            return false;
        }
        return false;
    }
}



