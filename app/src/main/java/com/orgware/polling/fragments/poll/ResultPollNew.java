package com.orgware.polling.fragments.poll;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orgware.polling.R;
import com.orgware.polling.adapters.ChoicesListviewAdapter;
import com.orgware.polling.fragments.BaseFragment;
import com.orgware.polling.pojo.ChoicesItem;
import com.orgware.polling.utils.Methodutils;
import com.orgware.polling.utils.piegraph.PieGraph;
import com.orgware.polling.utils.piegraph.PieSlice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nandagopal on 3/2/16.
 */
public class ResultPollNew extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView mChoiceListviewOne, mChoiceListviewTwo, mChoiceListviewThree;
    ChoicesListviewAdapter mAdapterOne, mAdapterTwo, mAdapterThree;
    List<ChoicesItem> itemListOne = new ArrayList<>();
    List<ChoicesItem> itemListTwo = new ArrayList<>();
    List<ChoicesItem> itemListThree = new ArrayList<>();
    int qtsSize, mQtsIdOne, mQtsIdTwo, mQtsIdThree;
    String mQuestionOne, mQuestionTwo, mQuestionThree;
    JSONArray mChoiceArrayOne, mChoiceArrayTwo, mChoiceArrayThree, mSubmitValuesArray;
    LinearLayout mLayoutQtsOne, mLayoutQtsTwo, mLayoutQtsThree, mBtnSubmit;
    TextView mQuestionTitleOne, mQuestionTitleTwo, mQuestionTitleThree;
    //    FrameLayout mFrameLayoutOne, mFrameLayoutTwo, mFrameLayoutThree;
    LinearLayout mLayoutOne, mLayoutTwo, mLayoutThree, mLayoutSubmit;
    RelativeLayout mBackToLayout;
    Resources resources;
    PieGraph mPieOne, mPieTwo, mPieThree;
    private int mTotalCountOne, mTotalCountTwo, mTotalCountThree;
//    private CircularProg mQtsOneProgressOne, mQtsOneProgressTwo, mQtsOneProgressTh, mQtsTwoProgressOne, mQtsTwoProgressTwo, mQtsTwoProgressTh,
//            mQtsThProgressOne, mQtsThProgressTwo, mQtsThProgressTh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((HomeActivity) act).mSearchPollsTxt.setVisibility(View.GONE);
        qtsSize = preferences.getInt(RESULT_QUESTION_SIZE, 0);
