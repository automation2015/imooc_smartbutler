package auto.cn.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.utils.SPUtils;
import auto.cn.smartbutler.utils.StaticClass;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtySplash extends AppCompatActivity {
    @Bind(R.id.tv_splash)
    TextView tvSplash;
    /**
     * 1.延时2000ms
     * 2.判断程序是否第一次运行
     * 3.自定义字体
     * 4.全屏主题
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if (isFirst()) {
                        startActivity(new Intent(AtySplash.this, AtyGuide.class));
                    } else {
                        startActivity(new Intent(AtySplash.this, MainActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_splash);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        //延时2000ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        //UtilTools.setFond(this, tvSplash);
    }

    //判断程序是否第一次运行
    private boolean isFirst() {
        boolean isFirst = SPUtils.getBoolean(AtySplash.this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            SPUtils.putBoolean(AtySplash.this, StaticClass.SHARE_IS_FIRST, false);
            return true;
        } else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
