package com.mnnyang.gzuclassschedule.mvp.mg;

import com.mnnyang.gzuclassschedule.BasePresenter;
import com.mnnyang.gzuclassschedule.BaseView;
import com.mnnyang.gzuclassschedule.data.bean.CsItem;
import com.mnnyang.gzuclassschedule.data.beanv2.CourseGroup;
import com.mnnyang.gzuclassschedule.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 17-11-4.
 */

public interface MgContract {
    interface Presenter extends BasePresenter {
        void deleteCsName(long csNameId);
        void switchCsName(long csNameId);
        void reloadCsNameList();
        void addCsName(String csName);
        void editCsName(long id, String newCsName);
    }

    interface View extends BaseView<Presenter> {
        void showList();
        void showNotice(String notice);
        void gotoCourseActivity();
        void deleteFinish();
        void addCsNameSucceed();
        void editCsNameSucceed();
    }

}
