package com.mnnyang.gzuclassschedule.mvp.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.utils.ActivityUtil;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressLint("RestrictedApi")
        ContentFrameLayout contentFrameLayout = new ContentFrameLayout(this);
        setContentView(contentFrameLayout);

//        setContentView(R.layout.date_select);

        ActivityUtil.replaceFragmentToActivity(getSupportFragmentManager(),
                HomeFragment.newInstance(), android.R.id.content);
    }

    @Override
    protected boolean canInitTheme() {
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch (item.getItemId()) {
        //    case android.R.id.home:
        //        finish();
        //        break;
        //}
        return super.onOptionsItemSelected(item);
    }
}
