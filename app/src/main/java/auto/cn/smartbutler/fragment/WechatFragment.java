package auto.cn.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.adapter.CommonBaseAdapter;
import auto.cn.smartbutler.adapter.ViewHolder;
import auto.cn.smartbutler.bean.WechatBean1;
import auto.cn.smartbutler.ui.AtyWebView;
import auto.cn.smartbutler.utils.StaticClass;
import butterknife.Bind;
import butterknife.ButterKnife;


public class WechatFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.lv_wechat)
    ListView lvWechat;
    private CommonBaseAdapter<WechatBean1.ResultBean.DataBean> mAdapter;
    private List<WechatBean1.ResultBean.DataBean> data = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>();
    private List<String> mUrl = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        final int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        final int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        RxVolley.get(StaticClass.URL_NEWS, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                if (!TextUtils.isEmpty(t)) {
                    WechatBean1 wechatBean = new Gson().fromJson(t, WechatBean1.class);
                    for (int i = 0; i < wechatBean.getResult().getData().size(); i++) {
                        WechatBean1.ResultBean.DataBean dataBean = wechatBean.getResult().getData().get(i);
                        data.add(dataBean);
                        mTitle.add(dataBean.getTitle());
                        mUrl.add(dataBean.getUrl());
                    }

                }
            }

            @Override
            public void onFinish() {
                mAdapter = new CommonBaseAdapter<WechatBean1.ResultBean.DataBean>(getActivity(), data, R.layout.item_wechat) {
                    @Override
                    public void convert(ViewHolder holder, WechatBean1.ResultBean.DataBean wechatBean) {
                        holder.setText(R.id.tv_wechat_title, wechatBean.getTitle())
                                .setText(R.id.tv_wechat_source, wechatBean.getAuthor_name());
                        holder.setImageUrlSize(R.id.iv_wechat_pic, wechatBean.getThumbnail_pic_s(), width / 3, height / 7);
                        //holder.setImageUrl(R.id.iv_wechat_pic, wechatBean.getThumbnail_pic_s());
                    }
                };
                lvWechat.setAdapter(mAdapter);
            }
        });
        lvWechat.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AtyWebView.class);
        intent.putExtra("title", mTitle.get(position));
        intent.putExtra("url", mUrl.get(position));
        startActivity(intent);
    }
}
