package com.sinoshem.sinosearch.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sinoshem.sinosearch.R;
import com.sinoshem.sinosearch.bean.MessageEvent;
import com.sinoshem.sinosearch.bean.MessageEventCode;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaozhe on 2017/6/13.
 */
public class ContainerOverlayToolbarActivity extends ContainerToolbarActivity {


    public static Intent newInstance(Context context, String title, String fname) {
        return new Intent(context, ContainerOverlayToolbarActivity.class).putExtra(TITLE, title)
                .putExtra(FRAGMENT_NAME, fname);
    }

    public static Intent newInstance(Context context, String title, String fname, Bundle bundle) {
        return newInstance(context, title, fname, bundle, false);
    }

    public static Intent newInstance(Context context, String title, String fname, Bundle bundle, boolean startMain) {
        return new Intent(context, ContainerOverlayToolbarActivity.class).putExtra(TITLE, title)
                .putExtra(FRAGMENT_NAME, fname)
                .putExtra(FRAGMENT_BUNDLE, bundle)
                .putExtra(BaseActivity.EXTRA_START_MAIN, startMain);
    }

    @Override public int provideLayoutId() {
        return R.layout.activity_container_toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_action_share){
            EventBus.getDefault()
                    .post(new MessageEvent(MessageEventCode.FLASH_SHARE));
        }
        return super.onOptionsItemSelected(item);
    }
}
