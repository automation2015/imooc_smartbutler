package auto.cn.smartbutler.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.adapter.CommonBaseAdapter;
import auto.cn.smartbutler.adapter.ViewHolder;
import auto.cn.smartbutler.base.BaseActivity;
import auto.cn.smartbutler.bean.UserBean;
import auto.cn.smartbutler.loader.UserLoader;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtyCustom extends BaseActivity implements LoaderManager.LoaderCallbacks<List<UserBean>> {
    @Bind(R.id.lv_custom)
    ListView lvCustom;
    private CommonBaseAdapter<UserBean> mAdapter;
    private List<UserBean> mDatas = new ArrayList<>();
    private LoaderManager supportLoaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_custom);
        ButterKnife.bind(this);


        mAdapter = new CommonBaseAdapter<UserBean>(this, mDatas, R.layout.item_loader_custom) {
            @Override
            public void convert(ViewHolder holder, UserBean userBeans) {
                holder.setText(R.id.tv_username, userBeans.getUsername())
                        .setText(R.id.tv_password, userBeans.getPassword());
            }
        };
        lvCustom.setAdapter(mAdapter);
        supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.initLoader(0, null, this);
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<List<UserBean>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new UserLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<UserBean>> loader, List<UserBean> userBeans) {
        if (userBeans != null && userBeans.size() > 0) {
            mDatas.addAll(userBeans);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<UserBean>> loader) {

    }
}
