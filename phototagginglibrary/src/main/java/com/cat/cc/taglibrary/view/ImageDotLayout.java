package com.cat.cc.taglibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cat.cc.taglibrary.R;
import com.cat.cc.taglibrary.util.DensityUtil;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cc on 2017/10/25.
 */

public class ImageDotLayout extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = ImageDotLayout.class.getSimpleName();
    private List<LinearLayout> iconList;
    private PhotoView photoView;//背景图
    private RectF tempRectF;
    private OnIconClickListener onIconClickListener;
    private OnIconLongClickListener onIconLongClickListener;
    private OnLayoutReadyListener onLayoutReadyListener;
    private OnImageClickListener onImageClickListener;
    private Matrix photoViewMatrix;
    boolean firstLoadPhotoView = true;
    private int scale;
    private Context mContext;

    public ImageDotLayout(@NonNull Context context) {
        this(context, null);
    }

    public ImageDotLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageDotLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView(context);
    }


    private Drawable mIconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_location);

    void initView(final Context context) {
        photoView = new PhotoView(context);
        LayoutParams layoutParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(photoView, layoutParams);
        photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rectF) {
                if (iconList != null && iconList.size() > 0) {
                    for (LinearLayout icon : iconList) {
                        IconBean bean = (IconBean) icon.getTag();
                        float newX = bean.sx * (rectF.right - rectF.left);
                        float newY = bean.sy * (rectF.bottom - rectF.top);
                        //保持底部中心位置不变
                        icon.setX(rectF.left + newX - DensityUtil.dp2px(getContext(), 20) / 2);
                        icon.setY(rectF.top + newY - DensityUtil.dp2px(getContext(), 20));
                    }
                }
                tempRectF = rectF;
                //图片加载完成后才可以添加图标
                if (onLayoutReadyListener != null) {
                    onLayoutReadyListener.onLayoutReady();
                    //保证只执行一次
                    onLayoutReadyListener = null;
                }
            }
        });
        //实现OnPhotoTapListener接口，监听图片被点击的位置
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float v, float v1) {
                /*Log.i(TAG, "onPhotoTap");
                int id = 0;
                if (iconList != null && iconList.size() > 0) {
                    id = iconList.size();
                }
                IconBean bean = new IconBean(id, v, v1, mIconDrawable);
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(bean);
                }*/
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                //单指时有ScrollView拦截事件滑动
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_POINTER_DOWN :
                //双指触摸时不想要父视图拦截触摸事件，由PhotoView执行放大缩小操作
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    /* boolean doublePointer;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                doublePointer = false;
                onTouchEvent(ev); //在2.3的机器上的时候必须有这句，， 4.0不需要， 怀疑在4.0的OnTouch中对双手的点击也做了处理 而2.3没有 只能再出发一下
                break;
            case MotionEvent.ACTION_POINTER_DOWN :
                doublePointer = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doublePointer = false;
                break;
        }
        if (doublePointer) {
            onTouchEvent(ev);
        } else {
            getChildAt(0).dispatchTouchEvent(ev);
        }
        return true;
    }*/

    public void setIconDrawable(Drawable drawable) {
        mIconDrawable = drawable;
    }

    public void addIcon(IconBean bean) {
        //记住此时photoView的Matrix
        if (photoViewMatrix == null) {
            photoViewMatrix = new Matrix();
        }
        photoView.getSuppMatrix(photoViewMatrix);
        if (iconList == null) {
            iconList = new ArrayList<>();
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(getContext(), 50), DensityUtil.dp2px(getContext(), 50));
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setTag(bean);
        float newX = bean.sx * (tempRectF.left - tempRectF.right);
        float newY = bean.sy * (tempRectF.bottom - tempRectF.top);
        linearLayout.setX(tempRectF.left + newX);
        linearLayout.setY(tempRectF.top + newY);
        linearLayout.setOnClickListener(this);
        linearLayout.setOnLongClickListener(this);

        if(bean.id.equals("-1")){
            //楼层记号
            final TextView text = new TextView(getContext());
            text.setText(bean.name);
            text.setTextSize(16);
            text.setPadding(5, 5, 5, 5);
            text.setTextColor(Color.parseColor("#FF0000"));

            linearLayout.addView(text);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            addView(linearLayout, layoutParams);
        }else {
            final ImageView icon = new ImageView(getContext());
            icon.setImageDrawable(bean.drawable == null ? mIconDrawable : bean.drawable);

            final TextView text = new TextView(getContext());
            text.setText(bean.name);
            text.setTextSize(10);
            text.setBackgroundResource(R.drawable.white_back_3dp);
            text.setPadding(5, 5, 5, 5);
            text.setTextColor(Color.parseColor("#CD9966"));

            linearLayout.addView(icon);
            linearLayout.addView(text);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            addView(linearLayout, layoutParams);
        }
        iconList.add(linearLayout);
    }

    public void updateIconResource(ImageView icon, Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void addIcon(int item,String id,String name, float sx, float sy, Drawable drawable) {
        IconBean iconBean = new IconBean(item,id,name, sx, sy, drawable);
        addIcon(iconBean);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View v) {
        if (onIconClickListener != null) {
            onIconClickListener.onIconClick(v);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (photoViewMatrix != null) {
           /* if(scale>1){
                photoViewMatrix.postScale(2,2);
            }*/
            photoView.setDisplayMatrix(photoViewMatrix);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onIconLongClickListener != null) {
            onIconLongClickListener.onIconLongClick(v);
        }
        return true;
    }


    public static class IconBean {
        public int item;//标签的顺序，从0开始
        public String id;
        public String name;
        public float sx;//左边距比例
        public float sy;//上边距比例
        public Drawable drawable;//图标

        public IconBean(int item,String id,String name, float sx, float sy, Drawable drawable) {
            this.item=item;
            this.id = id;
            this.name=name;
            this.sx = sx;
            this.sy = sy;
            this.drawable = drawable;
        }
    }

    public interface OnIconClickListener {
        void onIconClick(View v);
    }

    public interface OnIconLongClickListener {
        void onIconLongClick(View v);
    }

    public interface OnImageClickListener {
        void onImageClick(IconBean bean);
    }

    public interface OnLayoutReadyListener {
        void onLayoutReady();
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.onIconClickListener = onIconClickListener;
    }

    public void setOnIconLongClickListener(OnIconLongClickListener onIconLongClickListener) {
        this.onIconLongClickListener = onIconLongClickListener;
    }

    public void setOnLayoutReadyListener(OnLayoutReadyListener onLayoutReadyListener) {
        this.onLayoutReadyListener = onLayoutReadyListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    /**
     * 设置图片
     *
     * @param bitmap    图片
     */
    public void setImage(Bitmap bitmap,int scale) {
        this.scale=scale;
        firstLoadPhotoView = true;
        if(scale==2){
            ViewGroup.LayoutParams params=photoView.getLayoutParams();
            int height=DensityUtil.getWidth(mContext)*bitmap.getHeight()/bitmap.getWidth();
            if(height<DensityUtil.getHeight(mContext)){
                height=DensityUtil.getHeight(mContext);
            }
            params.height=height;
            params.width=DensityUtil.getWidth(mContext);

            photoView.setLayoutParams(params);
            photoView.setScaleType(ImageView.ScaleType.FIT_START);
        }

        photoView.setImageBitmap(bitmap);
    }

    /**
     * 移除icon
     */
    public void removeIcon(ImageView icon) {
        removeView(icon);
    }

    /**
     * 移除所有icon
     */
    public void removeAllIcon() {
        if (iconList != null && iconList.size() > 0) {
            for (LinearLayout icon : iconList) {
                removeView(icon);
            }
            iconList.clear();
        }
    }


    /**
     * 获取所有icon信息
     *
     * @return
     */
    public List<IconBean> getAllIconInfos() {
        List<IconBean> rectBeans = new ArrayList<>();
        if (iconList != null && iconList.size() > 0) {
            for (LinearLayout icon : iconList) {
                IconBean rectBean = (IconBean) icon.getTag();
                rectBeans.add(rectBean);
            }
        }
        return rectBeans;
    }

    public void addIcons(List<IconBean> iconBeanList) {
        if (iconBeanList != null && iconBeanList.size() > 0) {
            for (IconBean bean : iconBeanList) {
                addIcon(bean);
            }
        }
    }
}
