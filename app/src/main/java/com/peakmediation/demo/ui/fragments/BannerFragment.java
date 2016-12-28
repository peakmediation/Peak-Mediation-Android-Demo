package com.peakmediation.demo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.peak.PeakAsyncAdRequest;
import com.peak.PeakSdk;
import com.peakmediation.demo.R;

import static com.peakmediation.demo.PeakSdkConfig.BANNER_ID;

public class BannerFragment extends Fragment implements View.OnClickListener {

    private Button btnShowBanner;
    private Button btnShowBannerAsync;
    private Button btnRemoveBanner;
    private FrameLayout bannerContainer;
    private ProgressBar bannerProgressBar;
    private PeakAsyncAdRequest asyncAdRequest;
    private PeakAsyncAdRequest.PeakAsyncAdRequestListener peakAsyncAdRequestListener =
            new PeakAsyncAdRequest.PeakAsyncAdRequestListener() {
                @Override
                public void onAdReady(String adZoneId) {
                    bannerProgressBar.setVisibility(View.GONE);
                    showBanner();
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner_ad, container, false);
        setupViews(view);
        return view;
    }

    public void setupViews(View root) {
        btnShowBanner = (Button) root.findViewById(R.id.show_banner_btn);
        btnShowBannerAsync = (Button) root.findViewById(R.id.show_banner_async_btn);
        btnRemoveBanner = (Button) root.findViewById(R.id.remove_banner_btn);
        bannerContainer = (FrameLayout) root.findViewById(R.id.banner_container);
        bannerProgressBar = (ProgressBar) root.findViewById(R.id.banner_progressBar);

        btnShowBanner.setOnClickListener(this);
        btnShowBannerAsync.setOnClickListener(this);
        btnRemoveBanner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_banner_btn:
                showBanner();
                break;
            case R.id.show_banner_async_btn:
                showBannerAsync();
                break;
            case R.id.remove_banner_btn:
                removeBanner();
                break;
        }
    }

    private void removeBanner() {
        if (asyncAdRequest != null) {
            asyncAdRequest.cancel();
            bannerProgressBar.setVisibility(View.GONE);
        }
        if (bannerContainer.getChildCount() > 0) {
            bannerContainer.removeAllViews();
        }
    }

    private void showBannerAsync() {
        if (asyncAdRequest != null) {
            asyncAdRequest.cancel();
        }
        asyncAdRequest = PeakSdk.createAdRequest(BANNER_ID);
        if (asyncAdRequest != null) {
            bannerProgressBar.setVisibility(View.VISIBLE);
            asyncAdRequest.start(peakAsyncAdRequestListener);
        }
    }

    private void showBanner() {
        removeBanner();
        View bannerView = PeakSdk.showBanner(BANNER_ID);
        if (bannerView != null) {
            bannerContainer.addView(bannerView);
        }
    }

    @Override
    public void onDestroy() {
        if (asyncAdRequest != null) {
            asyncAdRequest.cancel();
        }
        super.onDestroy();
    }
}
