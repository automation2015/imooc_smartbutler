package auto.cn.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.adapter.ChatMsgAdapter;
import auto.cn.smartbutler.bean.ChatMsg;
import auto.cn.smartbutler.bean.Result;
import auto.cn.smartbutler.utils.SPUtils;
import auto.cn.smartbutler.utils.StaticClass;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ButlerFragment extends Fragment {
    @Bind(R.id.rlTop)
    RelativeLayout rlTop;
    @Bind(R.id.etInputMsg)
    EditText etInputMsg;
    @Bind(R.id.btnSendMsg)
    Button btnSendMsg;
    @Bind(R.id.rlBottom)
    RelativeLayout rlBottom;
    @Bind(R.id.lvMsg)
    ListView lvMsg;
    private ChatMsgAdapter mAdapter;
    private List<ChatMsg> mDatas;
    private SpeechSynthesizer mTts;
    private boolean isSpeak;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        initData();
        return view;
    }

    //发送信息监听事件
    @OnClick(R.id.btnSendMsg)
    public void sendMsgClick() {
        {
            String toMsg = etInputMsg.getText().toString();
            System.out.println(toMsg);
            if (TextUtils.isEmpty(toMsg)) {
                Toast.makeText(getActivity(), "发送信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            ChatMsg toMessage = new ChatMsg();
            toMessage.setDate(new Date());
            toMessage.setMsg(toMsg);
            toMessage.setType(ChatMsg.Type.OUTCOMING);
            mDatas.add(toMessage);
            mAdapter.notifyDataSetChanged();
            etInputMsg.setText("");
            String url = setParams(toMsg);
            RxVolley.get(url, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(t, Result.class);
                    ChatMsg chatMsg = new ChatMsg();
                    chatMsg.setMsg(result.getText());
                    chatMsg.setDate(new Date());
                    chatMsg.setType(ChatMsg.Type.INCOMING);
                    if (isSpeak){
                    startSpeak(chatMsg.getMsg());}
                    mDatas.add(chatMsg);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void initData() {
        mDatas = new ArrayList<ChatMsg>();
        ChatMsg chatMsg = new ChatMsg("小墨", "你好，小墨为您服务", ChatMsg.Type.INCOMING, new Date());
        mDatas.add(chatMsg);
        mAdapter = new ChatMsgAdapter(getActivity(), mDatas);
        if(isSpeak){
        startSpeak("你好，小墨为您服务");}
        lvMsg.setAdapter(mAdapter);
    }

    //获取图灵机器人的url
    private String setParams(String msg) {
//		此方法用于拼接连接字符串，根据官网接入文档的要求拼接
//		其格式为：URL+?key=KEY&info=msg，其中msg必須轉換為utf-8編碼
        String url = "";
        try {
            url = StaticClass.ROBOT_URL + "?key=" + StaticClass.KEY_ROBOT_API + "&info=" + URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    //初始化View
    private void initViews(View view) {
      isSpeak = SPUtils.getBoolean(getActivity(), "isSpeak", false);
        //1.创建SpeechSynthesizer对象，第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android) SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0-100
        //3.设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm"
        //保存再SD卡需要添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

    }

    //开始说话
    private void startSpeak(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }

    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //开始播放
        @Override
        public void onSpeakBegin() {

        }

        //缓冲进度回调
        //percent为缓冲进度0-100，beginPos为缓冲音频再文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {

        }

        //暂停播放
        @Override
        public void onSpeakPaused() {

        }

        //回复播放回调接口
        @Override
        public void onSpeakResumed() {

        }

        //播放回调进度
        //percent为播放进度0-100，beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置，info为附加信息
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        //会话结束回调接口，没有错误时，error为null
        @Override
        public void onCompleted(SpeechError speechError) {

        }

        //会话事件回调接口
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