//        if (qtsSize == 1)
//            setQtsOneContent();
//        else if (qtsSize == 2)
//            setQtsTwoContent();
//        else if (qtsSize == 3)
//            setQtsThreeContent();
//        else
//            Log.e("Result", "test");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_poll, container, false);
    }

    private void setPieGraph(PieGraph pg, final int mTotalCount, final int mTotal, final List<ChoicesItem> itemList) throws Exception {
        float testCount = 0;
        PieSlice slice;
        for (int i = 0; i < mTotalCount; i++) {
            if (itemList.get(i).mChoiceOptionId != 0) {
                slice = new PieSlice();
                slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                Log.e("Total count", "" + mTotalCount);
                Log.e("Test Count", itemList.get(i).mChoiceOptionId + " - " + itemList.get(i).mChoiceName + " - " + testCount);
                slice.setValue(testCount);
                slice.setTitle("first");
                pg.addSlice(slice);

            }

        }
        pg.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {

            @Override
            public void onClick(int position) {
                if (position != -1) {
                    float value = ((itemList.get(position).mChoiceOptionId * 100) / mTotal);
                    makeToast(" " + value + "%");
                }
            }
        });
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
        pg.setBackgroundBitmap(b);
        pg.setInnerCircleRatio(128);
        pg.setPadding(2);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        resources = getResources();
        mPieOne = (PieGraph) v.findViewById(R.id.piegraphOne);
        mPieTwo = (PieGraph) v.findViewById(R.id.piegraphTwo);
        mPieThree = (PieGraph) v.findViewById(R.id.piegraphThree);
        (mChoiceListviewOne = (ListView) v.findViewById(R.id.choicesListviewOne)).setOnItemClickListener(this);
        (mChoiceListviewTwo = (ListView) v.findViewById(R.id.choicesListviewTwo)).setOnItemClickListener(this);
        (mChoiceListviewThree = (ListView) v.findViewById(R.id.choicesListviewThree)).setOnItemClickListener(this);
        mBackToLayout = (RelativeLayout) v.findViewById(R.id.layout_statics_back);
        mBackToLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.getSupportFragmentManager().popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                makeToast("Test");
            }
        });
        (mLayoutSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        (mBtnSubmit = (LinearLayout) v.findViewById(R.id.layout_survey_detail_submit)).setOnClickListener(this);
        mLayoutQtsOne = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_one);
        mLayoutQtsTwo = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_two);
        mLayoutQtsThree = (LinearLayout) v.findViewById(R.id.layout_survey_detail_qts_three);
        mQuestionTitleOne = (TextView) v.findViewById(R.id.qts_survey_detail_one);
        mQuestionTitleTwo = (TextView) v.findViewById(R.id.qts_survey_detail_two);
        mQuestionTitleThree = (TextView) v.findViewById(R.id.qts_survey_detail_three);
        mLayoutSubmit.setVisibility(View.GONE);
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (qtsSize == 1) {
//            setQtsOneContent();
            try {
//                setPieGraph(mPieOne, preferences.getInt("mTotalCountOne", 0), preferences.getInt("mTotalOne", 0), itemListOne);
                setQtsOneContent(mPieOne);


                mChoiceListviewOne.setAdapter(mAdapterOne);
                mQuestionTitleOne.setText(mQuestionOne);
                mLayoutQtsTwo.setVisibility(View.GONE);
                mLayoutQtsThree.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (qtsSize == 2) {
//            setQtsTwoContent();
            try {
//                setPieGraph(mPieOne, preferences.getInt("mTotalCountOne", 0), preferences.getInt("mTotalOne", 0), itemListOne);
//                setPieGraph(mPieTwo, preferences.getInt("mTotalCountTwo", 0), preferences.getInt("mTotalTwo", 0), itemListTwo);
                setQtsTwoContent(mPieOne, mPieTwo);
                mChoiceListviewOne.setAdapter(mAdapterOne);
                mChoiceListviewTwo.setAdapter(mAdapterTwo);
                mQuestionTitleOne.setText(mQuestionOne);
                mQuestionTitleTwo.setText(mQuestionTwo);
                mLayoutQtsThree.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (qtsSize == 3) {
//            setQtsThreeContent();
            try {
//                setPieGraph(mPieOne, preferences.getInt("mTotalCountOne", 0), preferences.getInt("mTotalOne", 0), itemListOne);
//                setPieGraph(mPieTwo, preferences.getInt("mTotalCountTwo", 0), preferences.getInt("mTotalTwo", 0), itemListTwo);
//                setPieGraph(mPieThree, preferences.getInt("mTotalCountThree", 0), preferences.getInt("mTotalThree", 0), itemListThree);
                setQtsThreeContent(mPieOne, mPieTwo, mPieThree);
                mChoiceListviewOne.setAdapter(mAdapterOne);
                mChoiceListviewTwo.setAdapter(mAdapterTwo);
                mChoiceListviewThree.setAdapter(mAdapterThree);
                mQuestionTitleOne.setText(mQuestionOne);
                mQuestionTitleTwo.setText(mQuestionTwo);
                mQuestionTitleThree.setText(mQuestionThree);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


//    private void setResultsWithPieChart(JSONArray mQtsListArray) {
//        try {
//            for (int i = 0; i < mQtsListArray.length(); i++) {
//                JSONObject mChoicesObject = mQtsListArray.getJSONObject(i);
//                JSONObject mTotalCountObject = mChoicesObject.optJSONObject("totalCount");
//                JSONArray mChoicesArray = mChoicesObject.optJSONArray("choices");
//
////                for (int j = 0; j < mChoicesArray.length(); j++) {
////
////                }
//
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private void setQtsOneContent(PieGraph pieGraphOne) {
        try {
            PieSlice slice;
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            itemListOne.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                final JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount"), mChoiceObject.optString("percentage")));
                mTotalCountOne = mTotalCountOne + itemListOne.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName + " - One1 -" + mTotalCountOne);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphOne.addSlice(slice);
                    pieGraphOne.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
                        @Override
                        public void onClick(int position) {
                            makeToast("" + itemListOne.get(position).mChoicePercent);
                        }
                    });
                }
            }
            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalOne", mTotalCountOne).commit();
            mAdapterOne = new ChoicesListviewAdapter(act, R.layout.item_choices, itemListOne, 2);
//            editor.putInt(RES_COUNT_0, itemListOne.get(0).mChoiceOptionId);
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
            pieGraphOne.setBackgroundBitmap(b);
            pieGraphOne.setInnerCircleRatio(128);
            pieGraphOne.setPadding(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsTwoContent(PieGraph pieGraphOne, PieGraph pieGraphTwo) {
        try {
            PieSlice slice;
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mChoiceArrayTwo = new JSONArray("" + preferences.getString("RES_CHOICE_1", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            mQuestionTwo = preferences.getString("RES_QUESTION_1", "");
            itemListOne.clear();
            itemListTwo.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                mTotalCountOne = mTotalCountOne + itemListOne.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName + " - One2 -" + mTotalCountOne);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphOne.addSlice(slice);
                    pieGraphOne.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
                        @Override
                        public void onClick(int position) {
                            makeToast("" + itemListOne.get(position).mChoicePercent);
                        }
                    });
                }
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                mTotalCountTwo = mTotalCountTwo + itemListTwo.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName + " - Two2 - " + mTotalCountTwo);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphTwo.addSlice(slice);
                    pieGraphTwo.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
                        @Override
                        public void onClick(int position) {
                            makeToast("" + itemListTwo.get(position).mChoicePercent);
                        }
                    });
                }
            }
            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalOne", mTotalCountOne).
                    putInt("mTotalCountTwo", mChoiceArrayTwo.length()).putInt("mTotalTwo", mTotalCountTwo).commit();
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne, 2);
            mAdapterTwo = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListTwo, 2);
