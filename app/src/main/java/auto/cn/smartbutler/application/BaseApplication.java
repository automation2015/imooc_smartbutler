package auto.cn.smartbutler.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import auto.cn.smartbutler.utils.StaticClass;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
       // CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APPID, true);
        //初始化bmob
        //Bmob.initialize(this, StaticClass.BMOB_APPID);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
// 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.KEY_VOICE);
        /**
         * 百度地图初始化
         */
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //SDKInitializer.setCoordType(CoordinateConverter.CoordType.BD09LL);

    }
}
