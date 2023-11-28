package com.mnnyang.gzuclassschedule.mvp.impt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.app.Constant;
import com.mnnyang.gzuclassschedule.app.Url;
import com.mnnyang.gzuclassschedule.mvp.course.CourseActivity;
import com.mnnyang.gzuclassschedule.custom.EditTextLayout;
import com.mnnyang.gzuclassschedule.data.bean.CourseTime;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.LogUtil;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.event.CourseDataChangeEvent;
import com.mnnyang.gzuclassschedule.utils.spec.ShowTermDialog;

import org.greenrobot.eventbus.EventBus;

public class ImptActivity extends BaseActivity implements
        ImptContract.View, View.OnClickListener {

    ImptContract.Presenter mPresenter;
//    private ImageView mIvCaptcha;
    private String mXh;
    private DialogHelper mHelper;
    private EditTextLayout mEtlXh;
    private EditTextLayout mEtlPwd;
//    private EditTextLayout mEtlCaptcha;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ScreenUtils.setSystemBarTransparent(this);
        setContentView(R.layout.activity_impt);

        initBackToolbar("导入课程表");
        initView();

        new ImptPresenter(this, initSchoolUrl());
    }

    private String initSchoolUrl() {
        Intent intent = getIntent();
        String schoolUrl = intent.getStringExtra(Constant.INTENT_SCHOOL_URL);
        if (TextUtils.isEmpty(schoolUrl)) {
            schoolUrl = Url.URL_SWU_HOST;
        }
        schoolUrl = schoolUrl.replace(Url.default2, "");
        return schoolUrl;
    }

    private void initView() {
//        mIvCaptcha = (ImageView) findViewById(R.id.iv_captcha);
        mEtlXh = findViewById(R.id.etl_xh);
        mEtlPwd = findViewById(R.id.etl_pwd);
//        mEtlCaptcha = findViewById(R.id.etl_captcha);

        Button btnSkip = (Button) findViewById(R.id.btn_skip);
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
//        LinearLayout layoutCaptcha = (LinearLayout) findViewById(R.id.layout_refresh_captcha);

        mEtlXh.setText(Preferences.getString(Constant.XH, ""));
        mEtlPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        btnSkip.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
//        layoutCaptcha.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAction();
    }

    private void startAction() {
        mPresenter.start();
    }

//    @Override
//    public ImageView getCaptchaIV() {
//        return mIvCaptcha;
//    }

    @Override
    public void showImpting() {
        mHelper = new DialogHelper();
        mHelper.showProgressDialog(this, "导入中", "请稍等...", false);
    }

    @Override
    public void hideImpting() {
        LogUtil.d(this, "hideimp");
        if (mHelper != null) mHelper.hideProgressDialog();
    }

//    @Override
//    public void captchaIsLoading(boolean isLoading) {
//        if (getCaptchaIV() == null) {
//            return;
//        }
//
//        if (isLoading) {
//            getCaptchaIV().setImageResource(R.drawable.ic_svg_refresh);
//            RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//
//            rotateAnimation.setDuration(1000);
//            rotateAnimation.setInterpolator(new LinearInterpolator());
//            rotateAnimation.setRepeatCount(-1);
//            getCaptchaIV().startAnimation(rotateAnimation);
//        } else {
//            Animation animation = getCaptchaIV().getAnimation();
//            if (animation != null) {
//                animation.cancel();
//            }
//        }
//    }

    @Override
    public void showErrToast(String errMsg, boolean reLoad) {
        ToastUtils.show(errMsg);
//        if (reLoad) reLoadCaptcha();
    }

//    private void reLoadCaptcha() {
//        mPresenter.getCaptcha();
//    }

    @Override
    public void showSucceed() {
        EventBus.getDefault().post(new CourseDataChangeEvent());
        AppUtils.updateWidget(this);

        ToastUtils.show("导入成功");
        Preferences.putString(Constant.XH, mXh);
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
    }

    String selectedTime, selectedTerm;

    @Override
    public void showCourseTimeDialog(CourseTime ct) {
        new ShowTermDialog().showSelectTimeTermDialog(this,
                ct.years.toArray(new String[0]), new ShowTermDialog.TimeTermCallback() {
                    @Override
                    public void onTimeChanged(String time) {
                        selectedTime = time;
                    }

                    @Override
                    public void onTermChanged(String term) {
                        selectedTerm = term;
                    }

                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        mPresenter.importCustomCourses(selectedTime, selectedTerm);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                skip();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
//            case R.id.layout_refresh_captcha:
//                startAction();
//                break;
        }
    }

    private void confirm() {
        //TODO 数据验证
        mXh = mEtlXh.getText().trim();
        String pwd = mEtlPwd.getText().trim();
        LogUtil.e(this, "passwd=" + pwd);
//        String captcha = mEtlCaptcha.getText().trim();

        mPresenter.loadCourseTimeAndTerm(mXh, pwd);
    }

    private void skip() {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void setPresenter(ImptContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

}
