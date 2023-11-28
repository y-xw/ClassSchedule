package com.mnnyang.gzuclassschedule.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.Html5Activity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.data.beanv2.BaseBean;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.event.SignEvent;

import org.greenrobot.eventbus.EventBus;

public class SignActivity extends BaseActivity implements SignContact.View, View.OnClickListener {

    private SignContact.Presenter mPresenter;
    private boolean isLogin = false;
    private TextView mTvBigTitle;
    private TextView mTvSwitch;
    private TextView mTvTip;
    private TextView mBtnGo;
    private TextInputEditText mEtEmail;
    private TextInputEditText mEtPassword;
    private DialogHelper mProgressDialog;
    private View mRetrievePassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        initListener();

        initToSignUp();

        new SignPresenter(this).start();
    }


    @Override
    protected boolean canInitTheme() {
        return false;
    }

    private void initView() {
        mTvBigTitle = findViewById(R.id.tv_big_title);
        mTvSwitch = findViewById(R.id.tv_switch);
        mTvTip = findViewById(R.id.tv_tip);
        mBtnGo = findViewById(R.id.btn_go);

        mEtEmail = findViewById(R.id.tiet_email);
        mEtPassword = findViewById(R.id.tiet_password);

        mRetrievePassword = findViewById(R.id.tv_retrieve_password);

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initListener() {
        mTvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchStatus();
            }
        });

        mRetrievePassword.setOnClickListener(this);

        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();

                if (isLogin) {
                    mPresenter.signIn(email, password);
                } else {
                    mPresenter.signUp(email, password);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchStatus() {
        if (isLogin) {
            initToSignUp();
        } else {
            initToSignIn();
        }
    }

    private void initToSignIn() {
        isLogin = true;
        mTvBigTitle.setText("登 入");
        mTvTip.setText("还没有加入？");
        mTvSwitch.setText("去注册");
    }

    private void initToSignUp() {
        isLogin = false;
        mTvBigTitle.setText("注 册");
        mTvTip.setText("已有账号？");
        mTvSwitch.setText("去登入");
    }

    @Override
    public void setPresenter(SignContact.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showMassage(String msg) {
        toast(msg);
    }

    @Override
    public void showLoading(String msg) {
        mProgressDialog = new DialogHelper();
        mProgressDialog.showProgressDialog(this, "稍等", msg, false);
    }

    @Override
    public void stopLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.hideProgressDialog();
            mProgressDialog = null;
        }
    }

    @Override
    public void signInSucceed(BaseBean bean) {
        toast("登录成功");
        String email = mEtEmail.getText().toString();
        Cache.instance().setEmail(email);

        EventBus.getDefault().post(new SignEvent());
        finish();
    }

    @Override
    public void signInFailed(String msg) {
        toast(msg);
    }

    @Override
    public void signUpSucceed(BaseBean bean) {
        toast("注册成功，请登录");
        initToSignIn();
    }

    @Override
    public void signUpFailed(String msg) {
        toast(msg);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_retrieve_password:
                retrievePassword();
                break;
        }
    }

    private void retrievePassword() {
        Intent intent = new Intent(this, Html5Activity.class);

        Bundle bundle = new Bundle();
        bundle.putString("url",Url.URL_RETRIEVE_PASSWORD);
        bundle.putString("title","找回密码");
        intent.putExtra("bundle",bundle);

        startActivity(intent);
    }
}
