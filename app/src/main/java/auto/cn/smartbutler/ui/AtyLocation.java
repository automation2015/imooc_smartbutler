package auto.cn.smartbutler.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

import auto.cn.smartbutler.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtyLocation extends Activity {
    @Bind(R.id.bmapView)
    MapView bmapView;
    private BaiduMap mBaiduMap;
    //定位
    private LocationClient locationClient;
    private MyLocationListener myLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_location);
        ButterKnife.bind(this);
        mBaiduMap = bmapView.getMap();
        //显示卫星图层
        //mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //开启交通图
        //mBaiduMap.setTrafficEnabled(true);
        //开启热力图
        //mBaiduMap.setBaiduHeatMapEnabled(true);

        //初始化定位参数
        locationClient = new LocationClient(getApplicationContext());
        initLocationOption();
        myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);

        //开始定位
        locationClient.start();
        Log.e("TAG", "initLocationOption() called");
    }

    /**
     * 初始化定位参数配置
     */
    private void initLocationOption() {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
//注册监听函数
        // locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();
            //定位结束
            Log.e("TAG", "end" + errorCode);
            StringBuffer sb = new StringBuffer(256);
            sb.append("time:");
            sb.append(location.getTime());
            sb.append("\nerror code:");
            sb.append(location.getLocType());
            sb.append("\nlatitude:");
            sb.append(location.getLatitude());
            sb.append("\nlontitude:");
            sb.append(location.getLongitude());
            sb.append("\nradius:");
            sb.append(location.getRadius());
            //Gps定位结果
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed:");
                sb.append(location.getSpeed());//单位：公里/小时
                sb.append("\nsatellite:");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight:");
                sb.append(location.getAltitude());
                sb.append("\ndirection:");
                sb.append(location.getDirection());
                sb.append("\naddr:");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe:");
                sb.append("gps定位成功");


            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                //网络定位结果
                sb.append("\naddr:");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperations:");
                sb.append(location.getOperators());
                sb.append("\ndescribe:");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                //离线定位结果
                sb.append("\ndescribe:");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe:");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com,会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe:");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe:");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果");
            }
            sb.append("\nlocationdescribe:");
            sb.append(location.getLocationDescribe());//定义语义化信息
            List<Poi> list = location.getPoiList();//POI数据
            if (list != null) {
                sb.append("\npoilist size=:");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi=:");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            //定位的结果
            Log.e("TAG", sb.toString());
            //移动到我的位置
            //设置缩放
            MapStatusUpdate mapUpdate = MapStatusUpdateFactory.zoomTo(18);
            mBaiduMap.setMapStatus(mapUpdate);
//            //开始移动
            MapStatusUpdate mapLatlng = MapStatusUpdateFactory.newLatLng(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            mBaiduMap.setMapStatus(mapLatlng);
//            //绘制图层
//            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
//            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.ic_location);
//            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
//            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        bmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        bmapView.onDestroy();
    }
}
