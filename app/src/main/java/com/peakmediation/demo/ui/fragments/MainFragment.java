package com.peakmediation.demo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.peak.PeakSdk;
import com.peakmediation.demo.PeakSdkConfig;
import com.peakmediation.demo.R;
import com.peakmediation.demo.ui.activities.AllInOneTestActivity;

public class MainFragment extends Fragment {

    private EditText applicationIdET;
    private EditText staticInterstitialZoneEt;
    private EditText videoInterstitialZoneEt;
    private EditText rewardedInterstitialZoneEt;
    private EditText targetingAgeEt;
    private Spinner genderSpinner;
    private Button initializeBtn;
    private Button checkStaticBtn;
    private Button showStaticBtn;
    private Button checkVideoBtn;
    private Button showVideoBtn;
    private Button checkRewardedBtn;
    private Button showRewardedBtn;
    private ShowAdClickListener showAdClickListener = new ShowAdClickListener();
    private CheckClickListener checkClickListener = new CheckClickListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setupViews(view);
        fillET();
        return view;
    }

    private void setupViews(View container) {
        applicationIdET = (EditText) container.findViewById(R.id.application_id_et);
        staticInterstitialZoneEt = (EditText) container.findViewById(R.id.static_interstitial_zone_et);
        videoInterstitialZoneEt = (EditText) container.findViewById(R.id.video_interstitial_zone_et);
        rewardedInterstitialZoneEt = (EditText) container.findViewById(R.id.rewarded_interstitial_zone_et);
        targetingAgeEt = (EditText) container.findViewById(R.id.targeting_age_et);
        initializeBtn = (Button) container.findViewById(R.id.initialize_btn);
        checkStaticBtn = (Button) container.findViewById(R.id.check_static_btn);
        showStaticBtn = (Button) container.findViewById(R.id.show_static_btn);
        checkVideoBtn = (Button) container.findViewById(R.id.check_video_btn);
        showVideoBtn = (Button) container.findViewById(R.id.show_video_btn);
        checkRewardedBtn = (Button) container.findViewById(R.id.check_rewarded_btn);
        showRewardedBtn = (Button) container.findViewById(R.id.show_rewarded_btn);

        genderSpinner = (Spinner) container.findViewById(R.id.gender_spinner);

        initializeBtn.setOnClickListener(new InitializeClickListener());

        showStaticBtn.setOnClickListener(showAdClickListener);
        showVideoBtn.setOnClickListener(showAdClickListener);
        showRewardedBtn.setOnClickListener(showAdClickListener);
        checkStaticBtn.setOnClickListener(checkClickListener);
        checkVideoBtn.setOnClickListener(checkClickListener);
        checkRewardedBtn.setOnClickListener(checkClickListener);
    }

    private void fillET() {
        applicationIdET.setText(PeakSdkConfig.APP_ID);
        staticInterstitialZoneEt.setText(PeakSdkConfig.STATIC_INTERSTITIAL_ID);
        videoInterstitialZoneEt.setText(PeakSdkConfig.VIDEO_INTERSTITIAL_ID);
        rewardedInterstitialZoneEt.setText(PeakSdkConfig.REWARDED_VIDEO_INTERSTITIAL_ID);
    }

    private class ShowAdClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.show_video_btn:
                    PeakSdk.showInterstitial(videoInterstitialZoneEt.getText().toString());
                    break;
                case R.id.show_rewarded_btn:
                    PeakSdk.showInterstitial(rewardedInterstitialZoneEt.getText().toString());
                    break;
                case R.id.show_static_btn:
                    PeakSdk.showInterstitial(staticInterstitialZoneEt.getText().toString());
                    break;
            }
        }
    }

    private class CheckClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.check_video_btn:
                    PeakSdk.checkAdAvailable(videoInterstitialZoneEt.getText().toString());
                    break;
                case R.id.check_rewarded_btn:
                    PeakSdk.checkAdAvailable(rewardedInterstitialZoneEt.getText().toString());
                    break;
                case R.id.check_static_btn:
                    PeakSdk.checkAdAvailable(staticInterstitialZoneEt.getText().toString());
                    break;
            }
        }
    }

    private class InitializeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String appId = applicationIdET.getText().toString();
            String targetingAge = null;
            String targetingGender = null;
            if (targetingAgeEt.getText() != null) {
                targetingAge = targetingAgeEt.getText().toString();
            }
            if (genderSpinner.getSelectedItem() != null) {
                targetingGender = genderSpinner.getSelectedItem().toString();
            }
            ((AllInOneTestActivity) getActivity()).initializePeakSdk(appId,
                    targetingAge, targetingGender);
        }
    }
}
