package com.peakmediation.demo.activities;

import android.util.Log;

import com.peak.PeakSdkListener;
import com.peak.exception.PeakSdkException;

class PeakSdkLogListener implements PeakSdkListener {

    private final String logTag;

    @SuppressWarnings("SameParameterValue")
    PeakSdkLogListener(String logTag) {
        this.logTag = logTag;
    }

    @Override
    public void onInitializationSuccess() {
        Log.d(logTag, "onInitializationSuccess");
    }

    @Override
    public void onInitializationFailed(PeakSdkException e) {
        Log.e(logTag, "onInitializationFailed", e);
    }

    @Override
    public void onBannerShowSuccess(String peakAdZoneId) {
        Log.d(logTag, "onBannerShowSuccess (" + peakAdZoneId + ")");
    }

    @Override
    public void onBannerShowFailed(String peakAdZoneId, PeakSdkException e) {
        Log.e(logTag, "onBannerShowFailed (" + peakAdZoneId + ")");
    }

    @Override
    public void onInterstitialShowSuccess(String peakAdZoneId) {
        Log.d(logTag, "onInterstitialShowSuccess (" + peakAdZoneId + ")");
    }

    @Override
    public void onInterstitialShowFailed(String peakAdZoneId, PeakSdkException e) {
        Log.e(logTag, "onInterstitialShowFailed (" + peakAdZoneId + ")");
    }

    @Override
    public void onInterstitialClosed(String peakAdZoneId) {
        Log.d(logTag, "onInterstitialClosed (" + peakAdZoneId + ")");
    }

    @Override
    public void onCompletedRewardExperience(String peakAdZoneId) {
        Log.d(logTag, "onCompletedRewardExperience (" + peakAdZoneId + ")");
    }

    @Override
    public void onNativeAdShowSuccess(String peakAdZoneId) {
        Log.d(logTag, "onNativeAdShowSuccess (" + peakAdZoneId + ")");
    }

    @Override
    public void onNativeAdShowFailed(String peakAdZoneId, PeakSdkException e) {
        Log.e(logTag, "onNativeAdShowFailed (" + peakAdZoneId + ")");
    }

}
