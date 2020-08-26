package auto.cn.smartbutler.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.service.SmsService;
import auto.cn.smartbutler.utils.SPUtils;
import auto.cn.smartbutler.utils.StaticClass;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AtySetting extends Activity {

    private static final int RESULT_CODE_OPENCAMERA = 0;
    @Bind(R.id.sw_speak)
    Switch swSpeak;
    @Bind(R.id.sw_sms)
    Switch swSms;
    @Bind(R.id.tv_setting_version)
    TextView tvSettingVersion;
    @Bind(R.id.ll_setting_update)
    LinearLayout llSettingUpdate;
    @Bind(R.id.tv_setting_scan)
    TextView tvSettingScan;
    @Bind(R.id.ll_setting_scan)
    LinearLayout llSettingScan;
    @Bind(R.id.tv_setting_share)
    TextView tvSettingShare;
    @Bind(R.id.ll_setting_share)
    LinearLayout llSettingShare;
    @Bind(R.id.tv_setting_location)
    TextView tvSettingLocation;
    @Bind(R.id.ll_setting_location)
    LinearLayout llSettingLocation;
    @Bind(R.id.tv_setting_about)
    TextView tvSettingAbout;
    @Bind(R.id.ll_setting_about)
    LinearLayout llSettingAbout;
    @Bind(R.id.tv_setting_query)
    TextView tvSettingQuery;
    @Bind(R.id.ll_setting_query)
    LinearLayout llSettingQuery;
    private String versionName;
    private long versionCode;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_setting);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        boolean isSpeak = SPUtils.getBoolean(this, "isSpeak", false);
        swSpeak.setChecked(isSpeak);
        boolean isSms = SPUtils.getBoolean(this, "isSms", false);
        swSms.setChecked(isSms);
        try {
            getVersionNameCode();
            tvSettingVersion.setText("检测版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tvSettingVersion.setText("检测版本：");
        }
    }

    //更新软件
    @OnClick(R.id.ll_setting_update)
    public void checkUpdate() {
        //1.请求服务器的配置文件，拿到code
        //2.比较版本
        //3.dialog提示
        //4.跳转到更新页面，并且把url传递过去

        RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.d("TAG", t);
                parsingJson(t);
            }
        });
    }

    //解析获取的Json数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (versionCode < code) {
                showUpdateDialog(jsonObject.getString("content"));
            } else {
                Toast.makeText(AtySetting.this, "当前是最新版本！", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(AtySetting.this)
                .setTitle("有新版本啦...")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(AtySetting.this, AtyUpdate.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么也不做，也会执行dissmis方法
                    }
                }).show();
    }

    //检查版本更新
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
        versionName = packageInfo.versionName;
        versionCode = packageInfo.versionCode;
    }

    //二维码分享
    @OnClick(R.id.ll_setting_share)
    public void share() {
        startActivity(new Intent(AtySetting.this, AtyQrCode.class));

    }

    //二维码扫描
    @OnClick(R.id.ll_setting_scan)
    public void scan() {
        //打开扫描界面扫描条形码或二维码
        //CaptureActivity，固定写法，导入lib包下的
        Intent openCameraIntent = new Intent(AtySetting.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    //关于软件
    @OnClick(R.id.ll_setting_about)
    public void aboutSoftware() {
        Intent intent = new Intent(AtySetting.this, AtySoftware.class);
        startActivity(intent);
    }

    //归属地查询
    @OnClick(R.id.ll_setting_query)
    public void phoneQuery() {
        Intent intent = new Intent(AtySetting.this, AtyPhoneQuery.class);
        startActivity(intent);
    }

    //我的位置
    @OnClick(R.id.ll_setting_location)
    public void getLocation() {
        Intent intent = new Intent(AtySetting.this, AtyLocation.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                tvSettingScan.setText(scanResult);
            }
        }
    }

    @OnClick(R.id.sw_speak)
    public void speak() {
        //切换相反
        swSpeak.setSelected(!swSpeak.isSelected());
        //保存状态
        SPUtils.putBoolean(this, "isSpeak", swSpeak.isChecked());
    }

    @OnClick(R.id.sw_sms)
    public void sms() {
        //切换相反
        swSpeak.setSelected(!swSpeak.isSelected());
        //保存状态
        SPUtils.putBoolean(this, "isSms", swSpeak.isChecked());
        Intent intent = new Intent(AtySetting.this, SmsService.class);
        if (swSms.isChecked()) {
            startService(intent);
        } else {
            stopService(intent);
        }

    }

}