//            editor.putInt(RES_COUNT_0, itemListTwo.get(0).mChoiceOptionId);
//            editor.putInt(RES_COUNT_1, itemListTwo.get(1).mChoiceOptionId);
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
            pieGraphOne.setBackgroundBitmap(b);
            pieGraphOne.setInnerCircleRatio(128);
            pieGraphOne.setPadding(2);
            pieGraphTwo.setBackgroundBitmap(b);
            pieGraphTwo.setInnerCircleRatio(128);
            pieGraphTwo.setPadding(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setQtsThreeContent(PieGraph pieGraphOne, PieGraph pieGraphTwo, PieGraph pieGraphThree) {
        try {
            PieSlice slice;
            mChoiceArrayOne = new JSONArray("" + preferences.getString("RES_CHOICE_0", ""));
            mChoiceArrayTwo = new JSONArray("" + preferences.getString("RES_CHOICE_1", ""));
            mChoiceArrayThree = new JSONArray("" + preferences.getString("RES_CHOICE_2", ""));
            mQuestionOne = preferences.getString("RES_QUESTION_0", "");
            mQuestionTwo = preferences.getString("RES_QUESTION_1", "");
            mQuestionThree = preferences.getString("RES_QUESTION_2", "");
            itemListOne.clear();
            itemListTwo.clear();
            itemListThree.clear();
            for (int i = 0; i < mChoiceArrayOne.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayOne.optJSONObject(i);
                itemListOne.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                mTotalCountOne = mTotalCountOne + itemListOne.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListOne.get(i).mChoiceName + " - One3 -" + mTotalCountOne);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphOne.addSlice(slice);
                    pieGraphOne.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
                        @Override
                        public void onClick(int position) {

                        }
                    });
                }
            }
            for (int i = 0; i < mChoiceArrayTwo.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayTwo.optJSONObject(i);
                itemListTwo.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                mTotalCountTwo = mTotalCountTwo + itemListTwo.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListTwo.get(i).mChoiceName + " - Two3 -" + mTotalCountThree);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphTwo.addSlice(slice);
                    pieGraphTwo.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
                        @Override
                        public void onClick(int position) {

                        }
                    });
                }
            }
            for (int i = 0; i < mChoiceArrayThree.length(); i++) {
                JSONObject mChoiceObject = mChoiceArrayThree.optJSONObject(i);
                itemListThree.add(new ChoicesItem("" + mChoiceObject.optString("choice"), mChoiceObject.optInt("resultCount")));
                mTotalCountThree = mTotalCountThree + itemListThree.get(i).mChoiceOptionId;
                Log.e("Qts One", "" + itemListThree.get(i).mChoiceName + " - Three3 -" + mTotalCountThree);
                if (!mChoiceObject.optString("percentage").equals("0 %")) {
                    slice = new PieSlice();
                    slice.setColor(resources.getColor(Methodutils.mResultColor[i]));
//                testCount = ((itemList.get(i).mChoiceOptionId * 100) / mTotal);
                    String percentValue = mChoiceObject.optString("percentage");
                    percentValue = percentValue.replace("%", "");
                    slice.setValue(Float.parseFloat(percentValue));
                    slice.setTitle("first");
                    pieGraphThree.addSlice(slice);
                    pieGraphThree.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {
                        @Override
                        public void onClick(int position) {

                        }
                    });
                }
            }
