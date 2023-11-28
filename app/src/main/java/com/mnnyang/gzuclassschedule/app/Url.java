package com.mnnyang.gzuclassschedule.app;

/**
 * Created by mnnyang on 17-10-23.
 */

public class Url {

    /**
     * HOST
     */
    public static final String URL_HOST = "http://md.xxyangyoulin.cn:80/";
    /**
     * 上传course
     */
    public static final String URL_UPLOAD_COURSE = URL_HOST + "main/upload_course/";
    /**
     * 分享course
     */
    public static final String URL_SHARE = URL_HOST + "main/share/";
    /**
     * 下载course
     */
    public static final String URL_DOWN_COURSE = URL_HOST + "main/down_course/";
    /**
     * 登录
     */
    public static final String URL_LOGIN = URL_HOST + "user/login/";
    /**
     * 注册
     */
    public static final String URL_REGISTER = URL_HOST + "user/register/";
    /**
     * 用户信息
     */
    public static final String URL_USER_INFO = URL_HOST + "user/info/";

    /**
     * 找回密码
     */
    public static final String URL_RETRIEVE_PASSWORD = URL_HOST + "user/password/";
    /**
     * 反馈
     */
    public static final String URL_FEEDBACK = URL_HOST + "main/feedback/";

    /**
     * app更新
     */
    public static final String URL_CHECK_UPDATE_APP = URL_HOST + "main/check_update/";
    /**
     * app更新网页
     */
    public static final String URL_UPDATE_WEB = URL_HOST;

    /**
     * 教务管理系统
     */
    public static final String URL_SWU_HOST = "https://uaaap.swu.edu.cn/cas/login?service=http%3A%2F%2Fi.swu.edu.cn%2FPersonalApplications%2FviewPageV3";

    public static final String CheckCode = "CheckCode.aspx";
    public static final String xskbcx = "xskbcx.aspx";
    public static final String default2 = "default2.aspx";

    public static final String PARAM_XH = "xh";
    public static final String PARAM_XND = "xnd";
    public static final String PARAM_XQD = "xqd";

    public static final String __VIEWSTATE = "__VIEWSTATE";
    public static String VIEWSTATE_POST_CODE = "";
    public static String VIEWSTATE_LOGIN_CODE = "";
}
