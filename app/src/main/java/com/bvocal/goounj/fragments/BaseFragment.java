package com.bvocal.goounj.fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.bvocal.goounj.R;
import com.bvocal.goounj.adapters.ContactGridviewAdapter;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.pojo.ContactItem;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


/*This is the Parent fragment for all the fragment classes. */
public abstract class BaseFragment extends Fragment implements Appinterface {

    public FragmentActivity act;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    protected View mParentView;
    RecyclerView mRecyclerView;
    ContactGridviewAdapter mAdapter;
    CheckBox mSelectAll;
    List<ContactItem> contactItemList = new ArrayList<>();
    Dialog mContactDialog;
    GridLayoutManager gridLayoutManager;
    Button mContactsDone, mCreatedList;
    EditText inputSearch;
    LayoutInflater inflater;
//    private Typeface typeface;

    /*This sets the title as per fragment call and it is reused method*/
    public abstract void setTitle();

    /*This called first in this fragment*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = (FragmentActivity) getActivity();
        preferences = act.getSharedPreferences(SHARED_PREFERENCES_POLLING, 0);
        editor = preferences.edit();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        typeface = Typeface.createFromAsset(act.getAssets(), "LATO_LIGHT_0.TTF");
        setHasOptionsMenu(true);
    }

    /*This calls after view is created*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentView = view;
    }

    /*This is called after restart the current class*/
    @Override
    public void onStart() {
        super.onStart();
//        if (mParentView != null)
//            changeTypeface((ViewGroup) mParentView);
    }

    /*This resumes the fragment*/
    @Override
    public void onResume() {
        super.onResume();
        setTitle();

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

    /*This method is used to call the new Fragment and sets into the Frame Layout */
    public void setNewFragment(Fragment fragment, int fragment_content, String title,
                               boolean addbackstack) {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slideout2, R.anim.slidein2,
                R.anim.slidein, R.anim.slideout);

        transaction.replace(fragment_content, fragment);
        if (addbackstack)
            transaction.addToBackStack(title);
        transaction.commit();

    }


