package auto.cn.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.view.CustomDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment implements View.OnClickListener {
    private static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    private static final int REQUEST_CODE_CAMERA = 100;
    private static final int REQUEST_CODE_PICTURE = 101;
    private static final int REQUEST_CODE_RESULT = 102;
    @Bind(R.id.edit_user)
    TextView editUser;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_sex)
    EditText etSex;
    @Bind(R.id.et_age)
    EditText etAge;
    @Bind(R.id.et_desc)
    EditText etDesc;
    @Bind(R.id.btn_exit)
    Button btnExit;
    @Bind(R.id.btn_update_ok)
    Button btnUpdateOk;
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    private CustomDialog dialog;
    private Button btnCamera, btnPicture, btnCancel;
    private File tempFile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        //默认是不可点击/不可输入的
        setEnable(false);
        //设置具体的值
       // MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
//        etUsername.setText(userInfo.getUsername());
//        etAge.setText(userInfo.getAge()+"");
//        etSex.setText(userInfo.isSex()?"男":"女");
//        etDesc.setText(userInfo.getDesc());
        etUsername.setText("userInfo.getUsername()");
        etAge.setText("userInfo.getAge()");
        etSex.setText("女");
        etDesc.setText("userInfo.getDesc()");
        //初始化dialog
        dialog = new CustomDialog(getActivity(), 400, 500, R.layout.dialog_photo, R.style.pop_anim_style, Gravity.CENTER, 0);
        dialog.setCancelable(false);

        btnCamera = dialog.findViewById(R.id.btn_camera);
        btnPicture = dialog.findViewById(R.id.btn_picture);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCamera.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    //设置不可点击/不可输入的
    private void setEnable(boolean isEnable) {
        etUsername.setEnabled(isEnable);
        etSex.setEnabled(isEnable);
        etAge.setEnabled(isEnable);
        etDesc.setEnabled(isEnable);
    }

    //退出登录点击事件
    @OnClick(R.id.btn_exit)
    public void logoutClick() {
        //退出登录
        //清除缓存用户对象
        // MyUser.logOut();
        //currentUser为null
        // BmobUser currentUser = MyUser.getCurrentUser(MyUser.class);
       // startActivity(new Intent(getActivity(), AtyLogin.class));
        getActivity().finish();

    }

    //编辑用户点击事件
    @OnClick(R.id.edit_user)
    public void editUserClick() {
        btnUpdateOk.setVisibility(View.VISIBLE);
        setEnable(true);
    }

    //更新按钮点击事件
    @OnClick(R.id.btn_update_ok)
    public void updateClick() {
        //1.拿到输入框的内容
        String username = etUsername.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String sex = etSex.getText().toString().trim();
        //2.判断是否为空
        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(age) && TextUtils.isEmpty(sex) && TextUtils.isEmpty(desc)) {
            Toast.makeText(getActivity(), "输入框内容不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //3.更新
//            MyUser myUser = new MyUser();
//            myUser.setUsername(username);
//            myUser.setAge(Integer.parseInt(age));
//            if (sex == "男") {
//                myUser.setSex(true);
//            } else {
//                myUser.setSex(false);
//            }
//            if (!TextUtils.isEmpty(desc)) {
//                myUser.setDesc(desc);
//            } else {
//                myUser.setDesc("这个人很懒，什么都没有留下！");
//            }
//            BmobUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
//            myUser.update(bmobUser.getObjectId(), new UpdateListener() {
//                @Override
//                public void done(BmobException e) {
//                    if (e == null) {
//                        //修改成功
//                        setEnable(false);
//                        btnUpdateOk.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(), "修改失败！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

    //更换用户头像点击事件
    @OnClick(R.id.profile_image)
    public void selectUserIcon() {
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    //选择照相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
        dialog.dismiss();
    }

    //选择图库
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));

                    break;
                case REQUEST_CODE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case REQUEST_CODE_RESULT:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //删除原先的图片
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profileImage.setImageBitmap(bitmap);
        }

    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null)
            return;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return_data",true);
        startActivityForResult(intent, REQUEST_CODE_RESULT);
    }
}
