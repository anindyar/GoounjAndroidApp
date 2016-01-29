package com.orgware.polling.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.orgware.polling.R;
import com.orgware.polling.adapters.ContactGridviewAdapter;
import com.orgware.polling.adapters.ContactsAdapter;
import com.orgware.polling.pojo.ContactItem;
import com.orgware.polling.pojo.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nandagopal on 25/10/15.
 */
public class ContactGrid extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    RecyclerView mRecyclerView;
    ContactGridviewAdapter mAdapter;

    ContactsAdapter mContactAdapter;
    List<Model> list = new ArrayList<>();
    List<ContactItem> contactItemList = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    CheckBox mSelectAll;
    JSONArray mContactsNumbersArrray = new JSONArray();
    Button mContactsDone, mCreatedList;
    int max_pic_contact = 10;
    EditText inputSearch;

    @Override
    public void setTitle() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        gridLayoutManager = new GridLayoutManager(act, 3);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.gridListview);
        inputSearch = (EditText) v.findViewById(R.id.inputSearch);
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
        (mCreatedList = (Button) v.findViewById(R.id.btn_created_list)).setOnClickListener(this);
        (mContactsDone = (Button) v.findViewById(R.id.btnContacts_done)).setOnClickListener(this);
        (mSelectAll = (CheckBox) v.findViewById(R.id.btnCheckedAll)).setOnClickListener(this);
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

        return v;
    }

    private void launchMultiplePhonePicker() {

        try {
            Intent phonebookIntent = new Intent("intent.action.INTERACTION_TOPMENU");
            phonebookIntent.putExtra("additional", "phone-multi");
            startActivityForResult(phonebookIntent, REQUEST_CODE_PICK_CONTACT);
            // PICK_CONTACT IS JUST AN INT HOLDING SOME NUMBER OF YOUR CHOICE

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getValues() {

        StringBuffer responseText = new StringBuffer();
        List<Model> countryList = mContactAdapter.list;
        for (int i = 0; i < countryList.size(); i++) {
            Model country = countryList.get(i);
            if (country.isSelected())
                responseText.append("\n" + country.getName());
        }
        Toast.makeText(act, "" + responseText.toString(), Toast.LENGTH_SHORT).show();
    }

    public String getData(String contact, int which) {
        return contact.split(";")[which];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final int URI = 0;
        final int NUMBER = 1;

        if (act.RESULT_OK != resultCode) return;
        Bundle contactUri = data.getExtras();
        if (null == contactUri) return;

        ArrayList<String> contacts = (ArrayList<String>) contactUri.get("result");
        for (int i = 0; i < contacts.size(); i++) {
            Toast.makeText(act, getData(contacts.get(i), NUMBER), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        makeToast("" + position);
    }


    private List<Model> getModel() {
        for (int i = 0; i < 30; i++) {
            list.add(new Model("Linux - " + i));
        }
        return list;
    }

    private List<String> contactsFromCursor() {
        Cursor cursor = null;
        List<String> contacts = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            } while (cursor.moveToNext());
        }

        return contacts;
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


    private void getContactsDetails() {

        Cursor phones = act.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {
            String Name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String Number = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            System.out.println("Contact1 : " + Name + ", Number " + Number
                    + ", image_uri " + image_uri);


//            if (image_uri != null) {
//                image.setImageURI(Uri.parse(image_uri));
//            }


        }
    }

    private void test() {
        String data = "";
        JSONObject mContactObject = new JSONObject();
        List<ContactItem> stList = ((ContactGridviewAdapter) mAdapter)
                .getStudentist();
        for (int i = 0; i < stList.size(); i++) {
            ContactItem singleStudent = stList.get(i);
            if (singleStudent.mChkSelected == true) {
                data = data + "\n" + singleStudent.mContactNumber.toString();
                mContactsNumbersArrray.put(singleStudent.mContactNumber.toString());
            }
        }
        try {
            mContactObject.put("audience", mContactsNumbersArrray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putString(CONTACT_ARRAY, mContactObject.toString()).commit();
        Log.e("Contact Array", "" + mContactObject.toString());
//        Toast.makeText(act,
//                "Selected Students: \n" + data, Toast.LENGTH_LONG)
//                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContacts_done:
                test();
//                Bundle b=new Bundle();
//                String s=setArguments("Contact");
                act.getSupportFragmentManager().popBackStack();
                break;
//            case R.id.btn_contacts_back:
//                act.getSupportFragmentManager().popBackStack();
//                break;
        }
    }
}
