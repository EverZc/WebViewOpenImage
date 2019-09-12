package me.everzc.webviewimage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;



import java.util.Arrays;
import java.util.List;


/**
 */
public class PhotoNewBrowseActivity extends AppCompatActivity {

    private int firstDisplayImageIndex = 0;
    private boolean newPageSelected = false;
    private PinchImageViewBeifen mCurImage;
    private BaseAnimCloseViewPager imageViewPager;
    private List<String> pictureList;
    private String[] imageUrls = new String[]{};
    private int[] initialedPositions = null;
    PagerAdapter adapter;
    boolean canDrag = false;

    private String curImageUrl = "";

    private FrameLayout flCurLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_new_browse);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        initView();
    }

    public void initView() {
        imageUrls = getIntent().getStringArrayExtra("imageUrls");
        pictureList = Arrays.asList(imageUrls);
        curImageUrl = getIntent().getStringExtra("curImageUrl");
        firstDisplayImageIndex = returnClickedPosition();
        imageViewPager = (BaseAnimCloseViewPager) findViewById(R.id.viewpager);
        setViewPagerAdapter();


    }




    private int returnClickedPosition() {
        if (imageUrls == null || curImageUrl == null) {
            return -1;
        }
        for (int i = 0; i < imageUrls.length; i++) {
            if (curImageUrl.equals(imageUrls[i])) {
                return i;
            }
        }
        return -1;
    }

    private void setViewPagerAdapter() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pictureList == null ? 0 : pictureList.size();
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View layout = (View) object;
                container.removeView(layout);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View layout;
                layout = LayoutInflater.from(PhotoNewBrowseActivity.this).inflate(R.layout.layout_image_detail, null);
                ImageView ivLoading= (ImageView) layout.findViewById(R.id.iv_imageView);
                AnimationDrawable drawable= (AnimationDrawable) ivLoading.getBackground();
                drawable.start();
               // layout.setOnClickListener(onClickListener);
                PinchImageViewBeifen pinchImageView = (PinchImageViewBeifen) layout.findViewById(R.id.image_detail);
               pinchImageView.setOnClickListener(onClickListener);
                container.addView(layout);
                layout.setTag(position);
                if (position == firstDisplayImageIndex) {
                    onViewPagerSelected(position);
                }
                return layout;

            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        imageViewPager.setAdapter(adapter);
        imageViewPager.setOffscreenPageLimit(1);
        imageViewPager.setCurrentItem(firstDisplayImageIndex);
        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0f && newPageSelected) {
                    newPageSelected = false;
                    onViewPagerSelected(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                newPageSelected = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               // llLoading.setVisibility(View.VISIBLE);



            }
        });
        imageViewPager.setiAnimClose(new BaseAnimCloseViewPager.IAnimClose() {
            @Override
            public boolean canDrag() {
                return canDrag;
            }

            @Override
            public void onPictureClick() {
                finishAfterTransition();
            }

            @Override
            public void onPictureRelease(View view) {
                finishAfterTransition();
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAfterTransition();
        }
    };


    private void onViewPagerSelected(int position) {
        updateCurrentImageView(position);
        setImageView(pictureList.get(position));
    }


    /**
     * 设置图片
     *
     * @param path
     */
    private void setImageView(final String path) {
        if (mCurImage.getDrawable() != null)//判断是否已经加载了图片，避免闪动
            return;
        if (TextUtils.isEmpty(path)) {
            mCurImage.setBackgroundColor(Color.GRAY);
            return;
        }
        canDrag = false;
       // MyUtils.loadingGlide(PhotoNewBrowseActivity.this,path,mCurImage);
        Glide.with(this).load(path).into(mCurImage);
    }

    // 初始化每个view的image
    protected void updateCurrentImageView(final int position) {
        View currentLayout = imageViewPager.findViewWithTag(position);
        if (currentLayout == null) {
            ViewCompat.postOnAnimation(imageViewPager, new Runnable() {

                @Override
                public void run() {
                    updateCurrentImageView(position);
                }
            });
            return;
        }
        flCurLayout = (FrameLayout) currentLayout.findViewById(R.id.fl_actical);
        mCurImage = (PinchImageViewBeifen) currentLayout.findViewById(R.id.image_detail);
        imageViewPager.setCurrentShowView(flCurLayout);
    }


    @Override
    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra("index", imageViewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.finishAfterTransition();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


    }
}
