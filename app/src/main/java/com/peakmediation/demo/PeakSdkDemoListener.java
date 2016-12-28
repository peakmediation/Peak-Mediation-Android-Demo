package com.peakmediation.demo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.peak.PeakSdkListener;
import com.peak.exception.PeakSdkException;

public class PeakSdkDemoListener implements PeakSdkListener {

    private final String logTag;
    private Context context;

    public PeakSdkDemoListener(Context context, String logTag) {
        this.context = context;
        this.logTag = logTag;
    }

    @Override
    public void onInitializationSuccess() {
        String message = "onInitializationSuccess";
        Log.d(logTag, message);
        makeToast(message);
    }

    @Override
    public void onInitializationFailed(PeakSdkException e) {
        String message = "onInitializationFailed";
        Log.e(logTag, message, e);
        makeToast(message);
    }

    @Override
    public void onBannerShowSuccess(String peakAdZoneId) {
        String message = "onBannerShowSuccess" + " " + peakAdZoneId;
        Log.d(logTag, message);
        makeToast(message);
    }

    @Override
    public void onBannerShowFailed(String peakAdZoneId, PeakSdkException e) {
        String message = "onBannerShowFailed" + " " + peakAdZoneId;
        Log.e(logTag, message, e);
        makeToast(message);
    }

    @Override
    public void onInterstitialShowSuccess(String peakAdZoneId) {
        String message = "onInterstitialShowSuccess" + " " + peakAdZoneId;
        Log.d(logTag, message);
        makeToast(message);
    }

    @Override
    public void onInterstitialShowFailed(String peakAdZoneId, PeakSdkException e) {
        String message = "onInterstitialShowFailed" + " " + peakAdZoneId;
        Log.e(logTag, message, e);
        makeToast(message);
    }

    @Override
    public void onInterstitialClosed(String peakAdZoneId) {
        String message = "onInterstitialClosed " + " " + peakAdZoneId;
        Log.d(logTag, message);
        makeToast(message);
    }

    @Override
    public void onCompletedRewardExperience(String peakAdZoneId) {
        String message = "onCompletedRewardExperience" + " " + peakAdZoneId;
        Log.d(logTag, message);
        makeToast(message);
    }

    @Override
    public void onNativeAdShowFailed(String peakAdZoneId, PeakSdkException e) {
        String message = "onNativeAdShowFailed" + " " + peakAdZoneId;
        Log.e(logTag, message, e);
        makeToast(message);
    }

    @Override
    public void onNativeAdShowSuccess(String peakAdZoneId) {
        String message = "onNativeAdShowSuccess" + " " + peakAdZoneId;
        Log.d(logTag, message);
        makeToast(message);
    }

    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
