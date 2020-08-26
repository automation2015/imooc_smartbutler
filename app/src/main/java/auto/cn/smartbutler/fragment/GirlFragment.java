package auto.cn.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.adapter.CommonBaseAdapter;
import auto.cn.smartbutler.adapter.ViewHolder;
import auto.cn.smartbutler.bean.GirlBean;
import auto.cn.smartbutler.utils.PicassoUtils;
import auto.cn.smartbutler.utils.StaticClass;
import auto.cn.smartbutler.view.CustomDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;


public class GirlFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.gv_girl)
    GridView gvGirl;
    private List<GirlBean.DataBean> data = new ArrayList<>();
    private CommonBaseAdapter<GirlBean.DataBean> mAdapter;
    private CustomDialog dialog;
    private ImageView iv;
    private List<String > mListUrl=new ArrayList<>();
    private PhotoViewAttacher mAttecher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        ButterKnife.bind(this, view);
        initDatas();
initViews();
        return view;
    }

    private void initViews() {
        //初始化dialog
        dialog=new CustomDialog(getActivity(),LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT
                ,R.layout.item_girl_dialog,R.style.Theme_dialog,Gravity.CENTER,R.style.pop_anim_style);
       iv = dialog.findViewById(R.id.iv_girl_item);
    }

    private void initDatas() {
        final int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        final int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        RxVolley.get(StaticClass.URL_GIRLS, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (!TextUtils.isEmpty(t)) {
                    Log.e("TAG", "onSuccess() called with: t = [" + t + "]");
                    GirlBean girlBean = new Gson().fromJson(t, GirlBean.class);

                    for (int i = 0; i < girlBean.getData().size(); i++) {
                        GirlBean.DataBean dataBean = girlBean.getData().get(i);
                        data.add(dataBean);
                        mListUrl.add(dataBean.getUrl());
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mAdapter = new CommonBaseAdapter<GirlBean.DataBean>(getActivity(), data, R.layout.item_girl) {
                    @Override
                    public void convert(ViewHolder holder, GirlBean.DataBean dataBean) {
                        holder.setText(R.id.tv_girl_title, dataBean.getPublishedAt());
                        holder.setImageUrlSize(R.id.iv_girl, dataBean.getUrl(), width / 2, height / 5);

                    }
                };
                gvGirl.setAdapter(mAdapter);
            }
        });
        gvGirl.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //加载图片
        Picasso.with(getActivity()).load(mListUrl.get(position)).into(iv);
        //缩放
        mAttecher=new PhotoViewAttacher(iv);
        //刷新
        mAttecher.update();
        dialog.show();


    }
}
