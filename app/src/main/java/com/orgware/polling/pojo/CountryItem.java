package com.orgware.polling.pojo;

/**
 * Created by nandagopal on 23/10/15.
 */
public class CountryItem {
    public String mCountryName, mCountryCode;

    public CountryItem(String mCountryName, String mCountryCode) {
        this.mCountryName = mCountryName;
        this.mCountryCode = mCountryCode;
    }

    public CountryItem(String mCountryName) {
        this.mCountryName = mCountryName;
    }

    public CountryItem() {
    }
}
