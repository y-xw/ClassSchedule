package com.mnnyang.gzuclassschedule.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by mnnyang on 17-11-2.
 */

public class TimeUtils {
    /**
     * 返回某一周的第一周距离现在的实际周数
     *
     * @param weekBeginMillis
     * @param endMillis
     * @return
     */
    public static int getWeekGap(long weekBeginMillis, long endMillis) {
        return (int) (((endMillis - weekBeginMillis) / (1000 * 3600 * 24)) / 7);
    }

    /**
     * 获取本周周一的日期
     *
     * @return
     */
    public static Date getNowWeekBegin() {
        return getThisWeekMonday(new Date());
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int getNowMonth() {
        Calendar calendar = Calendar.getInstance();
        return 1 + calendar.get(Calendar.MONTH);
    }
}
