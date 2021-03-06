package com.bvocal.goounj.pojo;

/**
 * Created by nandagopal on 14/11/15.
 */
public class ChoicesItem {

    public boolean mChoiceChecked;
    public String mChoiceName;
    public int mChoiceOptionId;
    public String mChoicePercent;

    public ChoicesItem() {

    }

    public ChoicesItem(String mChoiceName) {
        this.mChoiceName = mChoiceName;
    }

    public ChoicesItem(String mChoiceName, int optionId) {
        this.mChoiceName = mChoiceName;
        this.mChoiceOptionId = optionId;
    }

    public ChoicesItem(String mChoiceName, int optionId, String mChoicePercent) {
        this.mChoiceName = mChoiceName;
        this.mChoiceOptionId = optionId;
        this.mChoicePercent = mChoicePercent;
    }

    public ChoicesItem(int optionId, String mChoiceName, String mChoicePercent) {
        this.mChoiceName = mChoiceName;
        this.mChoiceOptionId = optionId;
        this.mChoicePercent = mChoicePercent;
    }

    public ChoicesItem(String mChoiceName, boolean mChoiceChecked) {
        this.mChoiceName = mChoiceName;
        this.mChoiceChecked = mChoiceChecked;
    }

}
