package com.zb.bilateral.util;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zb.bilateral.model.PublicModel;

/**
 * Created by bt on 2018/3/21.
 */

public class ShareUtil {
    private Context mContext;

    public void share(String title, String content, PublicModel publicModel, final Context context){
        this.mContext=context;
        //防止QQ太长无法分享
        if(!TextUtils.isEmpty(content)&&Html.fromHtml(content).length()>50){
            content=(""+Html.fromHtml(content)).substring(0,49);
        }

        UMImage image = new UMImage(mContext,"http://www.zb0933.com/zbjj/userfiles/1/images/logo/logo.png");//网络图片
        UMWeb web = new UMWeb(publicModel.getShareRequest());
        web.setTitle(title);//标题
        web.setThumb(image);
        if(!TextUtils.isEmpty(content)){
            web.setDescription(""+ Html.fromHtml(content));//描述
        }else{
            web.setDescription("");//描述
        }

        new ShareAction((Activity) context)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(shareListener).open();
    };

    UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            AppToast.makeToast(mContext,"分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            AppToast.makeToast(mContext,"分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AppToast.makeToast(mContext,"您取消了分享");
        }
    };
}
