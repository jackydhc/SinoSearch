package com.sinoshem.sinosearch.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.sinoshem.corelibrary.mvp.MvpView;
import com.sinoshem.corelibrary.utils.UIUtil;
import com.sinoshem.sinosearch.BuildConfig;
import com.sinoshem.sinosearch.R;
import com.sinoshem.sinosearch.bean.MessageEvent;
import com.sinoshem.sinosearch.utils.ActivityManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by L on 2016/7/26.
 */
public abstract class BaseActivity extends RxAppCompatActivity implements MvpView {

    public static final String EXTRA_START_MAIN = "start_main";

    public boolean isOnPause;

    @Nullable @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @Nullable @BindView(R.id.toolbar_title) protected TextView toolbar_title;

    @Override protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (toolbar_title != null) {
            toolbar_title.setText(title);
        }
    }

    public void setTitleTextColor(@IdRes int color) {
        if (toolbar_title != null) {
            toolbar_title.setTextColor(color);
        }
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected boolean interceptInit() {
        return false;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (interceptInit()) {
            return;
        }

        //transparent();
        int id = provideLayoutId();
        if (id <= 0) {
            return;
        }
        setContentView(id);
        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            ActionBar actionBar = getSupportActionBar();
            toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_back_light);
                initActionBar(actionBar);
            }
        }

        initOnCreate(savedInstanceState);
        EventBus.getDefault()
                .register(this);
        ActivityManager.get()
                .add(this);
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void initActionBar(ActionBar actionBar) {
    }
    public abstract int provideLayoutId();

    protected abstract void initOnCreate(Bundle savedInstanceState);

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override protected void onResume() {
        super.onResume();
        isOnPause = false;
        MobclickAgent.onResume(this);
    }

    @Override protected void onPause() {
        isOnPause = true;
        UIUtil.hideKeyboard(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override protected void onDestroy() {
        fixInputMethodManagerLeak(this);
        EventBus.getDefault()
                .unregister(this);
        ActivityManager.get()
                .remove(this);
        // Unbind from the service
        dismissCustomDialog();
        super.onDestroy();
    }

    /**
     * 子类如有使用自定义Dialog，必须在此方法中进行关闭
     */
    protected void dismissCustomDialog() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUserEvent(MessageEvent messageEvent) {
        // FIXME: 2017/4/18 empty body
    }

    @Override public boolean isAlive() {
        return !isFinishing();
    }



    public static void startIntent(Context context, Class aClass) {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);
    }

    public static Intent gIntent(Context context, Class aClass) {
        return new Intent(context, aClass);
    }

    @Override public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 修复InputMethodManager造成的内存泄漏
     */
    protected void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        try {
            String[] arr = new String[] { "mCurRootView", "mServedView", "mNextServedView", "mLastSrvView" };
            Field f;
            Object obj_get;
            int len = arr.length;
            for (int i = 0; i < len; i++) {
                String param = arr[i];
                f = imm.getClass()
                        .getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == context) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑，也就不用继续for循环了
                        break;
                    }
                }

            }
        } catch (Throwable t) {
            if (BuildConfig.DEBUG) {
                t.printStackTrace();
            }
        }
    }


    @Override public void finish() {
        super.finish();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra(EXTRA_START_MAIN, false)) {
//                MainActivity.startToMain(this);
            }
        }
    }
}
