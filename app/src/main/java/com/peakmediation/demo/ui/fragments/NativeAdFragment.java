package com.peakmediation.demo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peak.PeakAsyncAdRequest;
import com.peak.PeakSdk;
import com.peak.nativeads.model.PeakNativeAdModel;
import com.peakmediation.demo.R;
import com.squareup.picasso.Picasso;

import static com.peakmediation.demo.PeakSdkConfig.NATIVE_AD_ID;

public class NativeAdFragment extends Fragment implements View.OnClickListener {

    private Button btnShowNative;
    private Button btnShowNativeAsync;
    private Button btnRemoveNative;
    private FrameLayout nativeContainer;
    private ProgressBar nativeProgressBar;
    private PeakAsyncAdRequest asyncAdRequest;
    private PeakAsyncAdRequest.PeakAsyncAdRequestListener peakAsyncAdRequestListener =
            new PeakAsyncAdRequest.PeakAsyncAdRequestListener() {
                @Override
                public void onAdReady(String adZoneId) {
                    showNative();
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_native_ad, container, false);
        setupViews(view);
        return view;
    }

    public void setupViews(View root) {
        btnShowNative = (Button) root.findViewById(R.id.show_native_btn);
        btnShowNativeAsync = (Button) root.findViewById(R.id.show_native_async_btn);
        btnRemoveNative = (Button) root.findViewById(R.id.remove_native_btn);
        nativeContainer = (FrameLayout) root.findViewById(R.id.native_container);
        nativeProgressBar = (ProgressBar) root.findViewById(R.id.native_progressBar);

        btnShowNative.setOnClickListener(this);
        btnShowNativeAsync.setOnClickListener(this);
        btnRemoveNative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_native_btn:
                showNative();
                break;
            case R.id.show_native_async_btn:
                showBannerAsync();
                break;
            case R.id.remove_native_btn:
                removeNativeAd();
                break;
        }
    }

    private void removeNativeAd() {
        if (asyncAdRequest != null) {
            asyncAdRequest.cancel();
            nativeProgressBar.setVisibility(View.GONE);
        }
        if (nativeContainer.getChildCount() > 0) {
            nativeContainer.removeAllViews();
        }
    }

    private void showBannerAsync() {
        if (asyncAdRequest != null) {
            asyncAdRequest.cancel();
        }
        asyncAdRequest = PeakSdk.createAdRequest(NATIVE_AD_ID);
        if (asyncAdRequest != null) {
            nativeProgressBar.setVisibility(View.VISIBLE);
            asyncAdRequest.start(peakAsyncAdRequestListener);
        }
    }

    private void showNative() {
        removeNativeAd();
        PeakNativeAdModel nativeAdModel = PeakSdk.showNativeAd(NATIVE_AD_ID);
        if (nativeAdModel != null) {
            nativeContainer.addView(createNativeAdView(nativeAdModel));
            PeakSdk.trackNativeAdShown(NATIVE_AD_ID);
        }
    }

    @Override
    public void onDestroy() {
        if (asyncAdRequest != null) {
            asyncAdRequest.cancel();
        }
        super.onDestroy();
    }

    private View createNativeAdView(PeakNativeAdModel nativeAd) {
        View nativeAdView = LayoutInflater.from(getContext())
                .inflate(R.layout.native_ad_layout, nativeContainer, false);
        ImageView mainImageView = (ImageView) nativeAdView.findViewById(R.id.mainImageView);
        ImageView logoImageView = (ImageView) nativeAdView.findViewById(R.id.logoImageView);
        ImageView privacyIconImageView = (ImageView) nativeAdView.findViewById(R.id.privacyIconImageView);
        TextView titleTextView = (TextView) nativeAdView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = (TextView) nativeAdView.findViewById(R.id.descriptionTextView);
        TextView sponsoredTextView = (TextView) nativeAdView.findViewById(R.id.sponsoredTextView);
        Button adActionButton = (Button) nativeAdView.findViewById(R.id.buttonInteractWithAd);

        Picasso imageLoader = Picasso.with(getActivity());
        String mainImage = nativeAd.getMainImage();
        if (!TextUtils.isEmpty(mainImage)) {
            imageLoader.load(mainImage).into(mainImageView);
        }
        String icon = nativeAd.getIcon();
        if (!TextUtils.isEmpty(icon)) {
            imageLoader.load(icon).into(logoImageView);
        }
        String privacyIcon = nativeAd.getPrivacyIcon();
        if (!TextUtils.isEmpty(privacyIcon)) {
            imageLoader.load(privacyIcon).into(privacyIconImageView);
            privacyIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PeakSdk.handleNativeAdPrivacyIconClicked(NATIVE_AD_ID);
                }
            });
        }
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
        return nativeAdView;
    }
}
