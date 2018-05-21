package com.zb.bilateral.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.ShowRoomModel;
import com.zb.bilateral.util.AppUtil;

import java.util.List;

/**
 * Created by yaos on 2017/8/2.
 */

public class NearCultrueDialog extends PopupWindow {
    Activity context;
    Handler handler;

    private final static int NEAR_HALL=11;

    private CommonAdapter commonAdapter;
    List<CultrueModel> cultrueModels;

    public NearCultrueDialog(Activity mContext, View parent, Handler handler, List<CultrueModel> cultrueModels) {
        this.handler=handler;
        this.context=mContext;
        this.cultrueModels=cultrueModels;

        View view = View
                .inflate(mContext, R.layout.dialog_near_hall_item, null);
        // 设置popWindow的显示和消失动画
        setAnimationStyle(R.style.anim_menu_bottombar);

        //添加activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 0.5f;
        context.getWindow().setAttributes(windowLP);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);

        // 实例化一个ColorDrawable颜色为半透明
       // ColorDrawable dw = new ColorDrawable(Color.parseColor("#90000000"));
        //setBackgroundDrawable(dw);
        setBackgroundDrawable(new BitmapDrawable());
        setContentView(view);
        if (Build.VERSION.SDK_INT == 24) {
            //在computeGravity()这个方法可以看出，在Android7.0系统上，调用PopupWindow.update( )方法会导致PopupWindow的位置出现在界面顶部
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
        }
        //实例化
        init(view);
    }

    public void init(View view) {
        RecyclerView near_hall_recy= (RecyclerView) view.findViewById(R.id.near_hall_recy);
        TextView near_hall_title= (TextView) view.findViewById(R.id.near_hall_title);
        near_hall_title.setText("附近文物");

        if(cultrueModels.size()>5){
            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) near_hall_recy.getLayoutParams();
            linearParams.height = AppUtil.dip2px(context,200);// 控件的宽强制设成30
            near_hall_recy.setLayoutParams(linearParams);
        }

        setRecy(near_hall_recy);
    }

    public void setRecy(RecyclerView mRecyclerView){
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        commonAdapter = new CommonAdapter<CultrueModel>(R.layout.list_near_cultrue_item,cultrueModels) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, CultrueModel item) {
                TextView near_cultrue_name = baseViewHolder.getView(R.id.near_cultrue_name);
                TextView near_cultrue_no = baseViewHolder.getView(R.id.near_cultrue_no);

                near_cultrue_name.setText(baseViewHolder.getPosition()+"、"+item.getName());
                near_cultrue_no.setText("编号:"+item.getNo());
            }
        };

        mRecyclerView.setAdapter(commonAdapter);

        commonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Message message=new Message();
                message.obj=cultrueModels.get(position);
                message.what=NEAR_HALL;
                handler.sendMessage(message);

                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //取消掉activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 1f;
        context.getWindow().setAttributes(windowLP);
    }
}
