package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.AppointHelpPagerAdapter;
import cn.reservation.app.baixingxinwen.fragment.AppointAlarmViewFragment;
import cn.reservation.app.baixingxinwen.fragment.LineAlarmFragment;
import cn.reservation.app.baixingxinwen.fragment.ReportAlarmFragment;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.CustomTabView2;

public class AppointHelpActivity extends AppCompatActivity implements AppointAlarmViewFragment.OnFragmentInteractionListener,
        LineAlarmFragment.OnFragmentInteractionListener, ReportAlarmFragment.OnFragmentInteractionListener, TabLayout.OnTabSelectedListener {

    private Context mContext;
    AnimatedActivity pActivity;
    private Resources res;
    public ViewPager simpleViewPager;
    public TabLayout tabLayout;
    public String[] mTabTitles = new String[3];
    public TextView mActionbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_help);

        mContext = TabHostActivity.TabHostStack;
        pActivity = (AnimatedActivity) AppointHelpActivity.this.getParent();
        res = mContext.getResources();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.appoint_alarm));
        mActionbarTitle = (TextView) findViewById(R.id.actionbar_title);

        mTabTitles[0] = res.getString(R.string.appoint_alarm);
        mTabTitles[1] = res.getString(R.string.line_alarm);
        mTabTitles[2] = res.getString(R.string.report_alarm);

        // get the reference of ViewPager and TabLayout
        simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        for(int i=0; i<3; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            CustomTabView2 tabView = new CustomTabView2(mContext, mTabTitles[i], i==0);
            tab.setCustomView(tabView);
            tabLayout.addTab(tab);
        }

        AppointHelpPagerAdapter adapter = new AppointHelpPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);
        //tabLayout.addOnTabSelectedListener(this);
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            pActivity.finishChildActivity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                //super.onBackPressed();
                pActivity.finishChildActivity();
            } else {
                getSupportFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        simpleViewPager.setCurrentItem(tab.getPosition());
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            boolean selected = tab.getPosition() == i;
            CustomTabView2 view = (CustomTabView2) tabLayout.getTabAt(i).getCustomView();
            view.setSelected(selected);
        }
        mActionbarTitle.setText(mTabTitles[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
