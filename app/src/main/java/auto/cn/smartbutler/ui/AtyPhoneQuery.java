package auto.cn.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.base.BaseActivity;
import auto.cn.smartbutler.utils.StaticClass;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class AtyPhoneQuery extends BaseActivity {

    @Bind(R.id.et_query)
    EditText etQuery;
    @Bind(R.id.iv_query_company)
    ImageView ivQueryCompany;
    @Bind(R.id.tv_query_result)
    TextView tvQueryResult;
    @Bind(R.id.btn_query_one)
    Button btnQueryOne;
    @Bind(R.id.btn_query_two)
    Button btnQueryTwo;
    @Bind(R.id.btn_query_three)
    Button btnQueryThree;
    @Bind(R.id.btn_query_del)
    Button btnQueryDel;
    @Bind(R.id.btn_query_four)
    Button btnQueryFour;
    @Bind(R.id.btn_query_five)
    Button btnQueryFive;
    @Bind(R.id.btn_query_six)
    Button btnQuerySix;
    @Bind(R.id.btn_query_zero)
    Button btnQueryZero;
    @Bind(R.id.btn_query_seven)
    Button btnQuerySeven;
    @Bind(R.id.btn_query_eight)
    Button btnQueryEight;
    @Bind(R.id.btn_query_nine)
    Button btnQueryNine;
    @Bind(R.id.btn_query_query)
    Button btnQueryQuery;
private boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_phone_query);
        ButterKnife.bind(this);

    }
    //键盘长按点击事件
    @OnLongClick({R.id.btn_query_del})
    public boolean delLongClick(){
        etQuery.setText("");
        return true;
    }

//键盘点击事件
    @OnClick({R.id.btn_query_one, R.id.btn_query_three, R.id.btn_query_two,
            R.id.btn_query_four, R.id.btn_query_five, R.id.btn_query_six,
            R.id.btn_query_seven, R.id.btn_query_eight, R.id.btn_query_nine
            , R.id.btn_query_zero, R.id.btn_query_del, R.id.btn_query_query})
    public void getPhoneNumClick(View view) {
        String phoneNum = etQuery.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_query_one:
            case R.id.btn_query_two:
            case R.id.btn_query_three:
            case R.id.btn_query_four:
            case R.id.btn_query_five:
            case R.id.btn_query_six:
            case R.id.btn_query_seven:
            case R.id.btn_query_eight:
            case R.id.btn_query_nine:
            case R.id.btn_query_zero:
                if(flag){
                    phoneNum="";
                    etQuery.setText("");
                    flag=false;
                }
                etQuery.setText(phoneNum + ((Button) view).getText());
                //移动光标
                etQuery.setSelection(phoneNum.length()+1);
                break;
            case R.id.btn_query_del:
                if(!TextUtils.isEmpty(phoneNum)&&phoneNum.length()>0){
                    //每次结尾减去1
                    etQuery.setText(phoneNum.substring(0,phoneNum.length()-1));
                    //移动光标
                    etQuery.setSelection(phoneNum.length()-1);
                }
                break;
            case R.id.btn_query_query:

                if(!TextUtils.isEmpty(phoneNum)&&phoneNum.length()>0){
                getPhone(phoneNum);
                }
                break;

        }
    }
//获取归属地
    private void getPhone(String phoneNum) {
        String url="http://apis.juhe.cn/mobile/get?phone="+phoneNum+"&key="+StaticClass.KEY_PHONE ;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Toast.makeText(AtyPhoneQuery.this,""+t,Toast.LENGTH_SHORT).show();
           parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String province = jsonResult.getString("province");
            String city = jsonResult.getString("city");
            String areacode = jsonResult.getString("areacode");
            String zip = jsonResult.getString("zip");
            String company = jsonResult.getString("company");
            String card = jsonResult.getString("card");
            tvQueryResult.setText("归属地："+province+city+"\n"+"区号："+areacode+"\n"+
                    "邮编："+zip+"\n"+"运营商："+company+"\n"+"类型："+card);
            //图片显示
            switch (company){
                case "移动":
                    ivQueryCompany.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    ivQueryCompany.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    ivQueryCompany.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag=true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
