package com.bvocal.goounj.pojo;

/**
 * Created by nandagopal on 23/10/15.
 */
public class CountryItem {
    public String mCountryName, mCountryCode, mCityName;

//    public CountryItem(String mCountryName, String mCountryCode) {
//        this.mCountryName = mCountryName;
//        this.mCountryCode = mCountryCode;
//    }

    public CountryItem(String mCountryName, String mCityName) {
        this.mCountryName = mCountryName;
        this.mCityName = mCityName;
    }

    public CountryItem(String mCountryName) {
        this.mCountryName = mCountryName;
    }

    public CountryItem() {
    }
}