    public void makeToast(String value) {
        Toast.makeText(act, " " + value, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    /*This creates the font style*/
//    protected void changeTypeface(ViewGroup vGroup) {
//        for (int i = 0; i < vGroup.getChildCount(); i++) {
//            View v = vGroup.getChildAt(i);
//            if (v instanceof TextView) {
//                ((TextView) v).setTypeface(typeface);
//            } else if (v instanceof RadioButton) {
//                ((RadioButton) v).setTypeface(typeface);
//            } else if (v instanceof DrawerLayout || v instanceof FrameLayout
//                    || v instanceof LinearLayout || v instanceof RelativeLayout
//                    || v instanceof RadioGroup || v instanceof ListView || v instanceof RecyclerView || v instanceof Menu) {
//                changeTypeface((ViewGroup) v);
//            }
//        }
//    }

    public String splitFromString(String stringName) {

        try {
            SimpleDateFormat mRequiredDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = mSimpleDateFormat.parse(stringName);

            return mRequiredDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String splitOnlyDateFromString(String stringName) {

        try {
            SimpleDateFormat mRequiredDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date date = mSimpleDateFormat.parse(stringName);

            return mRequiredDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String splitDateFromString(String stringName) {
        StringBuilder sb = new StringBuilder();
        String unwanted, wanted, wantedOne, wantedTwo, unWantedone;
        StringTokenizer tokenize = new StringTokenizer(stringName, "."); //2016-01-28T08:30:29.000Z
        wanted = "" + tokenize.nextToken();
        unwanted = "" + tokenize.nextToken();
        StringTokenizer tokenizer = new StringTokenizer(wanted, "T"); //2016-01-28T08:30:29
        String values = tokenizer.nextToken();
        String str_date = "13-09-2011";
        Date date = null;
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("MMM dd");
            date = (Date) formatter.parse(values);
            System.out.println("Today is " + date.getTime());
            Log.e("Date", "" + date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        StringTokenizer tokenizer_1 = new StringTokenizer(values, "-");
//        String year = tokenizer_1.nextToken();
//        String month = tokenizer_1.nextToken();
//        String day = tokenizer_1.nextToken();
//        String date = "" + Methodutils.mMonthArray[Integer.parseInt(month)] + " " + day;
//        Log.e("MyDate", month + " - " + day);
        return date.toString();
    }

    public List<ContactItem> getNumber(ContentResolver cr, List<ContactItem> contactList) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

//            if (image_uri != null) {
//                image.setImageURI(Uri.parse(image_uri));
//            }
            System.out.println(name + ".................." + phoneNumber + "-------" + image_uri);
            contactList.add(new ContactItem(name, phoneNumber));
        }
        phones.close();// close cursor
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,aa);
//        lv.setAdapter(adapter);
        //display contact numbers in the list
        return contactList;
    }

    private JSONArray addContactJson(JSONArray mContactsNumbersArrray, JSONArray mContactsNamesArrray) {
//        String data = "";
//        mContactsNumbersArrray = null;
        List<ContactItem> stList = ((ContactGridviewAdapter) mAdapter)
                .getStudentist();
        try {
            for (int i = 0; i < stList.size(); i++) {
                ContactItem singleStudent = stList.get(i);
                if (singleStudent.mChkSelected == true) {
//                data = data + "\n" + singleStudent.mContactNumber.toString();
                    mContactsNumbersArrray.put(singleStudent.mContactNumber);
                    mContactsNamesArrray.put(singleStudent.mcontactName);
                    Log.e("Contact", "-" + singleStudent.mcontactName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        editor.putString(CONTACT_ARRAY, mContactObject.toString()).commit();
        return mContactsNumbersArrray;
    }

    public JSONArray showContactDialog(final JSONArray mJsonArray, final JSONArray mJsonArrayNames) {
        mContactDialog = new Dialog(act);
        mContactDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContactDialog.setCancelable(true);
        mContactDialog.setContentView(R.layout.fragment_contacts);
        gridLayoutManager = new GridLayoutManager(act, 3);
        mRecyclerView = (RecyclerView) mContactDialog.findViewById(R.id.gridListview);
        inputSearch = (EditText) mContactDialog.findViewById(R.id.inputSearch);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        contactItemList.clear();
        getNumber(act.getContentResolver(), contactItemList);
        Collections.sort(contactItemList, new Comparator<ContactItem>() {
            public int compare(ContactItem s1, ContactItem s2) {
                return s1.mcontactName.compareToIgnoreCase(s2.mcontactName);
            }
        });
//inputSearch.setFilters(new InputFilter[]);
        mAdapter = new ContactGridviewAdapter(act, contactItemList);
        mRecyclerView.setAdapter(mAdapter);
        (mCreatedList = (Button) mContactDialog.findViewById(R.id.btn_created_list)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        (mContactsDone = (Button) mContactDialog.findViewById(R.id.btnContacts_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactJson(mJsonArray, mJsonArrayNames);
                mContactDialog.dismiss();
            }
        });
        (mSelectAll = (CheckBox) mContactDialog.findViewById(R.id.btnCheckedAll)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    mAdapter.selectAllList();
                else
                    mAdapter.deSelectAllList();
            }
        });
//        mContactAdapter = new ContactsAdapter(act, getModel());
//        mListview.setAdapter(mContactAdapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    List<ContactItem> filteredTitles = new ArrayList<ContactItem>();
                    for (int i = 0; i < contactItemList.size(); i++) {
                        if (contactItemList.get(i).mcontactName.toString().toLowerCase().contains(s) ||
                                contactItemList.get(i).mcontactName.toString().toUpperCase().contains(s) ||
                                contactItemList.get(i).mcontactName.toString().contains(s)) {
                            filteredTitles.add(contactItemList.get(i));
                        }
                    }
                    mAdapter = new ContactGridviewAdapter(act, filteredTitles);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new ContactGridviewAdapter(act, contactItemList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mContactDialog.show();
        return mJsonArray;
    }

    /*This used to pass the fragment with parameter instead of constructor*/
    Fragment setFArgs(Fragment f, String... param) {
        Bundle args = new Bundle();
        for (int i = 0; i < param.length; i += 2) {
            args.putString(param[i], param[i + 1]);
        }
        f.setArguments(args);
        return f;
    }

    // Fragment setPageFragment(Fragment fragment, String title) {
    // Bundle b = new Bundle();
    // b.putString("title", title);
    // fragment.setArguments(b);
    // return fragment;
    // }

}
