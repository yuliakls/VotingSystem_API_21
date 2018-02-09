package com.example.katri.lolvotingsystem;

/**
 * Created by Katri on 09/02/2018.
 */

public class Candidate {
    String CandidateID;
    String CandidateName;

    public Candidate(String CandidateID, String CandidateName){
        this.CandidateID = CandidateID;
        this.CandidateName = CandidateName;
    }

    public String getVoteDescription() {
        return CandidateName;
    }
}
