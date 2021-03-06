package com.bvocal.goounj.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.SpinnerAdapter;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.pojo.MenuListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 20/10/15.
 */
public class Methodutils implements Appinterface {

    public static final int[] mResultColor = new int[]{R.color.tab_opinion, R.color.tab_social, R.color.tab_survey,
            R.color.holo_red_light, R.color.home_bg, R.color.home_bg};
    public static final String[] mMonthArray = new String[]{"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static void showCategoryDialog(Context mContext, final TextView mTextview, final List<String> mCategoryList) {
        final Dialog mCategoryDialog = new Dialog(mContext);
        mCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCategoryDialog.setCancelable(true);
        mCategoryDialog.setContentView(R.layout.dialog_spinner);
        ListView mCategoryListview = (ListView) mCategoryDialog.findViewById(R.id.listviewCategory);
        SpinnerAdapter mSpinnerAdapter = new SpinnerAdapter(mContext, R.layout.item_spinner, mCategoryList);
        mCategoryListview.setAdapter(mSpinnerAdapter);
        mCategoryListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mTextview.setText("" + mCategoryList.get(position));
                mCategoryDialog.dismiss();
            }
        });
        mCategoryDialog.show();

    }

    public static String encodeimage(Bitmap bitmap) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage.toString();
        } catch (Exception e) {
            return "";
        }

    }

//    public static void showListSearch(final Context mContext, final List<CurrentPollItem> itemList, final RecyclerView mCurrentPollList) {
//        final Dialog mCategoryDialog = new Dialog(mContext);
//        mCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mCategoryDialog.setCancelable(true);
//        mCategoryDialog.setContentView(R.layout.dialog_search);
//        Window window = mCategoryDialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        wlp.gravity = Gravity.TOP;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//
//        ImageView dlg_clear = (ImageView) mCategoryDialog.findViewById(R.id.search_close);
//        ImageView dlg_back = (ImageView) mCategoryDialog.findViewById(R.id.search_back);
//        final EditText dlg_textbox = (EditText) mCategoryDialog.findViewById(R.id.search_text);
//        dlg_textbox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                CurrentPollAdapter mAdapter;
//                if (!s.toString().equals("")) {
//                    List<CurrentPollItem> filteredTitles = new ArrayList<>();
//                    for (int i = 0; i < itemList.size(); i++) {
//                        if (itemList.get(i).mCurrentPollTitle.toString().toLowerCase().contains(s) ||
//                                itemList.get(i).mCurrentPollTitle.toString().toUpperCase().contains(s) ||
//                                itemList.get(i).mCurrentPollTitle.toString().contains(s)) {
//                            filteredTitles.add(itemList.get(i));
//                        }
//                    }
//                    mAdapter = new CurrentPollAdapter(mContext, filteredTitles, 1);
//                    mCurrentPollList.setAdapter(mAdapter);
////                    mAdapter = new ContactGridviewAdapter(act, filteredTitles);
////                    mRecyclerView.setAdapter(mAdapter);
//                } else {
//                    mAdapter = new CurrentPollAdapter(mContext, itemList, 1);
//                    mCurrentPollList.setAdapter(mAdapter);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        dlg_clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dlg_textbox.setText("");
//            }
//        });
//        dlg_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCategoryDialog.dismiss();
//            }
//        });
//
//        mCategoryDialog.show();
//
//    }

    public static Bitmap decodeProfile(String encodedString) {
        Bitmap decodedByte = null;
        try {
            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedByte;
    }

    public static List<MenuListItem> setMenuName() {
        List<MenuListItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuListItem("My Profile",
                R.drawable.menu_profile_ic));
        menuItems.add(new MenuListItem("About Us",
                R.drawable.menu_about_us));
        menuItems.add(new MenuListItem("TimeLine",
                R.drawable.menu_timeline));
        menuItems.add(new MenuListItem("Change Number",
                R.drawable.menu_change_number));
        menuItems.add(new MenuListItem("AD With Us",
                R.drawable.menu_adwith_us));
        menuItems.add(new MenuListItem("Settings", R.drawable.menu_settings));
        return menuItems;
    }

    public static List<String> setCategoryName() {
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Art & Culture");
        menuItems.add("Beauty & Personal Care");
        menuItems.add("Education");
        menuItems.add("Entertainment");
        menuItems.add("Food & Beverages");
        menuItems.add("General Reference");
        menuItems.add("Geography & Places");
        menuItems.add("Health & Fitness");
        menuItems.add("HistoryVote & Events");
        menuItems.add("Latest Trends");
        menuItems.add("Life & Concepts");
        menuItems.add("Lifestyle & Shopping");
        menuItems.add("Mathematics & Logic");
        menuItems.add("Nature & Wild Life");
        menuItems.add("Neighbourhood & Society");
        menuItems.add("People");
        menuItems.add("Philosophy & Thinking");
        menuItems.add("Photography");
        menuItems.add("Religion & Belief System");
        menuItems.add("Science & Technology");
        menuItems.add("Sports");
        menuItems.add("Travel & Tourism");
        menuItems.add("Others");

        return menuItems;
    }

    /*Dialog method with one button listener*/
    public static void message(Context context, String message, final View.OnClickListener onClickListener) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Message");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (onClickListener != null)
                    onClickListener.onClick(null);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*Dialog method with one button listener*/
    public static void messageWithTitle(Context context, String title, String message, final View.OnClickListener onClickListener) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (onClickListener != null)
                    onClickListener.onClick(null);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /*Dialog method with two button listener*/
    public static void message2btn(Context context, String message, String positivemsg, String negativmsg, final View.OnClickListener onClickListener, final View.OnClickListener onClickListener2) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Message");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(positivemsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                onClickListener.onClick(null);
            }
        });
        alertDialogBuilder.setNegativeButton(negativmsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                onClickListener2.onClick(null);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String setParam(String... param) {
        JSONObject objParam = new JSONObject();
        for (int i = 0; i < param.length; i += 2) {
            try {
                objParam.put(param[i], param[i + 1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objParam.toString();
    }

    public static void showNamesDialog(Context context, JSONArray mContactArrayNames) {
        Dialog mContactDialog = new Dialog(context, R.style.dialog);
        mContactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContactDialog.setCancelable(true);
        mContactDialog.setContentView(R.layout.dialog_names);
        ListView textView = (ListView) mContactDialog.findViewById(R.id.list_contact_names);
        SpinnerAdapter mSpinnerAdapter;
        List<String> mNamesList = new ArrayList<>();
        mNamesList.clear();
        try {
            for (int i = 0; i < mContactArrayNames.length(); i++) {
                mNamesList.add("" + mContactArrayNames.get(i));
            }
            mSpinnerAdapter = new SpinnerAdapter(context, R.layout.item_spinner, mNamesList);
            textView.setAdapter(mSpinnerAdapter);
        } catch (Exception e) {

        }

        mContactDialog.show();
    }

}
