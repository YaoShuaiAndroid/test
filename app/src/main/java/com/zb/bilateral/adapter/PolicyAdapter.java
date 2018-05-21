package com.zb.bilateral.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.BaseModel;
import com.zb.bilateral.model.PolicyModel;


public class PolicyAdapter extends ListBaseAdapter<PolicyModel> {
    Context context;

    public PolicyAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_policy_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final PolicyModel item = mDataList.get(position);

        TextView policy_title=holder.getView(R.id.policy_title);
        TextView policy_content=holder.getView(R.id.policy_content);

        policy_content.setText(item.getDigest());
        policy_title.setText(item.getTitle());
    }
}