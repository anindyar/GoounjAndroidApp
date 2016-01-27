package com.orgware.polling.fragments.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.orgware.polling.MainHomeActivity;
import com.orgware.polling.MenuDetailActivity;
import com.orgware.polling.R;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.utils.Methodutils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by nandagopal on 21/1/16.
 */
public class ProfileDetail extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    TextView mProfileName, mProfileGender;
    int CAMERA_PICK = 1, GALLERY_PICK = 2, PICK_CROP = 3;
    private Dialog mGenderDialog;
    private RadioButton mMale, mFemale;
    private CheckBox mCBProfileNameImg, mCBProfileGender, mCBProfileAge;

    @Override
    public void setTitle() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProfileName = (TextView) view.findViewById(R.id.profile_text_detail);
        mProfileGender = (TextView) view.findViewById(R.id.profile_gender);
        ((mCBProfileNameImg = (CheckBox) view.findViewById(R.id.profile_edit_name))).setOnCheckedChangeListener(this);
        ((mCBProfileGender = (CheckBox) view.findViewById(R.id.profile_edit_gender))).setOnCheckedChangeListener(this);
        ((mCBProfileAge = (CheckBox) view.findViewById(R.id.profile_edit_age))).setOnCheckedChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProfileName.setText("" + preferences.getString(USERNAME, ""));
        mProfileGender.setText("" + preferences.getString(GENDER, "Select Gender"));
        ((MenuDetailActivity) act).setTitle("Profile");
    }

    /*This method is used to open a dialog, that dialog contains the choosing option like Gallery or Camera to choose the image.
         After choose the image it navigates to onActivityResult() method */
    public void ChoosePic() {

        Methodutils.message2btn(getActivity(), "Choose Pic from", "Camera", "Gallery", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE),
                        CAMERA_PICK);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setType("image/*").setAction(
                        Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_PICK);
            }
        });

    }

    private void showGenderDialog() {
        mGenderDialog = new Dialog(act, R.style.dialog);
        mGenderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGenderDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mGenderDialog.setContentView(R.layout.dialog_gender);
        mMale = (RadioButton) mGenderDialog.findViewById(R.id.gender_male);
        mFemale = (RadioButton) mGenderDialog.findViewById(R.id.gender_female);

        mMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putString(GENDER, "Male").commit();
                mProfileGender.setText("" + preferences.getString(GENDER, "Select Gender"));
                mGenderDialog.dismiss();
            }
        });
        mFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putString(GENDER, "Female").commit();
                mProfileGender.setText("" + preferences.getString(GENDER, "Select Gender"));
                mGenderDialog.dismiss();
            }
        });

        mGenderDialog.show();
    }


    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mCBProfileGender.isChecked()) {
            showGenderDialog();
        }

//        if (mMale.isChecked()) {
//            editor.putString(GENDER, mMale.getText().toString()).commit();
//            mGenderDialog.dismiss();
//        }
//        if (mFemale.isChecked()) {
//            editor.putString(GENDER, mFemale.getText().toString()).commit();
//            mGenderDialog.dismiss();
//        }

    }
}
