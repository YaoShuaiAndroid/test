package com.zb.bilateral.activity.home_page.same_relics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.view.GlideRoundImage;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.person.collect.MuseumFragment;
import com.zb.bilateral.base.LazyFragment;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.retrofit.ApiStores;

import butterknife.Bind;

public class SameRelicsFragment extends LazyFragment {
    @Bind(R.id.same_relics_img)
    ImageView mImageView;
    @Bind(R.id.same_relics_museum_name)
    TextView sameRelicsMuseum_name;
    @Bind(R.id.same_relics_content)
    TextView sameRelicsContent;

    CultrueModel cultrueModel=null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_same_relics_fragment;
    }

    public static SameRelicsFragment newInstance(CultrueModel cultrueModel) {
        SameRelicsFragment sameRelicsFragment = new SameRelicsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("cultrueModel", cultrueModel);
        sameRelicsFragment.setArguments(bundle);
        return sameRelicsFragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        cultrueModel = getArguments().getParcelable("cultrueModel");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {
        if(cultrueModel!=null){
            sameRelicsMuseum_name.setText(cultrueModel.getMuseumName());
            sameRelicsContent.setText(cultrueModel.getIntroduce());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.no_img)
                    .transform(new GlideRoundImage(mContext));

            Glide.with(mContext)
                    .load(ApiStores.IMG_URL_TOP + cultrueModel.getCover())
                    .apply(options)
                    .into(mImageView);
        }
    }
}
