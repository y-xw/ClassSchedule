package com.mnnyang.gzuclassschedule.mvp.mg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;
import com.mnnyang.gzuclassschedule.app.AppUtils;
import com.mnnyang.gzuclassschedule.app.Cache;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.data.greendao.CourseGroupDao;
import com.mnnyang.gzuclassschedule.mvp.course.CourseActivity;
import com.mnnyang.gzuclassschedule.mvp.mg.adapter.MgAdapter;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;
import com.mnnyang.gzuclassschedule.utils.DialogListener;
import com.mnnyang.gzuclassschedule.utils.Preferences;
import com.mnnyang.gzuclassschedule.utils.ToastUtils;
import com.mnnyang.gzuclassschedule.utils.event.CourseDataChangeEvent;
import com.mnnyang.gzuclassschedule.utils.spec.RecyclerBaseAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MgActivity extends BaseActivity implements MgContract.View, View.OnClickListener {

    private MgContract.Presenter mPresenter;
    private MgAdapter mAdapter;
    List<CourseGroup> csItems = new ArrayList<>();
    private DialogHelper mCsNameDialogHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initBackToolbar(getString(R.string.kb_manage));
        initRecyclerView();
        initFab();

        new MgPresenter(this, csItems).start();
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mAdapter = new MgAdapter(R.layout.layout_item_cs, csItems);

        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                getBaseContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setItemClickListener(new MgAdapter.MgListener() {

            @Override
            public void onEditClick(View view, Long csNameId, RecyclerBaseAdapter.ViewHolder holder) {
                editDialog(csNameId);
            }

            @Override
            public void onDelClick(View view, Long csNameId,
                                   RecyclerBaseAdapter.ViewHolder holder) {
                deleteDialog(csNameId);
            }

            @Override
            public void onItemClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                switchDialog((long) view.getTag());
            }

            @Override
            public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {
                deleteDialog((long) view.getTag());
            }
        });
    }

    private void editDialog(final long id) {
        mCsNameDialogHelper = new DialogHelper();
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_input_course_table_name, null);

        final EditText editText = view.findViewById(R.id.et_course_table_name);
        //default value
        CourseGroup group = Cache.instance().getCourseGroupDao()
                .queryBuilder()
                .where(CourseGroupDao.Properties.CgId.eq(id))
                .unique();

        if (group != null) {
            editText.setHint(group.getCgName());
        }

        mCsNameDialogHelper.showCustomDialog(this, view,
                getString(R.string.please_input_course_table_name), new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog, int which) {
                        super.onNegative(dialog, which);
                        mCsNameDialogHelper = null;
                    }

                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        String courseTableName = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(courseTableName)) {
                            toast(getString(R.string.course_name_can_not_be_empty));
                            return;
                        }

                        mPresenter.editCsName(id, courseTableName);
                    }
                });
    }

    private void deleteDialog(final long id) {
        long csNameId = Preferences.getLong(
                getString(R.string.app_preference_current_cs_name_id), 0);

        if (id == csNameId) {
            toast(getString(R.string.you_can_not_delete_the_current_class_schedule));
            return;
        }

        DialogHelper dh = new DialogHelper();
        dh.showNormalDialog(this, getString(R.string.warning),
                "确认要删除该课表吗?",
                new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        deletingDialog(id);
                    }
                });
    }

    private void deletingDialog(long id) {
        mPresenter.deleteCsName(id);
    }

    private void switchDialog(final long tag) {
        DialogHelper dh = new DialogHelper();
        dh.showNormalDialog(this, getString(R.string.warning),
                "确认要切换到该课表吗?",
                new DialogListener() {
                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        mPresenter.switchCsName(tag);
                        EventBus.getDefault().post(new CourseDataChangeEvent());
                        AppUtils.updateWidget(getBaseContext());
                    }
                });
    }

    @Override
    public void showList() {
        long curNameId = Preferences.getLong(getString(R.string.app_preference_current_cs_name_id), 0);
        mAdapter.setCurrentCsNameIdTag(curNameId);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNotice(String notice) {
        ToastUtils.show(notice);
    }

    @Override
    public void gotoCourseActivity() {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteFinish() {
        mPresenter.reloadCsNameList();
        EventBus.getDefault().post(new CourseDataChangeEvent());
        AppUtils.updateWidget(this);
    }

    @Override
    public void addCsNameSucceed() {
        showNotice(getString(R.string.add_succeed));

        //刷新界面
        mPresenter.reloadCsNameList();

        //添加成功 关闭弹窗
        if (mCsNameDialogHelper != null) {
            mCsNameDialogHelper.hideCustomDialog();
        }
    }

    @Override
    public void editCsNameSucceed() {
        showNotice(getString(R.string.edit_succeed));

        //刷新界面
        mPresenter.reloadCsNameList();

        //关闭弹窗
        if (mCsNameDialogHelper != null) {
            mCsNameDialogHelper.hideCustomDialog();
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                add();
                break;
            default:
                break;
        }
    }

    private void add() {
        mCsNameDialogHelper = new DialogHelper();
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_input_course_table_name, null);
        final EditText editText = view.findViewById(R.id.et_course_table_name);

        mCsNameDialogHelper.showCustomDialog(this, view,
                getString(R.string.please_input_course_table_name), new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog, int which) {
                        super.onNegative(dialog, which);
                        mCsNameDialogHelper = null;
                    }

                    @Override
                    public void onPositive(DialogInterface dialog, int which) {
                        super.onPositive(dialog, which);
                        String courseTableName = editText.getText().toString().trim();
                        mPresenter.addCsName(courseTableName);
                    }
                });
    }

    @Override
    public void setPresenter(MgContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
