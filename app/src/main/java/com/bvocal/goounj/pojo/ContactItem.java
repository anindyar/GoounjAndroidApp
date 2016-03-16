package com.bvocal.goounj.pojo;

/**
 * Created by nandagopal on 25/10/15.
 */
public class ContactItem {
    public String mcontactName, mContactNumber, mContactImage;
    public boolean mChkSelected;

    public ContactItem() {

    }

    public ContactItem(String mcontactName, String mContactNumber, String mContactImage) {
        this.mcontactName = mcontactName;
        this.mContactNumber = mContactNumber;
        this.mContactImage = mContactImage;
    }

    public ContactItem(String mcontactName, String mContactNumber) {
        this.mcontactName = mcontactName;
        this.mContactNumber = mContactNumber;
    }

}
