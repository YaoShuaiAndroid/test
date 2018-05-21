package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.example.mycommon.util.FileUtils;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.MessageModel;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.model.PublicListModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.model.ShareDetailModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.model.BannerListModel;
import com.zb.bilateral.model.CollectListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.MuseumListModel;
import com.zb.bilateral.model.PersonListModel;
import com.zb.bilateral.model.PolicyListModel;
import com.zb.bilateral.model.ProviceListModel;
import com.zb.bilateral.model.QuestionListModel;
import com.zb.bilateral.model.ShowListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by yaos on 2017/5/10.
 */
public class PublicPresenter<T> extends BasePresenter<PublicView> {
    Context context;

    public PublicPresenter(PublicView view, Context context) {
        attachView(view);
        this.context = context;
    }

    //文化拾遗列表
    public void gleanings(String currentPage, String title, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("title", title);

        addSubscription(apiBaseStores.gleanings(title, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueListModel>() {
                    @Override
                    public void onSuccess(CultrueListModel baseModel) {
                        mvpView.SendResultSuccess(baseModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //政策法规
    public void policys(String currentPage, String title, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("title", title);

        addSubscription(apiBaseStores.policys(title, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<PolicyListModel>() {
                    @Override
                    public void onSuccess(PolicyListModel baseModel) {
                        mvpView.SendResultSuccess(baseModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }


    //我的收藏-博物馆
    public void msColl(String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiStores.msColl(token, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<MuseumListModel>() {
                    @Override
                    public void onSuccess(MuseumListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //查询基本展览列表
    public void basicExhs(String currentPage, String museumId, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("museumId", museumId);

        addSubscription(apiBaseStores.basicExhs(museumId, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<ShowListModel>() {
                    @Override
                    public void onSuccess(ShowListModel showListModel) {
                        mvpView.SendResultSuccess(showListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //查询临时展览列表
    public void temporaryExhs(String currentPage, String museumId, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("museumId", museumId);

        addSubscription(apiBaseStores.temporaryExhs(museumId, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<ShowListModel>() {
                    @Override
                    public void onSuccess(ShowListModel showListModel) {
                        mvpView.SendResultSuccess(showListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //我的收藏-馆藏
    public void trColl(String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiStores.trColl(token, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CollectListModel>() {
                    @Override
                    public void onSuccess(CollectListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //公共接口
    public void other(final Context context) {
        Map map = new HashMap();

        addSubscription(apiStores.other(AppUtil.getMD5Str(map)),
                new ApiCallback<PublicListModel>() {
                    @Override
                    public void onSuccess(PublicListModel publicListModel) {
                        PublicModel publicModel = publicListModel.getOther();
                        AppUtil.save(publicModel, FileUtils.publicPath(context));
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //修改名称
    public void updateName(String name, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("nickname", name);
        map.put("token", token);

        addSubscription(apiStores.updateName(name, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //修改图像
    public void updatePhoto(final String photo, String token) {
        Map map = new HashMap();
        map.put("photo", photo);
        map.put("token", token);

        addSubscription(apiStores.updatePhoto(photo, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        commitModel.setImgPath(photo);
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //馆藏
    public void treasures(String currentPage, String museumId, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("museumId", museumId);

        addSubscription(apiBaseStores.treasures(museumId, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CollectListModel>() {
                    @Override
                    public void onSuccess(CollectListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //我的收藏-活动
    public void actColl(String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiStores.actColl(token, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //我的收藏-文化拾遗
    public void gleaColl1(String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiStores.gleaColl1(token, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueListModel>() {
                    @Override
                    public void onSuccess(CultrueListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //单个政策法规
    public void policy(String policyId) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("policyId", policyId);

        addSubscription(apiStores.policy(policyId, AppUtil.getMD5Str(map)),
                new ApiCallback<PolicyListModel>() {
                    @Override
                    public void onSuccess(PolicyListModel baseModel) {
                        mvpView.SendResultSuccess(baseModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //加载轮播图
    public void banner(String type) {
        Map map = new HashMap();
        map.put("type", type);

        addSubscription(apiBaseStores.banner(type, AppUtil.getMD5Str(map)),
                new ApiCallback<BannerListModel>() {
                    @Override
                    public void onSuccess(BannerListModel bannerListModel) {
                        mvpView.SendBannerSuccess(bannerListModel.getHomePageList());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void leaveWord(String content, String name, String phone, String token, String type, String museumId) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("content", content);
        map.put("name", name);
        map.put("phone", phone);
        map.put("token", token);
        map.put("type", type);
        map.put("museumId", museumId);

        addSubscription(apiStores.leaveWord(content, name, type, museumId, phone, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void reply(String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("token", token);

        addSubscription(apiStores.reply(token, AppUtil.getMD5Str(map)),
                new ApiCallback<QuestionListModel>() {
                    @Override
                    public void onSuccess(QuestionListModel commitModel) {
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //博物馆列表
    public void getMuseums(String name, String currentPage, String area, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("name", name);
        map.put("currentPage", currentPage);
        map.put("area", area);

        addSubscription(apiStores.getMuseums(name, currentPage, area, AppUtil.getMD5Str(map)),
                new ApiCallback<MuseumListModel>() {
                    @Override
                    public void onSuccess(MuseumListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //根据定位获取博物馆列表
    public void getLocationMuseums(String name, String currentPage, String area, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("name", name);
        map.put("currentPage", currentPage);
        map.put("currentCity", area);

        addSubscription(apiStores.getLocationMuseums(name, currentPage, area, AppUtil.getMD5Str(map)),
                new ApiCallback<MuseumListModel>() {
                    @Override
                    public void onSuccess(MuseumListModel museumListModel) {
                        mvpView.SendResultSuccess(museumListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }


    //动态-活动列表
    public void acts(String museumId, String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("museumId", museumId);
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiBaseStores.acts(museumId, currentPage, token, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel activityListModel) {
                        mvpView.SendResultSuccess(activityListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }


    //动态-公告
    public void notices(String museumId, String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("museumId", museumId);
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiBaseStores.notices(museumId, currentPage, token, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel activityListModel) {
                        mvpView.SendResultSuccess(activityListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //动态-资讯列表
    public void infos(String museumId, String currentPage, String token, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("museumId", museumId);
        map.put("currentPage", currentPage);
        map.put("token", token);

        addSubscription(apiBaseStores.infos(museumId, currentPage, token, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel activityListModel) {
                        mvpView.SendResultSuccess(activityListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //讲解预约
    public void commentary(String museumId, String name, String phone, String bookingTime, String number, String other, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("museumId", museumId);
        map.put("name", name);
        map.put("phone", phone);
        map.put("bookingTime", bookingTime);
        map.put("number", number);
        map.put("other", other);
        map.put("token", token);


        addSubscription(apiStores.commentary(museumId, name, phone, bookingTime, number, other, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //省市区数据
    public void getArea() {
        mvpView.showLoading();

        Map map = new HashMap();

        addSubscription(apiStores.getArea(AppUtil.getMD5Str(map)),
                new ApiCallback<ProviceListModel>() {
                    @Override
                    public void onSuccess(ProviceListModel proviceListModel) {
                        mvpView.SendResultSuccess(proviceListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //省市区数据
    public void main(String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("token", token);

        addSubscription(apiStores.main(token, AppUtil.getMD5Str(map)),
                new ApiCallback<PersonListModel>() {
                    @Override
                    public void onSuccess(PersonListModel personListModel) {
                        mvpView.SendResultSuccess(personListModel.getConsumer());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //我的积分
    public void myPoint(String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("token", token);

        addSubscription(apiStores.myPoint(token, AppUtil.getMD5Str(map)),
                new ApiCallback<PersonModel>() {
                    @Override
                    public void onSuccess(PersonModel personModel) {
                        mvpView.SendResultSuccess(personModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }


    //场馆入驻
    public void newMu(String token, String name, String province, String city, String detailedAddress, String curatorName, String curatorPhone,
                      String linkmanName, String linkmanPhone) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("token", token);
        map.put("name", name);
        map.put("province", province);
        map.put("city", city);
        map.put("detailedAddress", detailedAddress);
        map.put("curatorName", curatorName);
        map.put("curatorPhone", curatorPhone);
        map.put("linkmanName", linkmanName);
        map.put("linkmanPhone", linkmanPhone);


        addSubscription(apiStores.newMu(token, name, province, city, detailedAddress, curatorName, curatorPhone,
                linkmanName, linkmanPhone, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }



    public void updatePassword(String newPwd, String oldPwd, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("oldPwd", oldPwd);
        map.put("newPwd", newPwd);
        map.put("token", token);

        addSubscription(apiStores.updatePassword(oldPwd, newPwd, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void antiques(String antiquesId) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("antiquesId", antiquesId);

        addSubscription(apiStores.antiques(antiquesId, AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueDetailModel>() {
                    @Override
                    public void onSuccess(CultrueDetailModel cultrueDetailModel) {
                        mvpView.SendResultSuccess(cultrueDetailModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //个人中心上传图片
    public void person_img(final String token, File path, final Context context) {
        mvpView.showLoading();

        Map<String, String> map_sign = new HashMap();
        map_sign.put("token", token);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("file" + "\";filename=\"" + path.getName(), RequestBody.create(MediaType.parse("image/png"), path));

        addSubscription(apiStores.person_img(
                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), token),
                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), AppUtil.getMD5Str(map_sign)),
                map),

                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        updatePhoto(commitModel.getImgPath(),token);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void nearbyAntiques(String antiquesId,String token ) {
        Map map=new HashMap();
        map.put("antiquesId",antiquesId);
        map.put("token",token);

        addSubscription(apiBaseStores.nearbyAntiques(antiquesId,token, AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueListModel>() {
                    @Override
                    public void onSuccess(CultrueListModel cultrueListModel) {
                        mvpView.SendResultSuccess(cultrueListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void share() {
        Map map=new HashMap();

        addSubscription(apiBaseStores.share( AppUtil.getMD5Str(map)),
                new ApiCallback<ShareDetailModel>() {
                    @Override
                    public void onSuccess(ShareDetailModel shareDetailModel) {
                        mvpView.SendResultSuccess(shareDetailModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void message(String messageId) {
        Map map=new HashMap();
        map.put("messageId",messageId);

        addSubscription(apiBaseStores.message( messageId,AppUtil.getMD5Str(map)),
                new ApiCallback<MessageModel>() {
                    @Override
                    public void onSuccess(MessageModel messageModel) {
                        mvpView.SendResultSuccess(messageModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

}
