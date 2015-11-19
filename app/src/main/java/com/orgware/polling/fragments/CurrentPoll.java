package com.orgware.polling.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.orgware.polling.HomeActivity;
import com.orgware.polling.R;
import com.orgware.polling.adapters.CurrentPollAdapter;
import com.orgware.polling.database.GoounjDatabase;
import com.orgware.polling.interfaces.RestApiListener;
import com.orgware.polling.network.NetworkHelper;
import com.orgware.polling.network.RestApiProcessor;
import com.orgware.polling.pojo.CurrentPollItem;
import com.orgware.polling.utils.Methodutils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 26/10/15.
 */
public class CurrentPoll extends BaseFragment implements AdapterView.OnItemClickListener {

    RecyclerView mCurrentPollList;
    CurrentPollAdapter mAdapter;
    List<CurrentPollItem> itemList;
    List<CurrentPollItem> singleItemList;
    int limit = 15, dashboardId;
    GoounjDatabase db;
    boolean currentPollService;

    @Override
    public void setTitle() {
//        ((HomeActivity)act).mPageTitle.setText("Current Poll");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new GoounjDatabase(act);
        itemList = new ArrayList<>();
        singleItemList = new ArrayList<>();
        dashboardId = preferences.getInt(DASHBOARD_ID, 0);
        Log.e("DashBoard", "" + dashboardId);
//        mAdapter = new CurrentPollAdapter(act, itemList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_poll_listview, container, false);
        mCurrentPollList = (RecyclerView) v.findViewById(R.id.currentPollListview);
        mCurrentPollList.setLayoutManager(new LinearLayoutManager(act));
//        db = new GoounjDatabase(act);
        currentPollService = preferences.getBoolean(CURRENT_POLLDB, true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (preferences.getInt(DASHBOARD_ID, 0) == 1)
            ((HomeActivity) act).mPageTitle.setText("Survey Poll");
        else
            ((HomeActivity) act).mPageTitle.setText("Current Poll");
        if (NetworkHelper.checkActiveInternet(act))
            getPollForCreatedUser(BASE_URL + SHOW_POLL_FOR_AUDIENCE);
        else
            Methodutils.messageWithTitle(act, "No Internet connection", "Please check your internet connection", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.getSupportFragmentManager().popBackStack();
                    return;
                }
            });

//        getPollForCreatedUser("http://192.168.0.112:3000/polls/v1/pollList");
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
//        editor.putInt(QUESTION_SIZE, 3).commit();
//        ((HomeActivity) act).setNewFragment(new CurrentPollPager(), "Current Poll Pager", true);
        editor.putInt(POLL_ID, itemList.get(position).currentPollId).putString(POLL_NAME, "" + itemList.get(position).mCurrentPollTitle).putString(CURRENT_CREATED_USER_NAME, "" + itemList.get(position).mCreatedUserName).commit();
//        makeToast("Id- " + itemList.get(position).currentPollId + " Name- " + itemList.get(position).mCurrentPollTitle + " By- " + itemList.get(position).mCreatedUserName);
//        getPollDetailPage("http://192.168.0.112:3000/polls/v1/poll/3");
        getPollDetailPage(BASE_URL + SHOW_POLL_URL + itemList.get(position).currentPollId);
    }

    private String showPollParams() {
        JSONObject mShowPollOnject = new JSONObject();
        try {
            mShowPollOnject.put(USER_ID, "" + preferences.getString(USER_ID, ""));
//            mShowPollOnject.put(USER_ID, "3");
            mShowPollOnject.put(POLL_LIMIT, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mShowPollOnject.toString();
    }

    private void getPollForCreatedUser(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.POST, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                Log.e("Poll List Response", "" + response.toString());
                if (response.equals("[]"))
                    makeToast("No records found");
                try {
                    showPollList(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFailed(Exception e) {
                if (e == null) {
                    Log.e("Error", "" + e.getMessage());
                    Methodutils.message(act, "Internal Server Error. Requested Action Failed", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            act.getSupportFragmentManager().popBackStack();
                        }
                    });
                } else {
                    if (e.getMessage() == null)
                        Methodutils.message(act, "No Records Found", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                act.getSupportFragmentManager().popBackStack();
                            }
                        });
                    else
                        Methodutils.message(act, "Try again, Failed to connect to server", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                act.getSupportFragmentManager().popBackStack();
                            }
                        });

                }
            }
        });
        processor.execute(showPollParams().toString());
    }

    public void showPollList(String response) {
        try {
//            JSONObject object = new JSONObject(response);
//            JSONArray objectArray = object.optJSONArray(response);
            JSONArray objectArray = new JSONArray(response);
            itemList.clear();
            if (preferences.getInt(DASHBOARD_ID, 0) == 0) {
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject objectPolls = objectArray.optJSONObject(i);
                    Log.e("Array Values", "" + i);
                    if (objectPolls.optString("isAnswered").equals("0")) {
                        itemList.add(new CurrentPollItem(objectPolls.optInt("pollId"), objectPolls.optString("startDate"), objectPolls.optString("endDate"),
                                objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
                    }
                }
            }
            if (preferences.getInt(DASHBOARD_ID, 0) == 1) {
                for (int i = 0; i < objectArray.length(); i++) {
                    JSONObject objectPolls = objectArray.optJSONObject(i);
                    Log.e("Array Values", "" + i);
                    itemList.add(new CurrentPollItem(objectPolls.optInt("pollId"), objectPolls.optString("startDate"), objectPolls.optString("endDate"),
                            objectPolls.optString("pollName"), objectPolls.optInt("isBoost"), objectPolls.optString("createdUserName")));
                }
            }
            refreshCurrentPollListview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCurrentPollListview() {
        mAdapter = new CurrentPollAdapter(act, itemList);
        mAdapter.setOnItemClickListener(this);
        mCurrentPollList.setAdapter(mAdapter);
    }

    private void getPollDetailPage(String url) {
        RestApiProcessor processor = new RestApiProcessor(act, RestApiProcessor.HttpMethod.GET, url, true, true, new RestApiListener<String>() {
            @Override
            public void onRequestCompleted(String response) {
                savePollDetailResponse(response);
            }

            @Override
            public void onRequestFailed(Exception e) {
                Methodutils.messageWithTitle(act, "Failed", "" + e.getMessage(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        act.getSupportFragmentManager().popBackStack();
                    }
                });
            }
        });
        processor.execute();
    }

    private void savePollDetailResponse(String result) {
        try {
            JSONObject mPollDetailObject = new JSONObject(result);
            JSONArray mQuestionsArray = mPollDetailObject.optJSONArray(ANS_QUESTIONLIST);
            editor.putInt(QUESTION_SIZE, mQuestionsArray.length()).commit();
            if (mQuestionsArray.length() != 0)
                for (int j = 0; j < mQuestionsArray.length(); j++) {
                    JSONObject mQuestions = mQuestionsArray.optJSONObject(j);
                    JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                    Log.e("Choice Array - " + j, "" + mChoicesArray.toString());

                    editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putInt("QUESTION_ID_" + j, mQuestions.optInt(QUESTION_ID)).
                            putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                }
//                setQuestions(mQuestionsArray.length(), mQuestionsArray);
            else
                makeToast("Sorry,No Questions to answer!");
            if (dashboardId == 0)
                ((HomeActivity) act).setNewFragment(new CurrentPollPager(), "Pager", true);
            else if (dashboardId == 1)
                ((HomeActivity) act).setNewFragment(new SurveyDetail(), "Pager", true);
            else
                Log.e("No DashBoard", "" + dashboardId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQuestions(int questionLength, JSONArray mQuestionssArray) {
        if (questionLength == 1) {
            for (int j = 0; j < mQuestionssArray.length(); j++) {
                JSONObject mQuestions = mQuestionssArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());

                editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putInt("QUESTION_ID_" + j, mQuestions.optInt(QUESTION_ID)).
                        putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
//                        .putInt(CHOICE_SIZE_1, mChoicesArray.length()).commit();
//                setChoices(j, mChoicesArray.length(), mChoicesArray);
            }
        }
        if (questionLength == 2) {
            for (int j = 0; j < mQuestionssArray.length(); j++) {
                JSONObject mQuestions = mQuestionssArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
                editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                Log.e("QUESTION_SIZE_" + j, "" + preferences.getString("QUESTION_SIZE_" + j, ""));
//                setChoices(j, mChoicesArray.length(), mChoicesArray);
            }
        }
        if (questionLength == 3) {
            for (int j = 0; j < mQuestionssArray.length(); j++) {
                editor.putString("QUESTION_SIZE_" + j, "" + mQuestionssArray.optString(j)).commit();
                JSONObject mQuestions = mQuestionssArray.optJSONObject(j);
                JSONArray mChoicesArray = mQuestions.optJSONArray(CHOICES);
                editor.putString("QUESTION_SIZE_" + j, "" + mQuestions.optString(QUESTION)).putString("CHOICE_" + j, "" + mChoicesArray.toString()).commit();
                Log.e("Choice Array - " + j, "" + mChoicesArray.toString());
//                editor.putInt(QUESTION_SIZE, 1).putInt(CHOICE_1, mChoicesArray.length()).commit();
//                setChoices(j, mChoicesArray.length(), mChoicesArray);
            }
        }
    }

    private void setChoices(int i, int mChoicesLength, JSONArray mChoicesArray) {
        editor.putInt("CHOICE_SIZE_" + i, mChoicesLength).commit();
        Log.e("Choice Size - " + i, "" + mChoicesLength);
        if (mChoicesLength == 1) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                Log.e("Choice Object - " + i, "" + mChoiceObject.toString());
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();

            }
        }
        if (mChoicesLength == 2) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                Log.e("Choice Object - " + i, "" + mChoiceObject.toString());
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
        if (mChoicesLength == 3) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
                Log.e("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                Log.e("Choice Object - " + i, "" + mChoiceObject.toString());
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
        if (mChoicesLength == 4) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
//                Log.e("" + j, "" + mChoicesArray.optString(j));
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
        if (mChoicesLength == 5) {
            for (int j = 0; j < mChoicesArray.length(); j++) {
//                Log.e("" + j, "" + mChoicesArray.optString(j));
//                editor.putString("CHOICE_" + i + "_" + j, "" + mChoicesArray.optString(j));
                JSONObject mChoiceObject = mChoicesArray.optJSONObject(j);
                editor.putString("CHOICE_" + i + "_" + j, "" + mChoiceObject.optString("choice")).
                        putInt("OPTION_" + i + "_" + j, mChoiceObject.optInt("optionId")).commit();
            }
        }
    }

}
