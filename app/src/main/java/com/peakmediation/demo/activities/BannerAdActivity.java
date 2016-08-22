package com.peakmediation.demo.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.peak.PeakSdk;
import com.peak.PeakSdkListener;
import com.peak.PeakSdkUiHelper;
import com.peakmediation.demo.PeakSdkConfig;
import com.peakmediation.demo.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.peakmediation.demo.PeakSdkConfig.BANNER_ID;

public class BannerAdActivity extends AppCompatActivity {

    private static final String LOG_TAG = BannerAdActivity.class.getName();

    private static final int BANNER_AVAILABILITY_CHECK_DELAY_SECONDS = 1;
    private static final int ACTIVITY_FINISH_IF_AD_NOT_SHOWN_DELAY = 30 * 1000;

    private final ScheduledExecutorService bannerAdAvailabilityExecutor =
            Executors.newSingleThreadScheduledExecutor();

    private final PeakSdkUiHelper uiHelper = new PeakSdkUiHelper(this);

    private final PeakSdkListener peakSdkListener = new PeakSdkLogListener(LOG_TAG);

    private final Handler uiThreadHandler = new Handler();
    private ViewGroup rootView;
    private View banner;

    private ProgressBar progressBar;

    private final Runnable finishActivityRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isBannerShown()) {
                Toast.makeText(BannerAdActivity.this, R.string.error_banner_ad_not_provided,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        findViews();
        PeakSdk.initialize(PeakSdkConfig.APP_ID, uiHelper, peakSdkListener);
        bannerAdAvailabilityExecutor.scheduleWithFixedDelay(
                getShowBannerRunnable(), BANNER_AVAILABILITY_CHECK_DELAY_SECONDS,
                BANNER_AVAILABILITY_CHECK_DELAY_SECONDS, TimeUnit.SECONDS);
        uiThreadHandler.postDelayed(finishActivityRunnable, ACTIVITY_FINISH_IF_AD_NOT_SHOWN_DELAY);
    }

    private void findViews() {
        rootView = (ViewGroup) findViewById(R.id.rootView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.resume();
    }

    @Override
    protected void onPause() {
        uiHelper.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.destroy();
    }

    private Runnable getShowBannerRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    bannerAdAvailabilityExecutor.shutdownNow();
                }
                if (isBannerShown()) {
                    return;
                }
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showBanner();
                    }
                });
            }
        };
    }

    private boolean isBannerShown() {
        return banner != null;
    }

    private void showBanner() {
        if (banner != null) {
            return;
        }
        banner = PeakSdk.showBanner(BANNER_ID);
        if (banner != null && !isFinishing()) {
            progressBar.setVisibility(View.GONE);
            addBannerToScreen();
        }
    }

    private void addBannerToScreen() {
        RelativeLayout bannerContainer = new RelativeLayout(this);
        FrameLayout.LayoutParams bannerContainerLayoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerContainerLayoutParams.gravity = Gravity.BOTTOM;
        bannerContainer.setLayoutParams(bannerContainerLayoutParams);
        RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bannerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        bannerContainer.addView(banner, bannerLayoutParams);
        rootView.addView(bannerContainer);
    }

}
