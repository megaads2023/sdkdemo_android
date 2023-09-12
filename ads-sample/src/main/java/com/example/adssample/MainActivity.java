package com.example.adssample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.core.app.ActivityCompat;

import com.network.mega.ads.common.Constants;
import com.network.mega.ads.common.MegaAds;
import com.network.mega.ads.common.SdkConfiguration;
import com.network.mega.ads.common.SdkInitializationListener;
import com.network.mega.ads.common.logging.MegaAdsLog;
import com.network.mega.ads.common.privacy.PersonalInfoManager;
import com.network.mega.ads.common.util.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private CheckBox agreePrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> REQUIRED_DANGEROUS_PERMISSIONS = new ArrayList<>();
        REQUIRED_DANGEROUS_PERMISSIONS.add(Manifest.permission.ACCESS_FINE_LOCATION);
        REQUIRED_DANGEROUS_PERMISSIONS.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> permissionsToBeRequested = new ArrayList();
        for (String p : REQUIRED_DANGEROUS_PERMISSIONS) {
            if (!DeviceUtils.isPermissionGranted(this, p)) {
                permissionsToBeRequested.add(p);
            }
        }
        if (!permissionsToBeRequested.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToBeRequested.toArray(new String[]{}),
                    Constants.UNUSED_REQUEST_CODE
            );
        }
        initView();
    }

    private void initView() {
        agreePrivacy = findViewById(R.id.cb_agree_privacy);
        Button btn_banner = findViewById(R.id.btn_banner);
        Button btn_native = findViewById(R.id.btn_native);
        agreePrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PersonalInfoManager personalInformationManager = MegaAds.getPersonalInformationManager();
                if (personalInformationManager == null) {
                    printInfo("请初始化SDK");
                    return;
                }
                if (isChecked) {
                    personalInformationManager.grantConsent();
                    printInfo("同意获取隐私数据");
                } else {
                    personalInformationManager.revokeConsent();
                    printInfo("撤销同意获取隐私数据");
                }

            }
        });
        btn_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
            }
        });
        btn_native.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NativeActivity.class));
            }
        });
        findViewById(R.id.btn_interstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InterstitialActivity.class));
            }
        });
    }
}