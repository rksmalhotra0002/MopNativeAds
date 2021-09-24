package com.twitter.ads.ui;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.privacy.ConsentDialogListener;
import com.mopub.common.privacy.PersonalInfoManager;
import com.mopub.mobileads.MoPubErrorCode;

import org.jetbrains.annotations.NotNull;

public final class MyMoPub {

    private PersonalInfoManager mPersonalInfoManager;
    private Context mContext;

    public final void init(@NotNull Context context, @NotNull String adunit) {

        mContext = context;
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adunit)
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false).
                        build();

        MoPub.initializeSdk(mContext,sdkConfiguration, initSdkListener());

    }

    private final SdkInitializationListener initSdkListener() {
        return () -> {
            Log.d("MoPub", "MoPub SDK Initilized");
            GDPRConsent();
        };
    }

    private final void GDPRConsent() {

        mPersonalInfoManager = MoPub.getPersonalInformationManager();

        if (mPersonalInfoManager.shouldShowConsentDialog()) {
            mPersonalInfoManager.loadConsentDialog(initDialogLoadListener());
        }
    }

    private final ConsentDialogListener initDialogLoadListener() {

        return  (new ConsentDialogListener() {

            public void onConsentDialogLoaded() {

                mPersonalInfoManager.showConsentDialog();

            }
            public void onConsentDialogLoadFailed(@NonNull @NotNull MoPubErrorCode moPubErrorCode) {

                Log.d("MoPub", "Consent dialog failed to load.");

            }
        });
    }
}
