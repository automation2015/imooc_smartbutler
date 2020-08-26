package auto.cn.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.utils.StaticClass;
import auto.cn.smartbutler.view.DispatchLinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 短信监听服务
 */
public class SmsService extends Service implements View.OnClickListener {
    private SmsReceiver smsReceiver;
    private HomeReceiver homeReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private DispatchLinearLayout mView;
    private String KEY_SYSTEM_DIALOGS_RESON ="reason";
    private String KEYSYSTEM_DIALOGS_HOME_KEY="homekey";
    private TextView tvSettingPhone;
    private TextView tvSettingContent;
    private Button btnSend;
    public SmsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
//动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //添加Action
        intent.addAction(StaticClass.ACTION_SMS);
        //设置权限,google推荐为1000，这里设置为整数最大值
        intent.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intent);
        homeReceiver=new HomeReceiver();
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(smsReceiver);
        unregisterReceiver(homeReceiver);

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onClick(View v) {
        sendSms();
    }

    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //注意此处应该添加短信权限
            String action = intent.getAction();
            if (action == StaticClass.ACTION_SMS) {
                Log.e("TAG", "来短信了");
                //获取短信内容，返回的时一个Object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj : objs) {
                    //把数组元素转换为短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getMessageBody();
                    Log.d("TAG", "短信的内容：" + smsPhone + ":" + smsContent);
                    showWindows();
                }

            }
        }
    }

    //窗口提示
    private void showWindows() {
        //获取系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//获取布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParams.format = PixelFormat.TRANSPARENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.item_sms, null);
        tvSettingPhone=mView.findViewById(R.id.tv_setting_phone);
        tvSettingContent=mView.findViewById(R.id.tv_setting_content);
        btnSend=mView.findViewById(R.id.btn_send_sms);
        btnSend.setOnClickListener(this);
        tvSettingPhone.setText("发件人：" + smsPhone);
        tvSettingContent.setText(smsContent);
        //添加到窗口中
        wm.addView(mView, layoutParams);
        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }
    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener=new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
                Log.d("TAG", "我按了“back”键");
                if(mView.getParent()!=null){
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };


    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SEND, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", smsContent);
        startActivity(intent);
        //消失窗口
        if (mView.getParent() != null) {
            wm.removeView(mView);
        }

    }
    class HomeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra(KEY_SYSTEM_DIALOGS_RESON);
                if(KEYSYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    Log.d("TAG", "我点击了“home”键");
                    if(mView.getParent()!=null){
                        wm.removeView(mView);
                    }
                }
            }
        }
    }
}
