package com.example.katri.lolvotingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileResults extends AppCompatActivity implements AsyncResponse{
    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;
    String ExistingVote;
    public ArrayList<HashMap<String, String>> voteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_results);

        voteList = new ArrayList<>();

        Intent intent = getIntent();
        ExistingVote = intent.getStringExtra("VoteNum");

        ResultsSync vs = new ResultsSync(this,ExistingVote);
        vs.delegate = this;
        vs.execute();

    }

    @Override
    public void processFinish(ArrayList<HashMap<String, String>> voteList, int flag) {
        this.voteList = voteList;

        pieChart = (PieChart) findViewById(R.id.chart1);
        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<String>();
        AddValuesToPIEENTRY();
        AddValuesToPieEntryLabels();
        pieDataSet = new PieDataSet(entries, "");
        pieData = new PieData(PieEntryLabels, pieDataSet);
        pieData.setValueTextSize(11f);
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieChart.setData(pieData);
        pieChart.animateY(3000);
    }

    public void AddValuesToPIEENTRY(){
        for (int i=0; i<voteList.size(); i++) {
            entries.add(new BarEntry(Float.parseFloat(voteList.get(i).get("Amount")),i));
        }
    }

    public void AddValuesToPieEntryLabels(){
        for (int i=0; i<voteList.size(); i++) {
            PieEntryLabels.add(voteList.get(i).get("Name"));
        }
    }
}
