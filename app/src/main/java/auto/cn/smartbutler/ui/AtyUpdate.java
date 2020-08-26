package auto.cn.smartbutler.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;

import auto.cn.smartbutler.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtyUpdate extends Activity {

    @Bind(R.id.tv_size)
    TextView tvSize;
    @Bind(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;
    private String path;
    public static final int HANDLER_OK = 10001;
    public static final int HANDLER_LOADING = 10002;

    public static final int HANDLER_ON = 10003;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_LOADING:
                    //实施更新进度
                    Bundle bundle = msg.getData();
                    long totalSize = bundle.getLong("totalSize");
                    long transferredBytes = bundle.getLong("transferredBytes");
                    tvSize.setText(transferredBytes + "/" + totalSize);
                    //设置进度
                    numberProgressBar.setProgress((int)(((float)transferredBytes/(float)totalSize)*100));
                    break;
                case HANDLER_OK:
                    tvSize.setText("下载成功");
                    //启动下载
                    startInstall();
                    break;
                case HANDLER_ON:

                    tvSize.setText("下载失败");
                    break;
            }
        }
    };

    //启动安装
    private void startInstall() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_update);
        ButterKnife.bind(this);
        numberProgressBar.setMax(100);
        String url = getIntent().getStringExtra("url");
        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        if (!TextUtils.isEmpty(url)) {
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    Message msg = new Message();
                    msg.what = HANDLER_LOADING;
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes", transferredBytes);
                    bundle.putLong("totalSize", totalSize);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    mHandler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    mHandler.sendEmptyMessage(HANDLER_ON);
                }
            });
        } else {

        }
    }
}
