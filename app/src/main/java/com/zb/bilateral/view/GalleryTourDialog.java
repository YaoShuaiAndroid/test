package com.zb.bilateral.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.zb.bilateral.R;
import com.zb.bilateral.model.BaseModel;
import com.zb.bilateral.model.FloorModel;
import com.zb.bilateral.model.ShowRoomModel;

import java.util.List;


/**
 * Created by yaos on 2017/10/17.
 */

public class GalleryTourDialog extends PopupWindow {
    Activity context;
    Handler handler;

    private List<FloorModel> floorModels;//楼层列表
    private List<ShowRoomModel> showRoomModels;//楼层下的展厅列表

    private final static int CHANGE_DOWN = 1;
    private final static int CHANGE_FLOOR= 2;
    private final static int CLICK_HALL= 3;

    private RecyclerView floorRecyclerView, tourRecyclerView;
    private CommonAdapter commonFloorAdapter, commonTourAdapter;

    public GalleryTourDialog(Activity mContext, final View parent, Handler handler,
                             List<FloorModel> floorModels, List<ShowRoomModel> showRoomModels) {
        this.handler = handler;
        context = mContext;
        this.floorModels = floorModels;
        this.showRoomModels = showRoomModels;

        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_gallery_tour_item, null);

        // 设置popWindow的显示和消失动画
        setAnimationStyle(R.style.anim_menu_topbar);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(view);

        showAsDropDown(parent);

        update();
        //实例化
        init(view);
    }

    public void show(View parent){
        showAsDropDown(parent);
    }

    public void init(View view) {
        floorRecyclerView = (RecyclerView) view.findViewById(R.id.floor_list);
        tourRecyclerView = (RecyclerView) view.findViewById(R.id.tour_list);

        set_recy(floorModels, showRoomModels);
    }

    public void set_recy(final List<FloorModel> floorModels, final List<ShowRoomModel> showRoomModels) {
        floorRecyclerView.setHasFixedSize(true);
        floorRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        floorRecyclerView.setLayoutManager(linearLayoutManager);

        commonFloorAdapter = new CommonAdapter<FloorModel>(R.layout.list_provice_item, floorModels) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, FloorModel item) {
                TextView item_provice_text=baseViewHolder.getView(R.id.item_provice_text);

                item_provice_text.setText(item.getNo()+"楼");

                if(item.is_select()){
                    item_provice_text.setBackgroundColor(Color.parseColor("#fffFFF"));
                }else{
                    item_provice_text.setBackgroundColor(Color.parseColor("#f6f6f6"));
                }
            }
        };
        floorRecyclerView.setAdapter(commonFloorAdapter);

        commonFloorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Message message=new Message();
                message.what=CHANGE_FLOOR;
                message.arg1=position;
                handler.sendMessage(message);
            }
        });

        tourRecyclerView.setHasFixedSize(true);
        tourRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        tourRecyclerView.setLayoutManager(linearLayoutManager1);

        commonTourAdapter = new CommonAdapter<ShowRoomModel>(R.layout.list_tour_item, showRoomModels) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ShowRoomModel item) {
                TextView tour_item_text=baseViewHolder.getView(R.id.tour_item_text);
                TextView tour_item_num=baseViewHolder.getView(R.id.tour_item_num);

                tour_item_text.setText(item.getName());
                tour_item_num.setText((item.getVoiceCount()!=null?item.getVoiceCount():0)+"处讲解");
            }
        };

        tourRecyclerView.setAdapter(commonTourAdapter);

        commonTourAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(showRoomModels.get(position).getId()!=null){
                    Message message=new Message();
                    message.what=CLICK_HALL;
                    message.arg1=Integer.parseInt(showRoomModels.get(position).getVoiceCount());
                    message.obj=showRoomModels.get(position).getId();
                    handler.sendMessage(message);
                }

                dismiss();
            }
        });

    }

    /**
     * 点击楼层后更新楼层展厅数据
     * @param floorModel 新楼层数据
     * @param showRoomModel 新展厅数据
     */
    public void notifyAdapter(List<FloorModel> floorModel, List<ShowRoomModel> showRoomModel) {
        this.floorModels=floorModel;
        this.showRoomModels.clear();
        this.showRoomModels.addAll(showRoomModel);

        commonTourAdapter.notifyDataSetChanged();
        commonFloorAdapter.notifyDataSetChanged();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.sendEmptyMessage(CHANGE_DOWN);
    }
}
