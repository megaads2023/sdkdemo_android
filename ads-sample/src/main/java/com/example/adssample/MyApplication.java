package com.example.adssample;

import android.app.Application;
import android.widget.Toast;

import com.network.mega.ads.common.MegaAds;
import com.network.mega.ads.common.SdkConfiguration;
import com.network.mega.ads.common.SdkInitializationListener;
import com.network.mega.ads.common.logging.MegaAdsLog;
import com.network.mega.ads.common.privacy.PersonalInfoManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {
        MegaAds.setLocationAwareness(MegaAds.LocationAwareness.TRUNCATED);
        MegaAds.setLocationPrecision(4);
        SdkConfiguration.Builder builder = new SdkConfiguration.Builder("2360/e939915d0a6a7c6d827bdf5441cfe624");
        if (BuildConfig.DEBUG) {
            builder.withLogLevel(MegaAdsLog.LogLevel.DEBUG);
        } else {
            builder.withLogLevel(MegaAdsLog.LogLevel.INFO);
        }
        builder.withLegitimateInterestAllowed(true);
        MegaAds.initializeSdk(this, builder.build(), new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Toast.makeText(MyApplication.this , "初始化结束", Toast.LENGTH_SHORT).show();
            }
        });
        PersonalInfoManager infoManager = MegaAds.getPersonalInformationManager();
        infoManager.grantConsent();//同意获取隐私数据
    }
}
