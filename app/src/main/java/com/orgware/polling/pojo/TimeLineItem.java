package com.orgware.polling.pojo;

/**
 * Created by nandagopal on 22/1/16.
 */
public class TimeLineItem {

    public String mTimeLineDate, mTimeLinePollTitle, mTimeLinePollCreator, mType;

    public TimeLineItem() {

    }

    public TimeLineItem(String mTimeLineDate, String mTimeLinePollTitle, String mTimeLinePollCreator, String type) {
        this.mType = type;
        this.mTimeLineDate = mTimeLineDate;
        this.mTimeLinePollTitle = mTimeLinePollTitle;
        this.mTimeLinePollCreator = mTimeLinePollCreator;
    }

}
