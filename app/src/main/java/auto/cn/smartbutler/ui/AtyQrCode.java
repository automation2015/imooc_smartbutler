package auto.cn.smartbutler.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import auto.cn.smartbutler.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 生成二维码
 */
public class AtyQrCode extends Activity {

    @Bind(R.id.iv_qr_code)
    ImageView ivQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_qr_code);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int width = getResources().getDisplayMetrics().widthPixels;
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("智能管家",
                width / 2, width / 2,BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        ivQrCode.setImageBitmap(qrCodeBitmap);
    }
}
