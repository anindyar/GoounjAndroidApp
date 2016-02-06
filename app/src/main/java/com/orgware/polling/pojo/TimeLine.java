package com.orgware.polling.pojo;

/**
 * Created by nandagopal on 22/1/16.
 */
public class TimeLine {

    public String userName, date, pollName, createdUser;
    public int pollId;

    public TimeLine(String userName, String date, String pollName, String createdUser, int pollId) {
        this.userName = userName;
        this.date = date;
        this.pollName = pollName;
        this.createdUser = createdUser;
        this.pollId = pollId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPollName() {
        return pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
}
