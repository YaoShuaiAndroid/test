package com.zb.bilateral.activity.home_page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.fragment.HomePageFragment;
import com.zb.bilateral.model.CityModel;
import com.zb.bilateral.model.ProviceListModel;
import com.zb.bilateral.model.ProviceModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.LocationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class LocationActivity extends BaseNewActivity<PublicPresenter<ProviceListModel>> implements EasyPermissions.PermissionCallbacks,PublicView<ProviceListModel>{
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.location_provice)
    RecyclerView proviceRecyclerView;
    @Bind(R.id.location_city)
    RecyclerView cityRecyclerView;
    @Bind(R.id.location_name)
    TextView locationName;

    private CommonAdapter commonProviceAdapter,commonCityAdapter;
    private MyApplication myApplication;

    private List<ProviceModel> proviceModels=new ArrayList<>();//省份列表
    private List<CityModel> cityModels=new ArrayList<>();//城市列表

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private final static int LOCATION_RESULT=100;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("城市定位");
        
        myApplication= (MyApplication) getApplicationContext();

        if(myApplication.getAddress()!=null){
            locationName.setText(myApplication.getAddress());
        }

        mLocationClient = new LocationClient(mCctivity);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);

        initPermission();
    }

    @Override
    protected void initData() {
        getArea();
    }

    /**
     * 省市区数据
     */
    public void getArea() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.getArea();
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }


    public void setProviceRecy(){
        proviceRecyclerView.setHasFixedSize(true);
        proviceRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mCctivity,LinearLayoutManager.VERTICAL,false);
        proviceRecyclerView.setLayoutManager(linearLayoutManager);

        commonProviceAdapter = new CommonAdapter<ProviceModel>(R.layout.list_provice_item, proviceModels) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, ProviceModel item) {
                TextView item_provice_text=baseViewHolder.getView(R.id.item_provice_text);

                item_provice_text.setText(item.getName());

                if(item.is_select()){
                    item_provice_text.setBackgroundResource(R.color.white_color);
                }else{
                    item_provice_text.setBackgroundColor(Color.parseColor("#f3f3f3"));
                }
            }
        };

        proviceRecyclerView.setAdapter(commonProviceAdapter);

        commonProviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                proviceModels=setProvice(proviceModels,position);

                cityModels.clear();
                cityModels.addAll(proviceModels.get(position).getCityList());

                commonProviceAdapter.notifyDataSetChanged();
                commonCityAdapter.notifyDataSetChanged();
            }
        });

        cityRecyclerView.setHasFixedSize(true);
        cityRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mCctivity,LinearLayoutManager.VERTICAL,false);
        cityRecyclerView.setLayoutManager(linearLayoutManager1);

        commonCityAdapter = new CommonAdapter<CityModel>(R.layout.list_city_item, cityModels) {
            @Override
            public void convertViewItem(BaseViewHolder baseViewHolder, CityModel item) {
                TextView item_city_text=baseViewHolder.getView(R.id.item_city_text);

                item_city_text.setText(item.getName());
            }
        };

        cityRecyclerView.setAdapter(commonCityAdapter);

        commonCityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putString("city_id",cityModels.get(position).getId());
                bundle.putString("city_name",cityModels.get(position).getName());

                Intent intent=new Intent(mCctivity, HomePageFragment.class);
                intent.putExtras(bundle);
                setResult(LOCATION_RESULT,intent);
                finish();
            }
        });
    }


    @OnClick({R.id.top_left_img,R.id.location_fresh,R.id.location_name})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.location_fresh:
                initPermission();
                break;
            case R.id.location_name:
                Bundle bundle=new Bundle();
                bundle.putString("city_id","-1");
                bundle.putString("city_name",locationName.getText().toString());

                Intent intent=new Intent(mCctivity, HomePageFragment.class);
                intent.putExtras(bundle);
                setResult(100,intent);
                finish();
                break;
        }
    }

    boolean flag;

    private final int PERMISSIONS_LOCATION= 1;
    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (!EasyPermissions.hasPermissions(mCctivity, perms)) {
                EasyPermissions.requestPermissions(this, "APP需要打开定位权限", PERMISSIONS_LOCATION, perms);
            } else {
                flag = true;

                getLocation();
            }
        } else {
            flag = true;

            getLocation();
        }
    }

    /**
     * 权限的结果回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 获取定位信息
     */
    public void getLocation() {
        mLocationClient.start();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        getLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        AppToast.showShortText(mCctivity,"请打开定位权限否则无法定位");
    }

    @Override
    public void SendResultSuccess(ProviceListModel proviceListModel) {
        if(proviceListModel.getProvinceList().size()>0){
            proviceListModel.getProvinceList().get(0).setIs_select(true);

            proviceModels=setProvice(proviceListModel.getProvinceList(),0);

            cityModels.addAll(proviceListModel.getProvinceList().get(0).getCityList());

            setProviceRecy();
        }
    }

    public List<ProviceModel> setProvice(List<ProviceModel> proviceModels,int position){
        for (int i = 0; i < proviceModels.size(); i++) {
            if(i==position){
                proviceModels.get(i).setIs_select(true);
            }else{
                proviceModels.get(i).setIs_select(false);
            }
        }

        return proviceModels;
    }

    @Override
    public void SendResultFail(String msg) {

    }

    @Override
    public void SendBannerSuccess(List list) {

    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String city = location.getCity();    //获取城市

            if(!TextUtils.isEmpty(city)){
                mLocationClient.stop();
                mLocationClient.unRegisterLocationListener(this);
            }

            myApplication.setAddress(city);
            myApplication.setCityId("-1");

            locationName.setText(city);
        }
    }
}
