package auto.cn.smartbutler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class UtilTools {
    public static void setFond(Context context, TextView tv){
        Typeface fontType=Typeface.createFromAsset(context.getAssets(),"fonts/FONT.TTF");
        tv.setTypeface(fontType);
    }
    //获取版本号
    public static String getVersion(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo( context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知";
        }
    }
}
