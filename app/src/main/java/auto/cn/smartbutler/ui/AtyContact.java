package auto.cn.smartbutler.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import auto.cn.smartbutler.R;
import auto.cn.smartbutler.base.BaseActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 1.获得LoaderManager对象
 * 2.使用LoaderManager来初始化Loader
 * 3.实现LoaderCallbacks接口
 * 4.在onCreateLoader方法中返回CursorLoader，在onLoadFinished方法中获得加载的数据
 */
public class AtyContact extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.lv_contact)
    ListView lvContact;
    @Bind(R.id.et_contact)
    EditText etContact;
    private LoaderManager loaderManager;
    private SimpleCursorAdapter simpleCursorAdapter;
    private String filterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_contact);
        ButterKnife.bind(this);

        //设置游标适配器
        simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME}, new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lvContact.setAdapter(simpleCursorAdapter);
        loaderManager = getSupportLoaderManager();
        //初始化Loader，参数1：Loader的Id，参数2：给Loader传递的参数，参数3：LoaderCallbacks接口
        loaderManager.initLoader(0, null, this);
        //联系人筛选文本框内容改变后，重新夹杂Loader的数据
        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterName = s.toString();
                //重新创建Loader
                loaderManager.restartLoader(0,null,AtyContact.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 创建Loader对象
     * @param i
     * @param bundle
     * @return
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        if (!TextUtils.isEmpty(filterName)){
            uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(filterName));
        }
        CursorLoader cursorLoader = new CursorLoader(AtyContact.this, uri,
                null, null, null, null);
        return cursorLoader;
    }

    /**
     * 加载数据完成回调
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //在主线程完成，更新UI
        Cursor oldCursor = simpleCursorAdapter.swapCursor(data);
        if (oldCursor != null) {
            oldCursor.close();
        }

    }

    /**
     * 重置Loader
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //释放当前游标
        Cursor oldCursor = simpleCursorAdapter.swapCursor(null);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }
}
