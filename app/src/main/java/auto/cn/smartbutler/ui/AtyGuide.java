package auto.cn.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtyGuide extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.vp_guide)
    ViewPager vpGuide;
    @Bind(R.id.point1)
    ImageView point1;
    @Bind(R.id.point2)
    ImageView point2;
    @Bind(R.id.point3)
    ImageView point3;
    @Bind(R.id.tv_guide_skip)
    TextView tvGuideSkip;

    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;
    private Button btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_guide);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        view1 = View.inflate(this, R.layout.guide_page1, null);
        view2 = View.inflate(this, R.layout.guide_page2, null);
        view3 = View.inflate(this, R.layout.guide_page3, null);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        btnStart = view3.findViewById(R.id.btn_guide_start);
        btnStart.setOnClickListener(this);
        setPointImg(true, false, false);
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        setPointImg(true, false, false);
                        tvGuideSkip.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false, true, false);
                        tvGuideSkip.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false, false, true);
                        tvGuideSkip.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    //设置小圆点的图片
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3) {
        if (isCheck1) {
            point1.setBackgroundResource(R.mipmap.point_green);
        } else {
            point1.setBackgroundResource(R.mipmap.point_grey);
        }
        if (isCheck2) {
            point2.setBackgroundResource(R.mipmap.point_green);
        } else {
            point2.setBackgroundResource(R.mipmap.point_grey);
        }
        if (isCheck3) {
            point3.setBackgroundResource(R.mipmap.point_green);
        } else {
            point3.setBackgroundResource(R.mipmap.point_grey);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guide_start:
            case R.id.tv_guide_skip:
                startActivity(new Intent(AtyGuide.this, MainActivity.class));
                break;
        }
    }
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        //进入viewpager的时候，他会加载第一屏和第二屏，展示第一屏，预加载第二屏，滑动到第二屏的
        // 时候，会预加载第三屏而第一屏因为有可能滑动回第一屏，所以不会销毁，而滑动到第三屏，就
        // 会销毁第一屏，第二屏不会销毁，同理第四屏也是这样，
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager) container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView(mList.get(position));
        }
    }
}
