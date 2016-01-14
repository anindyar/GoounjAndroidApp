package com.orgware.polling.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orgware.polling.R;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.adapters.SpinnerAdapter;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.pojo.MenuListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 20/10/15.
 */
public class Methodutils {

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

    public static void showListSearch(final Context mContext, final List<CurrentPollItem> itemList, final RecyclerView mCurrentPollList) {
        final Dialog mCategoryDialog = new Dialog(mContext);
        mCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCategoryDialog.setCancelable(true);
        mCategoryDialog.setContentView(R.layout.dialog_search);
        Window window = mCategoryDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        ImageView dlg_clear = (ImageView) mCategoryDialog.findViewById(R.id.search_close);
        ImageView dlg_back = (ImageView) mCategoryDialog.findViewById(R.id.search_back);
        final EditText dlg_textbox = (EditText) mCategoryDialog.findViewById(R.id.search_text);
        dlg_textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CurrentPollAdapter mAdapter;
                if (!s.toString().equals("")) {
                    List<CurrentPollItem> filteredTitles = new ArrayList<>();
                    for (int i = 0; i < itemList.size(); i++) {
                        if (itemList.get(i).mCurrentPollTitle.toString().toLowerCase().contains(s) ||
                                itemList.get(i).mCurrentPollTitle.toString().toUpperCase().contains(s) ||
                                itemList.get(i).mCurrentPollTitle.toString().contains(s)) {
                            filteredTitles.add(itemList.get(i));
                        }
                    }
                    mAdapter = new CurrentPollAdapter(mContext, filteredTitles, 1);
                    mCurrentPollList.setAdapter(mAdapter);
//                    mAdapter = new ContactGridviewAdapter(act, filteredTitles);
//                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new CurrentPollAdapter(mContext, itemList, 1);
                    mCurrentPollList.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dlg_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_textbox.setText("");
            }
        });
        dlg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryDialog.dismiss();
            }
        });

        mCategoryDialog.show();

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

}