//            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalCountTwo", mChoiceArrayTwo.length()).putInt("mTotalCountThree", mChoiceArrayThree.length()).commit();
            editor.putInt("mTotalCountOne", mChoiceArrayOne.length()).putInt("mTotalOne", mTotalCountOne).
                    putInt("mTotalCountTwo", mChoiceArrayTwo.length()).putInt("mTotalTwo", mTotalCountTwo).
                    putInt("mTotalCountThree", mChoiceArrayThree.length()).putInt("mTotalThree", mTotalCountThree).commit();
            mAdapterOne = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListOne, 2);
            mAdapterTwo = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListTwo, 2);
            mAdapterThree = new ChoicesListviewAdapter(act, android.R.layout.simple_list_item_single_choice, itemListThree, 2);
//            editor.putInt(RES_COUNT_0, itemListThree.get(0).mChoiceOptionId);
//            editor.putInt(RES_COUNT_1, itemListThree.get(1).mChoiceOptionId);
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_icon);
            pieGraphOne.setBackgroundBitmap(b);
            pieGraphOne.setInnerCircleRatio(128);
            pieGraphOne.setPadding(2);
            pieGraphTwo.setBackgroundBitmap(b);
            pieGraphTwo.setInnerCircleRatio(128);
            pieGraphTwo.setPadding(2);
            pieGraphThree.setBackgroundBitmap(b);
            pieGraphThree.setInnerCircleRatio(128);
            pieGraphThree.setPadding(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Animator.AnimatorListener getAnimationListener() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            return new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("piefrag", "anim end");
                }

                @Override
                public void onAnimationCancel(Animator animation) {//you might want to call slice.setvalue(slice.getGoalValue)
                    Log.d("piefrag", "anim cancel");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        else return null;

    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
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
        switch (view.getId()) {
            case R.id.choicesListviewOne:
                editor.putInt(OPTION_ONE, itemListOne.get(position).mChoiceOptionId).commit();
                break;
            case R.id.choicesListviewTwo:
                editor.putInt(OPTION_TWO, itemListOne.get(position).mChoiceOptionId).commit();
                break;
            case R.id.choicesListviewThree:
                editor.putInt(OPTION_THREE, itemListOne.get(position).mChoiceOptionId).commit();
                break;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}

