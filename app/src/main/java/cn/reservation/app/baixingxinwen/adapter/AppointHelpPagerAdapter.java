package cn.reservation.app.baixingxinwen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.reservation.app.baixingxinwen.fragment.AppointAlarmViewFragment;
import cn.reservation.app.baixingxinwen.fragment.LineAlarmFragment;
import cn.reservation.app.baixingxinwen.fragment.ReportAlarmFragment;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

/**
 * Created by LiYin on 3/19/2017.
 */
public class AppointHelpPagerAdapter extends FragmentStatePagerAdapter {
    public int mNumOfTabs;

    public AppointHelpPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            /*
            case 0:
                AppointAlarmViewFragment tab1 = AppointAlarmViewFragment.newInstance(CommonUtils.userInfo.getUserID());
                return tab1;
            case 1:
                LineAlarmFragment tab2 = LineAlarmFragment.newInstance(CommonUtils.userInfo.getUserID());
                return tab2;
            case 2:
                ReportAlarmFragment tab3 = ReportAlarmFragment.newInstance(CommonUtils.userInfo.getUserID());
                return tab3;
            */
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
