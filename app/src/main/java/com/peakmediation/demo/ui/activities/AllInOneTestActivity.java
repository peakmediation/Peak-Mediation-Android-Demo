package com.peakmediation.demo.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.peak.PeakGender;
import com.peak.PeakSdk;
import com.peak.PeakSdkUiHelper;
import com.peakmediation.demo.PeakSdkDemoListener;
import com.peakmediation.demo.R;
import com.peakmediation.demo.ui.fragments.BannerFragment;
import com.peakmediation.demo.ui.fragments.MainFragment;
import com.peakmediation.demo.ui.fragments.NativeAdFragment;

public class AllInOneTestActivity extends AppCompatActivity {

    public PeakSdkUiHelper uiHelper = new PeakSdkUiHelper(AllInOneTestActivity.this);
    private static final String TAG = "AllInOneTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one_test);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setSaveEnabled(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setCurrentItem(2);
    }

    public void initializePeakSdk(String appId, String targetingAge, String gender) {
        if (targetingAge != null && !targetingAge.isEmpty()) {
            PeakSdk.setTargetingAge(Integer.parseInt(targetingAge));
        }
        if (gender != null && !gender.isEmpty()) {
            PeakGender targetingGender;
            if ("Male".equals(gender)) {
                targetingGender = PeakGender.MALE;
            } else if ("Female".equals(gender)) {
                targetingGender = PeakGender.FEMALE;
            } else {
                targetingGender = PeakGender.UNSPECIFIED;
            }
            PeakSdk.setTargetingGender(targetingGender);
        }
        PeakSdk.initialize(appId, this, new PeakSdkDemoListener(this, "PeakSdk"));
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        int numberOfTabs;

        PagerAdapter(FragmentManager fm, int numberOfTabs) {
            super(fm);
            this.numberOfTabs = numberOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BannerFragment();
                case 1:
                    return new NativeAdFragment();
                case 2:
                    return new MainFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numberOfTabs;
        }
    }

    @Override
    protected void onResume() {
        uiHelper.resume();
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        uiHelper.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        uiHelper.pause();
        super.onPause();
    }
}
