package auto.cn.smartbutler.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.bean.UserBean;

/**
 * 自定义Loader
 */
public class UserLoader extends AsyncTaskLoader<List<UserBean>> {
    public UserLoader( Context context) {
        super(context);
    }

    /**
     * 在子线程加载数据
     * @return
     */
    @Nullable
    @Override
    public List<UserBean> loadInBackground() {
        //模拟从网络返回数据
        List<UserBean> list=new ArrayList<>();
        list.add(new UserBean("郭靖","123"));
        list.add(new UserBean("黄蓉","123"));
        list.add(new UserBean("张三丰","123"));
        list.add(new UserBean("洪七公","123"));
        return list;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        //如果Loader启动了，强制启动LoaderInBackground
        if(isStarted()){
            forceLoad();
        }
    }
}
