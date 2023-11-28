package com.mnnyang.gzuclassschedule.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mnnyang.gzuclassschedule.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 对话框工具
 * Created by mnnyang on 17-4-14.
 */

public class DialogHelper {

    private ProgressDialog progressDialog;
    //方便在回调函数外拿到DialogHelper对象关闭mCustomDialog
    private AlertDialog mCustomDialog;

    /**
     * 不确定不可关闭等待对话框<br>
     */
    public void showProgressDialog(Context context, String title, String msg, boolean canceable) {
        hideCustomDialog();

        progressDialog = ProgressDialog.show(context, title, msg, true, canceable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 一般对话框
     */
    public void showNormalDialog(@NonNull Activity activity, @NonNull String title,
                                 @NonNull String massage, @NonNull final DialogListener listener) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(massage)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPositive(dialog, which);
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegative(dialog, which);
                            }
                        })
                .show();
    }

    /**
     * List对话框
     */
    public void showListDialog(@NonNull Activity activity, String title,
                               @NonNull String[] items, @NonNull final DialogListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onItemClick(dialog, which);
            }
        }).show();
    }


    /**
     * 自定义弹框
     */
    public void showCustomDialog(@NonNull Context context, View dialogView, String title,
                                 final DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(dialogView);


        if (title != null) {
            builder.setTitle(title);
        }

        if (listener != null) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onPositive(dialog, which);
                }
            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.onNegative(dialog, which);
                            dialog.dismiss();
                        }
                    });
        }

        //此处设置位置窗体大小
        mCustomDialog = builder.create();

        try {
            Field field = field = mCustomDialog.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);

            //   获得mAlert变量的值
            Object obj = field.get(mCustomDialog);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);

            //   修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj, new ButtonHandler(mCustomDialog));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCustomDialog.show();
    }

    public void showCustomDialog(@NonNull Context context, View dialogView, String title, int dialogWidth,
                                 final DialogListener listener) {
        showCustomDialog(context, dialogView, title, listener);
        Window window = mCustomDialog.getWindow();
        if (window != null) {
            window.setLayout(dialogWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 底部弹窗
     */
    public Dialog buildBottomDialog(Activity activity, View layoutView) {
        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        bottomDialog.setContentView(layoutView);
        ViewGroup.LayoutParams layoutParams = layoutView.getLayoutParams();
        layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
        layoutView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        return bottomDialog;
    }

    /**
     * 底部列表弹窗
     */
    public Dialog buildBottomListDialog(Activity activity, String[] items, final DialogListener listener) {
        ListView listView = new ListView(activity.getApplicationContext());
        listView.setDivider(new ColorDrawable(activity.getResources().getColor(R.color.color_divider)));
        listView.setDividerHeight(1);
        listView.setBackgroundColor(activity.getResources().getColor(R.color.white_f1));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.adapter_bottom_dialog_sytle1, items);
        listView.setAdapter(adapter);


        final Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(bottomDialog, position);
            }
        });

        bottomDialog.setContentView(listView);
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = activity.getResources().getDisplayMetrics().widthPixels;
        listView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        return bottomDialog;
    }


    public void hideCustomDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
            mCustomDialog = null;
        }
    }
}