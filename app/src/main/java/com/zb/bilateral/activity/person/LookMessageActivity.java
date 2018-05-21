package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.Question;
import com.zb.bilateral.model.QuestionListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class LookMessageActivity extends BaseNewActivity<PublicPresenter<QuestionListModel>> implements PublicView<QuestionListModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.look_message_recy)
    RecyclerView mRecyclerView;
    @Bind(R.id.look_noread_message_recy)
    RecyclerView mNoMessagaeRecyclerView;

    private CommonAdapter commonAdapter,commonNoMessageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_look_message;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("回复");
    }

    @Override
    protected void initData() {
        reply();
    }

    /**
     * 回复
     */
    public void reply() {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.reply(token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    public void set_recy(QuestionListModel questionListModel) {
        mNoMessagaeRecyclerView.setHasFixedSize(true);
        mNoMessagaeRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity, LinearLayoutManager.VERTICAL, false);
        mNoMessagaeRecyclerView.setLayoutManager(linearLayoutManager);

        commonNoMessageAdapter = new CommonAdapter<Question>(R.layout.list_message_noread_item, questionListModel.getNotReplyList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, Question item) {
                TextView look_message_reply=baseViewHolder.getView(R.id.list_noread_text);

                look_message_reply.setText(item.getQuestion());
            }
        };

        mNoMessagaeRecyclerView.setAdapter(commonNoMessageAdapter);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mCctivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager1);

        commonAdapter = new CommonAdapter<Question>(R.layout.list_look_message_item, questionListModel.getReplyList()) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, Question item) {
                TextView look_message_reply=baseViewHolder.getView(R.id.look_message_reply);
                TextView look_message_reply_question=baseViewHolder.getView(R.id.look_message_reply_question);

                look_message_reply.setText("回复："+item.getContent());
                look_message_reply_question.setText(item.getQuestion());

                SpannableStringBuilder builder = new SpannableStringBuilder(look_message_reply.getText().toString());

                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#FFAA25"));

                builder.setSpan(redSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                look_message_reply.setText(builder);
            }
        };

        mRecyclerView.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    @OnClick({R.id.top_left_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(QuestionListModel questionListModel) {
        set_recy(questionListModel);
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}
