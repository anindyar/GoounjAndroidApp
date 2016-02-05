package com.orgware.polling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orgware.polling.database.GoounjDatabase;
import com.orgware.polling.interfaces.Appinterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nandagopal on 31-Aug-15.
 */
public class BaseActivity extends AppCompatActivity implements Appinterface {


    /*The Pixel variables used to get the maximum pixels of width and height of the current mobile and menuWidth is used to
    set the menu width based on Mobil screen*/
    public int heightPixel, widthPixel, menuWidth;

    /*isScreenXlarge is used to get the device resolution like, if screen resolution is large by getting value from above pixel variables,
    * then the screen view is going to set as Landscape to put the menu as static, if not means screen view will be portrait*/
    public boolean isScreenXlarge;

    /*SharedPreferences - is used to maintain to current user values like UserId and Id of the particular user which is used frequently.
    * Instead of calling local databse frequently, sharedpreferences are very useful
    * Editor - is used to insert or update the value in preferences */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    /*LayoutInflater- is used to add a external layout inside another layout at runtime*/
    LayoutInflater mInflater;

    /**
     * activity is the context of the current activity passed from any of the activity or fragment, purpose to handle UI feature
     */
    FragmentActivity activity;

    GoounjDatabase db;
    Configuration configuration;
    /*Typeface is the class to change the font style of an application*/
    private Typeface typefaceMuliRegular;

    /*
    * Declares the variables only one time of an application and reused
     * Sets the Menu size of the application
     * Sets the orientation of the application by accessing device width and height*/
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
//        configuration = new Configuration();
//        configuration.locale = Locale.JAPANESE;
//        this.getResources().updateConfiguration(configuration, null);

        activity = this;
        preferences = getSharedPreferences(SHARED_PREFERENCES_POLLING, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typefaceMuliRegular = Typeface.createFromAsset(getAssets(),
                "LATO_LIGHT_0.TTF");
        db = new GoounjDatabase(this);
        setOrientation(); // Sets the orientation of an application screen by checking mobile width and height
        setMenuSize(); // Sets the menu size of the application
    }

    /*Called when an activity is resumed and changes the font of the application*/
    @Override
    protected void onResume() {
        super.onResume();
        changeTypeface(); // Change the typeface (font) of an application
    }


    public void makeToast(Context mContext, String input) {
        Toast.makeText(mContext, "" + input, Toast.LENGTH_SHORT).show();
    }

    /*Declares the default content ID to change Font style of the application.*/
    public void changeTypeface() {
        changeTypeface((ViewGroup) findViewById(android.R.id.content));
    }

