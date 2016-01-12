package com.orgware.polling.pojo;

/**
 * Created by nandagopal on 26/10/15.
 */
public class CurrentPollItem {
    public String mCurrentPollTitle, mCurrentPollCreatedBy, mCurrentPollStart, mCurrentPollEnd, mCreatedUserName, mSelfNominationDate;
    public int mCurrentPollImage, currentPollId, mCurrentBoost;


    public CurrentPollItem() {

    }

    public CurrentPollItem(int pollId) {
        this.currentPollId = pollId;

    }


    public CurrentPollItem(int pollId, String mCurrentPollStart, String mCurrentPollEnd, String mCurrentPollName, int mIsBoost, String mCreatedUserName) {
        this.currentPollId = pollId;
        this.mCurrentPollStart = mCurrentPollStart;
        this.mCurrentPollEnd = mCurrentPollEnd;
        this.mCurrentPollTitle = mCurrentPollName;
        this.mCurrentBoost = mIsBoost;
        this.mCreatedUserName = mCreatedUserName;
    }

    public CurrentPollItem(int pollId, String mCurrentPollStart, String mCurrentPollEnd, String mCurrentPollName, int mIsBoost, String mCreatedUserName, String selfNominationDate) {
        this.currentPollId = pollId;
        this.mCurrentPollStart = mCurrentPollStart;
        this.mCurrentPollEnd = mCurrentPollEnd;
        this.mCurrentPollTitle = mCurrentPollName;
        this.mCurrentBoost = mIsBoost;
        this.mCreatedUserName = mCreatedUserName;
        this.mSelfNominationDate = selfNominationDate;
    }

    public CurrentPollItem(String mCurrentPollStart, String mCurrentPollEnd, String mCurrentPollName, String mCreatedUserName) {
        this.mCurrentPollStart = mCurrentPollStart;
        this.mCurrentPollEnd = mCurrentPollEnd;
        this.mCurrentPollTitle = mCurrentPollName;
        this.mCreatedUserName = mCreatedUserName;
    }

}
;