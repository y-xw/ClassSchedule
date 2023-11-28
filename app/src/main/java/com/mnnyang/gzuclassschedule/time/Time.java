package com.mnnyang.gzuclassschedule.time;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mnnyang.gzuclassschedule.BaseActivity;
import com.mnnyang.gzuclassschedule.R;

import java.util.Calendar;
import java.util.Date;

public class Time extends BaseActivity implements View.OnClickListener{
    private DatePicker dp;
    public int year,month,day;
    long selectedDateMillis;
    DatePickerDialog datePickerDialog;
    Calendar calendar;

    private static final String TAG = "Time";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_select);
        calendar = Calendar.getInstance();

        if (dp == null) {
            dp = findViewById(R.id.start_week_begin_date);
        }

        if (findViewById(R.id.start_week_begin_date) == null) {
            Log.e(TAG, "Failed to find the DatePicker view");
            return;
        }Log.e(TAG, "Find the DatePicker view");

        dp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start_week_begin_date){
            showDatePickDlg();
        }
    }
    public void showDatePickDlg () {
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Time.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Time.this.year=year;
                Time.this.month=monthOfYear;
                Time.this.day=dayOfMonth;

                Log.d(TAG, "in onDateSet");

                Date selectedDate = new Date(year, monthOfYear, dayOfMonth);
                setSelectedDateMillis(selectedDate.getTime());

                String test = String.valueOf(getSelectedDateMillis());
                Log.d(TAG, test);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public long getSelectedDateMillis() {
        return selectedDateMillis;
    }

    public void setSelectedDateMillis(long selectedDateMillis) {
        this.selectedDateMillis = selectedDateMillis;
    }

    //    int year,month,day;
//    long selectedDateMillis;
//    DatePicker datePicker;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_home);
//        datePicker=findViewById(R.id.start_week_begin_date);
//        Calendar calendar=Calendar.getInstance();
//        /**
//         * 初始化时获得日期
//         */
//        year=calendar.get(Calendar.YEAR);
//        month=calendar.get(Calendar.MONTH);
//        day=calendar.get(Calendar.DAY_OF_MONTH);
//
//
//        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker datePicker, int year1, int month1, int day1) {
//                /**
//                 *year1,month1,day1是改变后获取的新日期
//                 */
//                Time.this.year=year1;
//                Time.this.month=month1;
//                Time.this.day=day1;
//
//                Date selectedDate = new Date(year1, month1, day1);
//                setSelectedDateMillis(selectedDate.getTime());
//
//                Time.this.setTitle(year1 + " - " +  month1 + " - " + day1);
////                show(year,month,day);
//            }
//        });
//    }
//    private void show(int i, int i1, int i2) {
//        String  str=i+"年"+(1+i1)+"月"+i2+'日';
//        //用Toast显示变化后的日期
//        Toast.makeText(Time.this,str,Toast.LENGTH_SHORT).show();
//    }
//
//    public long getSelectedDateMillis() {
//        return selectedDateMillis;
//    }
//
//    public void setSelectedDateMillis(long selectedDateMillis) {
//        this.selectedDateMillis = selectedDateMillis;
//    }
}
