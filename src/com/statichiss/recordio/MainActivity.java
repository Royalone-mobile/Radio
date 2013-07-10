package com.statichiss.recordio;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.statichiss.R;
import com.statichiss.recordio.fragments.PlayerFragment;
import com.statichiss.recordio.fragments.RecordingsFragment;
import com.statichiss.recordio.fragments.ScheduleFragment;

import java.util.List;
import java.util.Vector;

/**
 * Created by chris on 20/06/2013.
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;
    private static int tabIndex;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager());

//        mTabHost.addTab(mTabHost.newTabSpec("battery").setIndicator("Battery",
//                getResources().getDrawable(R.drawable.ic_battery_tab)),
//                BatteryFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("player").setIndicator("Player"), PlayerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("schedule").setIndicator("Schedule"), ScheduleFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("recordings").setIndicator("Recordings"), RecordingsFragment.class, null);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ComponentName remoteControlReceiver = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());

//        if (savedInstanceState != null && savedInstanceState.getInt("CurrentTab") > 0) {
//            savedInstanceState.getInt("CurrentTab");
//        }
        mTabHost.setCurrentTab(tabIndex);
        mTabHost.setOnTabChangedListener(this);

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, PlayerFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, ScheduleFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RecordingsFragment.class.getName()));

        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        this.mViewPager = (ViewPager) findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        if (preferences.getBoolean(getString(R.string.first_run_flag), true)) {
            ShowFirstRunPopUp();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.first_run_flag), false);
            editor.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabIndex = mTabHost.getCurrentTab();
        outState.putInt("CurrentTab", tabIndex);
        super.onSaveInstanceState(outState);
    }

    public void showTab(int i) {
        mTabHost.setCurrentTab(i);
    }

    private void ShowFirstRunPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.first_run_popup);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setCancelable(true);

        TextView popUpText = (TextView) dialog.findViewById(R.id.popUpText);
        popUpText.setText(R.string.first_run_text);

        Button cancelButton = (Button) dialog.findViewById(R.id.popUpCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int index) {
        this.mTabHost.setCurrentTab(index);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabChanged(String s) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }
}