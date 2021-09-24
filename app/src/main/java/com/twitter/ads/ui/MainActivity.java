package com.twitter.ads.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.mopub.common.SdkConfiguration;
import com.mopub.mobileads.FacebookAdapterConfiguration;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;
import com.twitter.ads.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener;

    private NativeAd.MoPubNativeEventListener moPubNativeEventListener;

    private MoPubNative moPubNative;

    private AdapterHelper adapterHelper;

    private FrameLayout native_ad_frame;

    private MoPubStaticNativeAdRenderer adRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTimerForMopNativeAds();

    }

    private void setTimerForMopNativeAds() {
        try {

            new MyMoPub().init(this, "11a17b188668469fb0412708c3d16813");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setMopUpNativeAds();
                }
            }, 200);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setMopUpNativeAds() {

        try {

            //FacebookMediationCode
            Map<String, String> facebookNativeBanner = new HashMap<>();

            facebookNativeBanner.put("native_banner", "true");

            SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder("186235493618081_186236813617949");

            configBuilder.withMediatedNetworkConfiguration(FacebookAdapterConfiguration.class.getName(), facebookNativeBanner);

            native_ad_frame = findViewById(R.id.native_ad_frame);

            moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
                @Override
                public void onClick(View view) {
                    // Click tracking.

                    Log.d("MoPub", "Native ad recorded a click.");

                }

                @Override
                public void onImpression(View view) {
                    // Impress is recorded - do what is needed AFTER the ad is visibly shown here.

                    Log.d("MoPub", "Native ad recorded an impression.");

                }
            };

            moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
                @Override
                public void onNativeLoad(NativeAd nativeAd) {

                    adapterHelper = new AdapterHelper(MainActivity.this, 0, 3);
                    View v = adapterHelper.getAdView(null, native_ad_frame, nativeAd,
                            new ViewBinder.Builder(0).build());
                    nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);
                    native_ad_frame.addView(v);
                    Log.d("MoPub", "Native ad has loaded.");

                }

                @Override
                public void onNativeFail(NativeErrorCode errorCode) {
                    Log.d("MoPub", "Native ad failed to load with error: $errorCode");
                }
            };

            moPubNative = new MoPubNative(this, "11a17b188668469fb0412708c3d16813", moPubNativeNetworkListener);

            ViewBinder viewBinder = new ViewBinder.Builder(R.layout.mopub_native_ad_view)
                    .titleId(R.id.mopub_native_ad_title)
                    .textId(R.id.mopub_native_ad_text)
                    //.mainImageId(R.id.mopub_native_ad_main_imageview)
                    .iconImageId(R.id.mopub_native_ad_icon)
                    .callToActionId(R.id.mopub_native_ad_cta)
                    .privacyInformationIconImageId(R.id.mopub_native_ad_privacy)
                    .build();

            adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
            moPubNative.registerAdRenderer(adRenderer);
            moPubNative.makeRequest();
            Log.d("MoPub", "Native ad is loading.");



    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        moPubNative.destroy();
        super.onDestroy();
    }
}
