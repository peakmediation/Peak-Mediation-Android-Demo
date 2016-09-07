package com.peakmediation.demo.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.peak.PeakAsyncAdRequest;
import com.peak.PeakSdk;
import com.peak.PeakSdkListener;
import com.peak.PeakSdkUiHelper;
import com.peakmediation.demo.PeakSdkConfig;
import com.peakmediation.demo.R;

public class BannerAdAsyncActivity extends AppCompatActivity {

    private static final String LOG_TAG = BannerAdAsyncActivity.class.getName();

    private static final int ACTIVITY_FINISH_IF_AD_NOT_SHOWN_DELAY = 30 * 1000;

    private final PeakSdkUiHelper uiHelper = new PeakSdkUiHelper(this);

    private final PeakSdkListener peakSdkListener = new PeakSdkLogListener(LOG_TAG);

    private Handler uiThreadHandler = new Handler();
    private ViewGroup rootView;
    private View banner;

    private ProgressBar progressBar;

    private PeakAsyncAdRequest asyncAdRequest;
    private PeakAsyncAdRequest.PeakAsyncAdRequestListener asyncAdRequestListener =
            new PeakAsyncAdRequest.PeakAsyncAdRequestListener() {

        @Override
        public void onAdReady(String adZoneId) {
            if (banner != null) {
                return;
            }
            if (PeakSdk.checkAdAvailable(adZoneId)) {
                banner = PeakSdk.showBanner(adZoneId);
                if (banner != null && !isFinishing()) {
                    progressBar.setVisibility(View.GONE);
                    addBannerToScreen();
                }
            }
        }

    };

    private Runnable finishActivityRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isBannerShown()) {
                Toast.makeText(BannerAdAsyncActivity.this, R.string.error_banner_ad_not_provided,
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
        initializeActionBar();
        PeakSdk.initialize(PeakSdkConfig.APP_ID, uiHelper, peakSdkListener);
        startAdRequest();
        uiThreadHandler.postDelayed(finishActivityRunnable, ACTIVITY_FINISH_IF_AD_NOT_SHOWN_DELAY);
    }

    private void startAdRequest() {
        asyncAdRequest = PeakSdk.createAdRequest(PeakSdkConfig.BANNER_ID);
        if (asyncAdRequest != null) {
            asyncAdRequest.start(asyncAdRequestListener);
        }
    }

    private void findViews() {
        rootView = (ViewGroup) findViewById(R.id.rootView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean isBannerShown() {
        return banner != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.resume();
    }

    @Override
    protected void onPause() {
        asyncAdRequest.cancel();
        uiHelper.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        uiHelper.destroy();
        super.onDestroy();
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

