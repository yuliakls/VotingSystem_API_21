package com.example.katri.lolvotingsystem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Katri on 09/02/2018.
 */

public interface AsyncResponse {
    void processFinish(ArrayList<HashMap<String, String>> voteList);
}
