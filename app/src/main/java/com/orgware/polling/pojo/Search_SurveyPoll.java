package com.orgware.polling.pojo;

/**
 * Created by nandagopal on 2/2/16.
 */
public class Search_SurveyPoll {

    public int pollId;
    public int isGeneric;
    public String endDate;
    public int isSurvey;
    public String startDate;
    public int isBoost;
    public int createdUserId;
    public String createdUserName;
    public String pollName;
    public int isActive;

    public Search_SurveyPoll(int pollId, int isGeneric, String endDate, int isSurvey, String startDate, int isBoost, int createdUserId, String createdUserName, String pollName, int isActive) {
        this.pollId = pollId;
        this.isGeneric = isGeneric;
        this.endDate = endDate;
        this.isSurvey = isSurvey;
        this.startDate = startDate;
        this.isBoost = isBoost;
        this.createdUserId = createdUserId;
        this.createdUserName = createdUserName;
        this.pollName = pollName;
        this.isActive = isActive;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public int getIsGeneric() {
        return isGeneric;
    }

    public void setIsGeneric(int isGeneric) {
        this.isGeneric = isGeneric;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getIsSurvey() {
        return isSurvey;
    }

    public void setIsSurvey(int isSurvey) {
        this.isSurvey = isSurvey;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getIsBoost() {
        return isBoost;
    }

    public void setIsBoost(int isBoost) {
        this.isBoost = isBoost;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