    /* Method changes the text default font to muli custom font
    *
    * @param vGroup Base class for layouts and views,this can change the font of every view of an application*/
    private void changeTypeface(ViewGroup vGroup) {
        for (int i = 0; i < vGroup.getChildCount(); i++) {
            View v = vGroup.getChildAt(i);
            if (v instanceof ImageView || v instanceof ImageButton
                    || v instanceof ListView)
                continue;
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(typefaceMuliRegular);
            } else if (v instanceof RadioButton) {
                ((RadioButton) v).setTypeface(typefaceMuliRegular);
            } else if (v instanceof DrawerLayout || v instanceof FrameLayout
                    || v instanceof LinearLayout || v instanceof RelativeLayout
                    || v instanceof RadioGroup || v instanceof ViewGroup) {
                changeTypeface((ViewGroup) v);
            }
        }
    }

    /*Detects the touches of the current screen of an application and finilize whether the keypad enable or not
    *
    * @param event A gesture starts with a motion event and when ACTION_UP is equal to current event the keypad has been disabled*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean evnt = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scr[] = new int[2];
            w.getLocationOnScreen(scr);
            float x = event.getRawX() + w.getLeft() - scr[0];
            float y = event.getRawY() + w.getTop() - scr[1];
            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {
                hideSoftInput();
            }

        }
        return evnt;
    }

    public void setMenuintent(int type) {
        Intent menuDetailIntent = new Intent(this, MenuDetailActivity.class);
        menuDetailIntent.putExtra("Menu_Detail", type);
        startActivity(menuDetailIntent);
    }


    public Fragment setPagerFragment(Fragment fragment, int pos) {
        Bundle args = new Bundle();
        args.putInt(PAGER_COUNT, pos);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment setPagerFragment(Fragment fragment, String key, int pos, String json_key, String json_value) {
        Bundle args = new Bundle();
        args.putInt(key, pos);
        args.putString(json_key, json_value);
        fragment.setArguments(args);
        return fragment;
    }

    public String splitFromString(String stringName) {

        try {
            SimpleDateFormat mRequiredDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = mSimpleDateFormat.parse(stringName);

            return mRequiredDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


//        StringBuilder sb = new StringBuilder();
//        String unwanted, wanted, wantedOne, wantedTwo, unWantedone;
//        StringTokenizer tokenize = new StringTokenizer(stringName, "."); //2016-01-28T08:30:29.000Z
//        wanted = "" + tokenize.nextToken();
//        unwanted = "" + tokenize.nextToken();
//        StringTokenizer tokenizer = new StringTokenizer(wanted, "T"); //2016-01-28T08:30:29
//        sb.append("" + tokenizer.nextToken());
//        sb.append(" ");
//        unWantedone = "" + tokenizer.nextToken();
//        String[] wantedTime = unWantedone.split(":");
//        wantedOne = wantedTime[0];
//        wantedTwo = wantedTime[1];
//        sb.append(wantedOne + ":" + wantedTwo);
//        Log.e("Date", "" + sb.toString());
//        return sb.toString();
        return "";
    }


    public void setNewFragment(Fragment fragment, String title, boolean addStack) {

        FragmentManager manager = getSupportFragmentManager();

        if (!addStack && manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment mCurrentFragment = manager.findFragmentById(R.id.fragment_content);
            if (mCurrentFragment != null)
                manager.beginTransaction().remove(mCurrentFragment).commit();
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottomtotop, R.anim.bottomtotop);
        transaction.replace(R.id.fragment_content, fragment, title);
        if (addStack)
            transaction.addToBackStack(title);
        transaction.commit();
    }

    public void setNewFragment(Fragment fragment, int fragment_content, String title, boolean addStack) {

        FragmentManager manager = getSupportFragmentManager();

        if (!addStack && manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment mCurrentFragment = manager.findFragmentById(fragment_content);
            if (mCurrentFragment != null)
                manager.beginTransaction().remove(mCurrentFragment).commit();
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottomtotop, R.anim.bottomtotop);
        transaction.replace(fragment_content, fragment, title);
        if (addStack)
            transaction.addToBackStack(title);
        transaction.commit();
    }

    /*This sets the menu size for the corresponding devices
    *
    * returns menuwidth a width of the menu*/
    private void setMenuSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixel = displayMetrics.heightPixels;
        widthPixel = displayMetrics.widthPixels;
        menuWidth = (int) (isScreenXlarge ? widthPixel * 0.45 : widthPixel * 0.85);
        ;
    }

    /*This declares the orientation of the entire application for the various devices.
    *
    * returns isScreenXlarge whether the screen is medium or normal or large*/
    private void setOrientation() {

        int screen = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screen == Configuration.SCREENLAYOUT_SIZE_SMALL
                || screen == Configuration.SCREENLAYOUT_SIZE_NORMAL
                || screen == Configuration.SCREENLAYOUT_SIZE_LARGE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (screen == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            isScreenXlarge = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /*Hide the keypad when touches occured in other side of the keypad*/
    public void hideSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                .getWindowToken(), 0);
    }

    /*This is used to set the text value to the corresponding textview
    *
    * @param input gets the user input
    * @param id gets the id of the textview*/
    void setText(String input, int id) {
        ((TextView) findViewById(id)).setText(input);
    }

    /*This method is used to, when the activity needs result
    *
     * @param requestCode which sends the request code and execute the code if the request code is right
     * @param resultCode which returns the result code of the request code
     * @param data which returns if any data is available */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
