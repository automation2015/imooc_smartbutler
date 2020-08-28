package auto.cn.smartbutler.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.base.BaseActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtyBookSplash extends BaseActivity {

    @Bind(R.id.tv_book_time)
    TextView tvBookTime;
    @Bind(R.id.rl_book_splash)
    RelativeLayout rlBookSplash;
    public static final int CODE = 1001;
    public static final int INTERVAL_TIME = 1000;
    public static final int TOTAL_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_book_splash);
        ButterKnife.bind(this);
        MyHandler handler = new MyHandler(this);
        Message message = Message.obtain();
        message.what = CODE;
        message.arg1 = TOTAL_TIME;
        handler.sendMessage(message);
    }

    public static class MyHandler extends Handler {
        public final WeakReference<AtyBookSplash> mWeakReference;
        private int time;

        public MyHandler(AtyBookSplash activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AtyBookSplash activity = mWeakReference.get();
            if (msg.what == CODE) {
                int time = msg.arg1;
                if (activity != null) {
                    activity.tvBookTime.setText(time);
                    //设置textView，更新UI
                    //发送倒计时
                    Message message = Message.obtain();
                    message.what = CODE;
                    message.arg1 = time - INTERVAL_TIME;
                    if (time > 0) {
                        sendMessageDelayed(message, INTERVAL_TIME);
                    }

                }
            }
        }
    }
}
