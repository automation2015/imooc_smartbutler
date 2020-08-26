package auto.cn.smartbutler.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.base.BaseActivity;
import auto.cn.smartbutler.utils.UtilTools;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AtySoftware extends BaseActivity {

    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.lv_about)
    ListView lvAbout;
private List<String> mList=new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_software);
        //去除阴影
        getSupportActionBar().setElevation(0);
        ButterKnife.bind(this);
        mList.add("应用名："+getString(R.string.app_name));
        mList.add("版本号："+UtilTools.getVersion(this));
        mList.add("官网：www.ad.laigang.net");
        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        lvAbout.setAdapter(mAdapter);
    }

}
