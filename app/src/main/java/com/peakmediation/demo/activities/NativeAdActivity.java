package com.peakmediation.demo.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.peak.PeakSdk;
import com.peak.PeakSdkUiHelper;
import com.peak.nativeads.PeakNativeAd;
import com.peakmediation.demo.PeakSdkConfig;
import com.peakmediation.demo.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.peakmediation.demo.PeakSdkConfig.NATIVE_AD_ID;

public class NativeAdActivity extends AppCompatActivity {

    private static final int NATIVE_AD_AVAILABILITY_CHECK_DELAY_SECONDS = 1;
    private static final int ACTIVITY_FINISH_IF_AD_NOT_SHOWN_DELAY = 30 * 1000;

    private final ScheduledExecutorService nativeAdAvailabilityExecutor =
            Executors.newSingleThreadScheduledExecutor();
    private final Handler uiThreadHandler = new Handler(Looper.getMainLooper());

    private final PeakSdkUiHelper uiHelper = new PeakSdkUiHelper(this);

    private ImageView mainImageView;
    private ImageView logoImageView;
    private ImageView privacyIconImageView;
    private TextView titleTextView;
    private TextView sponsoredTextView;
    private TextView descriptionTextView;
    private Button adActionButton;
    private ProgressBar progressBar;

    private PeakNativeAd nativeAd;

    private final Runnable finishActivityRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isNativeShown()) {
                Toast.makeText(NativeAdActivity.this, R.string.error_native_ad_not_provided,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);
        findViews();
        PeakSdk.initialize(PeakSdkConfig.APP_ID, uiHelper, null);
        nativeAdAvailabilityExecutor.scheduleWithFixedDelay(
                getShowNativeAdRunnable(), NATIVE_AD_AVAILABILITY_CHECK_DELAY_SECONDS,
                NATIVE_AD_AVAILABILITY_CHECK_DELAY_SECONDS, TimeUnit.SECONDS);
        uiThreadHandler.postDelayed(finishActivityRunnable, ACTIVITY_FINISH_IF_AD_NOT_SHOWN_DELAY);
    }

    private Runnable getShowNativeAdRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    nativeAdAvailabilityExecutor.shutdownNow();
                }
                if (isNativeShown()) {
                    return;
                }
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showNativeAd();
                    }
                });
            }
        };
    }

    private boolean isNativeShown() {
        return nativeAd != null;
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
        uiThreadHandler.removeCallbacks(finishActivityRunnable);
        uiHelper.destroy();
        super.onDestroy();
    }

    private void findViews() {
        mainImageView = (ImageView) findViewById(R.id.mainImageView);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        privacyIconImageView = (ImageView) findViewById(R.id.privacyInformationIconImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        sponsoredTextView = (TextView) findViewById(R.id.sponsoredTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        adActionButton = (Button) findViewById(R.id.interactWithAdButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void showNativeAd() {
        if (PeakSdk.checkAdAvailable(NATIVE_AD_ID)) {
            nativeAd = PeakSdk.showNativeAd(NATIVE_AD_ID);
            if (nativeAd != null) {
                progressBar.setVisibility(View.GONE);
                PeakSdk.trackNativeAdShown(NATIVE_AD_ID);
                bindNativeAdToViews(nativeAd);
            }
        }
    }

    private void bindNativeAdToViews(PeakNativeAd nativeAd) {
        Picasso imageLoader = Picasso.with(this);
        fillMainImage(nativeAd, imageLoader);
        fillIcon(nativeAd, imageLoader);
        fillPrivacyInformationIcon(nativeAd, imageLoader);
        titleTextView.setText(nativeAd.getTitle());
        descriptionTextView.setText(nativeAd.getText());
        sponsoredTextView.setText(R.string.sponsored);
        adActionButton.setVisibility(View.VISIBLE);
        adActionButton.setText(nativeAd.getActionText());
        adActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeakSdk.handleNativeAdClicked(NATIVE_AD_ID);
            }
        });
    }

    private void fillMainImage(PeakNativeAd nativeAd, Picasso imageLoader) {
        String mainImage = nativeAd.getMainImage();
        if (!TextUtils.isEmpty(mainImage)) {
            imageLoader.load(mainImage).into(mainImageView);
        }
    }

    private void fillIcon(PeakNativeAd nativeAd, Picasso imageLoader) {
        String icon = nativeAd.getIcon();
        if (!TextUtils.isEmpty(icon)) {
            imageLoader.load(icon).into(logoImageView);
        }
    }

    private void fillPrivacyInformationIcon(PeakNativeAd nativeAd, Picasso imageLoader) {
        String privacyIcon = nativeAd.getPrivacyIcon();
        if (!TextUtils.isEmpty(privacyIcon)) {
            imageLoader.load(privacyIcon).into(privacyIconImageView);
        }
    }

}
