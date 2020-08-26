package auto.cn.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.fragment.ButlerFragment;
import auto.cn.smartbutler.fragment.GirlFragment;
import auto.cn.smartbutler.fragment.UserFragment;
import auto.cn.smartbutler.fragment.WechatFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.VISIBLE;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.tab_main)
    TabLayout tabMain;
    @Bind(R.id.vp_main)
    ViewPager vpMain;
    @Bind(R.id.fab_main)
    FloatingActionButton fabMain;
    private List<String> mTitle;
    private List<Fragment> mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //去掉阴影
        //getSupportActionBar().setElevation(0);
        //测试bugly收集crash异常，执行到这段代码时会发生一个Crash，Logcat的TAG=CrashReportInfo
        //CrashReport.testJavaCrash();
        initDatas();
        initViews();

    }

    private void initDatas() {
        mTitle = new ArrayList<>();
        mTitle.add(getString(R.string.title_tab1));
        mTitle.add(getString(R.string.title_tab2));
        mTitle.add(getString(R.string.title_tab3));
        mTitle.add(getString(R.string.title_tab4));

        mFragment = new ArrayList<>();

        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }

    private void initViews() {
        //fabMain.setVisibility(View.GONE);
        //预加载
        vpMain.setOffscreenPageLimit(mFragment.size());
        //ViewPager 滑动监听
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("TAG", "onPageSelected() called with: i = [" + position + "]");
                if(position==0){
                    fabMain.setVisibility(View.GONE);
                }else {
                    fabMain.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        vpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回的item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }
            //设置标题

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        tabMain.setupWithViewPager(vpMain);
    }
    //FloatingActionButton点击事件
    @OnClick(R.id.fab_main)
    public void fabOnclick(){
        Intent intent = new Intent(MainActivity.this, AtySetting.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            ButterKnife.unbind(this);
        }
}
