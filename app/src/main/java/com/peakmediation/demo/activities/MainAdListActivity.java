package com.peakmediation.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.peak.PeakSdk;
import com.peak.PeakSdkListener;
import com.peak.PeakSdkUiHelper;
import com.peakmediation.demo.PeakSdkConfig;
import com.peakmediation.demo.R;

public class MainAdListActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainAdListActivity.class.getName();

    private final PeakSdkUiHelper uiHelper = new PeakSdkUiHelper(this);
    private final PeakSdkListener peakSdkListener = new PeakSdkLogListener(LOG_TAG);

    private ListView adTypesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ad_list);
        findViews();
        initViews();
        PeakSdk.initialize(PeakSdkConfig.APP_ID, uiHelper, peakSdkListener);
    }

    private void findViews() {
        adTypesListView = (ListView) findViewById(R.id.adTypesListView);
    }

    private void initViews() {
        adTypesListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.ad_types)));
        adTypesListView.setOnItemClickListener(new OnAdTypesListClickListener());
    }

    @Override
    protected void onPause() {
        uiHelper.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.resume();
    }

    @Override
    protected void onDestroy() {
        uiHelper.destroy();
        super.onDestroy();
    }

    private void showInterstitialAd(String interstitialAdId) {
        if (PeakSdk.checkAdAvailable(interstitialAdId)) {
            PeakSdk.showInterstitial(interstitialAdId);
        }
    }

    private void showStaticInterstitialAd() {
        showInterstitialAd(PeakSdkConfig.STATIC_INTERSTITIAL_ID);
    }

    private void showVideoInterstitialAd() {
        showInterstitialAd(PeakSdkConfig.VIDEO_INTERSTITIAL_ID);
    }

    private void showRewardedVideoAd() {
        showInterstitialAd(PeakSdkConfig.REWARDED_VIDEO_INTERSTITIAL_ID);
    }

    private void showBannerAd() {
        Intent nativeAdIntent = new Intent(this, BannerAdActivity.class);
        ActivityCompat.startActivity(this, nativeAdIntent, null);
    }

    private void showBannerAdAsync() {
        Intent nativeAdIntent = new Intent(this, BannerAdAsyncActivity.class);
        ActivityCompat.startActivity(this, nativeAdIntent, null);
    }


    private void showNativeAd() {
        Intent nativeAdIntent = new Intent(this, NativeAdActivity.class);
        ActivityCompat.startActivity(this, nativeAdIntent, null);
    }

    private class OnAdTypesListClickListener implements AdapterView.OnItemClickListener {

        private static final int BANNER_AD_TYPE_POSITION = 0;
        private static final int BANNER_AD_ASYNC_TYPE_POSITION = 1;
        private static final int STATIC_INTERSTITIAL_AD_TYPE_POSITION = 2;
        private static final int VIDEO_INTERSTITIAL_AD_TYPE_POSITION = 3;
        private static final int REWARDED_VIDEO_AD_TYPE_POSITION = 4;
        private static final int NATIVE_AD_TYPE_POSITION = 5;

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case BANNER_AD_TYPE_POSITION:
                    showBannerAd();
                    break;
                case BANNER_AD_ASYNC_TYPE_POSITION:
                    showBannerAdAsync();
                    break;
                case STATIC_INTERSTITIAL_AD_TYPE_POSITION:
                    showStaticInterstitialAd();
                    break;
                case VIDEO_INTERSTITIAL_AD_TYPE_POSITION:
                    showVideoInterstitialAd();
                    break;
                case REWARDED_VIDEO_AD_TYPE_POSITION:
                    showRewardedVideoAd();
                    break;
                case NATIVE_AD_TYPE_POSITION:
                    showNativeAd();
                    break;
            }
        }

    }

}
