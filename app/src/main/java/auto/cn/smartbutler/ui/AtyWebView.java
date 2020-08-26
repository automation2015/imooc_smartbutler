package auto.cn.smartbutler.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.base.BaseActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

public class AtyWebView extends BaseActivity {

    @Bind(R.id.pb_webview)
    ProgressBar pbWebview;
    @Bind(R.id.wv)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_web_view);
        ButterKnife.bind(this);
        initDatas();
    }

    private void initDatas() {
        final String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
        //支持Js
        wv.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        //接口回调
        wv.setWebChromeClient(new WebViewClient() );
         //加载网页
        wv.loadUrl(url);
        //本地显示
        wv.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //接受这个事件
                return true;
            }
        });
    }

  public class WebViewClient extends WebChromeClient{
        //进度变化的监听

      @Override
      public void onProgressChanged(WebView view, int newProgress) {
          if(newProgress==100){
              pbWebview.setVisibility(View.GONE);

          }
          super.onProgressChanged(view,newProgress);
      }
  }
}
