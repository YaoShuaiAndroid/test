package com.zb.bilateral.activity.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.google.gson.JsonArray;
import com.zb.bilateral.Constants;
import com.zb.bilateral.model.AnsweiListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.QuestionModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.QuestionListModel;
import com.zb.bilateral.mvp.AnswerQuestionPresenter;
import com.zb.bilateral.mvp.AnswerQuestionView;
import com.zb.bilateral.view.CommitDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/*
* 答题得分
* */
public class AnswerQuestionActivity extends BaseNewActivity<AnswerQuestionPresenter> implements AnswerQuestionView {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.answer_title)
    TextView answerTitle;
    @Bind(R.id.answerA)
    RadioButton answerA;
    @Bind(R.id.answerB)
    RadioButton answerB;
    @Bind(R.id.answerC)
    RadioButton answerC;
    @Bind(R.id.answerD)
    RadioButton answerD;
    @Bind(R.id.answer_total_item)
    TextView answerTotalItem;
    @Bind(R.id.answer_current_item)
    TextView answerCurrentItem;
    @Bind(R.id.radioGroupID)
    RadioGroup radioGroupID;
    @Bind(R.id.answer_next)
    TextView answerNext;


    private String museum_id;
    private int currentItem = 0;
    private int totalItem = 0;

    private final static int RESULT_QUESTION = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer_question;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        museum_id = getIntent().getStringExtra("museum_id");

        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("答题得分");
    }

    @Override
    protected void initData() {
        getAnswer();
    }

    /**
     * 答题-获取题目
     */
    public void getAnswer() {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.getAnswer(museum_id, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 提交答案
     */
    public void subAnswer(String answerData) {
        String token = AppUtil.getToken(mCctivity);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.subAnswer(answerData, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_rel, R.id.answer_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                setResult(RESULT_QUESTION);

                finish();
                break;
            case R.id.answer_next://下一题
                //设置用户选中的本题答案
                if (answerA.isChecked()) {
                    answeiListModels.getQueList().get(currentItem-1).setSelectAnswer("A");
                } else if (answerB.isChecked()) {
                    answeiListModels.getQueList().get(currentItem-1).setSelectAnswer("B");
                } else if (answerC.isChecked()) {
                    answeiListModels.getQueList().get(currentItem-1).setSelectAnswer("C");
                } else if (answerD.isChecked()) {
                    answeiListModels.getQueList().get(currentItem-1).setSelectAnswer("D");
                }

                if (totalItem != 0 && currentItem == totalItem) {
                    //最后一题提交答案
                    subAnswer(getAnswerData());
                } else if (totalItem != 0 && currentItem == (totalItem - 1)) {
                    //答完倒数第二题
                    currentItem++;

                    QuestionModel questionModel = answeiListModels.getQueList().get(currentItem-1);

                    answerCurrentItem.setText("" + currentItem);

                    answerA.setText(questionModel.getAnswerA());
                    answerB.setText(questionModel.getAnswerB());
                    answerC.setText(questionModel.getAnswerC());
                    answerD.setText(questionModel.getAnswerD());

                    answerTitle.setText(questionModel.getContent());

                    radioGroupID.clearCheck();

                    answerNext.setText("提交");
                } else if (totalItem != 0 && currentItem < totalItem) {
                    //答完题目
                    currentItem++;

                    QuestionModel questionModel = answeiListModels.getQueList().get(currentItem-1);

                    answerCurrentItem.setText("" + currentItem);

                    answerA.setText(questionModel.getAnswerA());
                    answerB.setText(questionModel.getAnswerB());
                    answerC.setText(questionModel.getAnswerC());
                    answerD.setText(questionModel.getAnswerD());

                    answerTitle.setText(questionModel.getContent());

                    radioGroupID.clearCheck();
                }
                break;
            default:
                break;
        }
    }

    public String getAnswerData() {
        JSONObject joAnswer = null;
        try {
            joAnswer = new JSONObject();
            JSONArray ja = new JSONArray();

            for (int i = 0; i < answeiListModels.getQueList().size(); i++) {
                JSONObject jo = new JSONObject();

                jo.put("answer", answeiListModels.getQueList().get(i).getSelectAnswer());
                jo.put("id", answeiListModels.getQueList().get(i).getId());
                ja.put(jo);
            }

            joAnswer.put("answerList", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return joAnswer.toString();
    }

    @Override
    protected AnswerQuestionPresenter createPresenter() {
        return new AnswerQuestionPresenter(this, mCctivity);
    }

    AnsweiListModel answeiListModels;

    @Override
    public void SendResultSuccess(AnsweiListModel answeiListModel) {
        if (answeiListModel.getQueList().size() > 0) {
            QuestionModel questionModel = answeiListModel.getQueList().get(0);

            currentItem = 1;
            totalItem = answeiListModel.getQueList().size();

            answeiListModels = answeiListModel;

            answerTotalItem.setText("" + totalItem);
            answerCurrentItem.setText("" + currentItem);

            answerA.setText(questionModel.getAnswerA());
            answerB.setText(questionModel.getAnswerB());
            answerC.setText(questionModel.getAnswerC());
            answerD.setText(questionModel.getAnswerD());

            answerTitle.setText(questionModel.getContent());

            if(answeiListModel.getQueList().size()==1){
                answerNext.setText("提交");
            }
        }
    }

    @Override
    public void SubmitResultSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity, "恭喜您，你将获得系统赠送给您的积分"+commitModel.getTotalScore()+"分。");
        finish();
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult(RESULT_QUESTION);

            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
