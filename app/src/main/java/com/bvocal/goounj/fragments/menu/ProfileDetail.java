package com.bvocal.goounj.fragments.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bvocal.goounj.MenuDetailActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.fragments.BaseFragment;
import com.bvocal.goounj.utils.Methodutils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nandagopal on 21/1/16.
 */
public class ProfileDetail extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final int CAMERA_PICK = 1, GALLERY_PICK = 2, PICK_CROP = 3;
    private TextView mProfileGender;
    private Dialog mGenderDialog;
    private Bitmap thePic;
    private CircleImageView mProfileImage;
    private RadioButton mMale, mFemale;
    private EditText mProfileName, mProfileAge;
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
        (mProfileImage = (CircleImageView) view.findViewById(R.id.profile_image)).setOnClickListener(this);
        mProfileName = (EditText) view.findViewById(R.id.profile_name_detail);
        mProfileAge = (EditText) view.findViewById(R.id.profile_age);
        (mProfileGender = (TextView) view.findViewById(R.id.profile_gender)).setOnClickListener(this);
        ((mCBProfileNameImg = (CheckBox) view.findViewById(R.id.profile_edit_name))).setOnCheckedChangeListener(this);
        ((mCBProfileGender = (CheckBox) view.findViewById(R.id.profile_edit_gender))).setOnCheckedChangeListener(this);
        ((mCBProfileAge = (CheckBox) view.findViewById(R.id.profile_edit_age))).setOnCheckedChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProfileName.setText("" + preferences.getString(USERNAME, "Enter your name"));
        mProfileGender.setText("" + preferences.getString(GENDER, "Select gender"));
        mProfileAge.setText("" + preferences.getString(PROFILE_AGE, "Enter your age"));
        mProfileImage.setEnabled(false);
        if (!preferences.getString(ENCODE_IMAGE, "").equals("") || !preferences.getString(ENCODE_IMAGE, "").equals("null") ||
                preferences.getString(ENCODE_IMAGE, "") != null)
            mProfileImage.setImageBitmap(Methodutils.decodeProfile(preferences.getString(ENCODE_IMAGE, "")));
        else
            mProfileImage.setImageResource(R.drawable.ic_usericon_blue);
        ((MenuDetailActivity) act).setTitle("Profile");

    }

    /*This method is used to open a dialog, that dialog contains the choosing option like Gallery or Camera to choose the image.
         After choose the image it navigates to onActivityResult() method */
    public void choosePic() {

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
                mProfileGender.setText("Male");
                mGenderDialog.dismiss();
            }
        });
        mFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mProfileGender.setText("Female");
                mGenderDialog.dismiss();
            }
        });

        mGenderDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case CAMERA_PICK:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    performCrop(getImageUri(photo));
                    break;
                case GALLERY_PICK:
                    performCrop(data.getData());
                    break;
                case PICK_CROP:
                    Bundle extras = data.getExtras();
                    thePic = extras.getParcelable("data");
//                    editor.putString(ENCODE_IMAGE, "" + Methodutils.encodeimage(thePic)).commit();
                    mProfileImage.setImageBitmap(thePic);
                    break;

            }

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
            mProfileGender.setEnabled(true);
            mProfileGender.setClickable(true);
            mProfileGender.setFocusableInTouchMode(true);
//            editor.putString(GENDER, "" + mProfileGender.getText().toString()).commit();
        } else {
            editor.putString(GENDER, "" + mProfileGender.getText().toString()).commit();
            mProfileGender.setEnabled(false);
            mProfileGender.setFocusableInTouchMode(false);
        }

        if (mCBProfileAge.isChecked()) {
            mProfileAge.setEnabled(true);
            mProfileAge.setFocusableInTouchMode(true);
//            editor.putString(PROFILE_AGE, mProfileAge.getText().toString()).commit();
        } else {
            editor.putString(PROFILE_AGE, mProfileAge.getText().toString()).commit();
            mProfileAge.setEnabled(false);
            mProfileAge.setFocusableInTouchMode(false);
        }

        if (mCBProfileNameImg.isChecked()) {
            mProfileImage.setClickable(true);
            mProfileName.setEnabled(true);
            mProfileImage.setFocusable(true);
            mProfileImage.setEnabled(true);
            mProfileImage.setFocusableInTouchMode(true);
//            editor.putString(USERNAME, mProfileName.getText().toString()).commit();
//            if (thePic != null)
//                editor.putString(ENCODE_IMAGE, Methodutils.encodeimage(thePic)).commit();
        } else {
            editor.putString(USERNAME, mProfileName.getText().toString()).commit();
            if (thePic != null) {
                editor.putString(ENCODE_IMAGE, Methodutils.encodeimage(thePic)).commit();
                mProfileImage.setImageBitmap(Methodutils.decodeProfile(preferences.getString(ENCODE_IMAGE, "")));
            }
            mProfileImage.setClickable(false);
            mProfileImage.setFocusable(false);
            mProfileImage.setEnabled(false);
            mProfileImage.setFocusableInTouchMode(false);
            mProfileName.setEnabled(false);
        }


    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra("scale", true);
            startActivityForResult(cropIntent, PICK_CROP);
        } catch (ActivityNotFoundException anfe) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Bitmap data) {
        String uri = Environment.getExternalStorageDirectory().toString();
        File f = new File(uri + "/Trackidon");
        if (!f.exists())
            f.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(f, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            data.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_gender:
                showGenderDialog();
                break;
            case R.id.profile_image:
                choosePic();
                break;
        }
    }
}
