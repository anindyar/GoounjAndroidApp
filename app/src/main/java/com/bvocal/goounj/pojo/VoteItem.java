package com.bvocal.goounj.pojo;

/**
 * Created by Nanda on 02/04/16.
 */
public class VoteItem {

    public String electionName, endDate, startDate, nominationEndDate, associationName;
    public int electionId, isVoted;

    public VoteItem() {

    }

//    public VoteItem(String electionName, String endDate, String startDate, String nominationEndDate, String associationName, int electionId, int isVoted) {
//        this.electionName = electionName;
//        this.endDate = endDate;
//        this.startDate = startDate;
//        this.nominationEndDate = nominationEndDate;
//        this.associationName = associationName;
//        this.electionId = electionId;
//        this.isVoted = isVoted;
//    }

    public VoteItem(int electionId, String electionName, String endDate, String startDate, int isVoted, String nominationEndDate, String associationName) {
        this.electionId = electionId;
        this.electionName = electionName;
        this.endDate = endDate;
        this.startDate = startDate;
        this.isVoted = isVoted;
        this.nominationEndDate = nominationEndDate;
        this.associationName = associationName;

    }
}
