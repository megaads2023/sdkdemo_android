package com.example.adssample;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.network.mega.ads.MegaAdsInterstitial;
import com.network.mega.ads.mobileads.MegaAdsErrorCode;
import com.network.mega.ads.network.ImpressionData;
import com.network.mega.ads.network.ImpressionListener;
import com.network.mega.ads.network.ImpressionsEmitter;

public class InterstitialActivity extends BaseActivity implements MegaAdsInterstitial.InterstitialAdListener, ImpressionListener {

    private final String UNITID_BANNER = "210009";
    private final String UNITID_NATIVE = "210010";
    private final String UNITID_VIDEO = "210011";
    private String UNITID = UNITID_BANNER;

    private MegaAdsInterstitial interstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        initView();
    }

    private void initView() {
        findViewById(R.id.interstitial_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInterstitial();
            }
        });
        findViewById(R.id.interstitial_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
            }
        });
        RadioGroup adTypeGroup = findViewById(R.id.rg_adtype);
        adTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_adtype_html) {
                    UNITID = UNITID_BANNER;
                } else if (checkedId == R.id.rb_adtype_image) {
                    UNITID = UNITID_NATIVE;
                } else if (checkedId == R.id.rb_adtype_video) {
                    UNITID = UNITID_VIDEO;
                }
            }
        });

    }

    private long startTime;
    private void loadInterstitial() {
        printInfo("loadInterstitial UNITID:" + UNITID);
        startTime = System.currentTimeMillis();
        interstitial = new MegaAdsInterstitial(this, UNITID);
        interstitial.setInterstitialAdListener(this);
        interstitial.load();
    }

    private void showInterstitial() {
        if (interstitial != null && interstitial.isReady()) {
            ImpressionsEmitter.addListener(this);
            interstitial.show();
        } else {
            printInfo("Interstitial ad not ready");
        }
    }

    @Override
    public void onInterstitialLoaded(final MegaAdsInterstitial interstitial) {
//        printInfo("Interstitial loaded: " + UNITID);
        Toast.makeText(getApplicationContext(), "请求成功，耗时：" + ((System.currentTimeMillis() - startTime) / 1000), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInterstitialFailed(final MegaAdsInterstitial interstitial, final MegaAdsErrorCode errorCode) {
        printInfo("Interstitial (" + UNITID + ") failed to load with error: " + errorCode);
    }

    @Override
    public void onInterstitialShown(final MegaAdsInterstitial interstitial) {
        printInfo("Interstitial shown: " + UNITID);
    }

    @Override
    public void onImpression(final String impressionUNITID, final ImpressionData impressionData) {
        // Note: this is called for any ad format/unit_id impression.
        if (!impressionUNITID.equals(UNITID)) return;
        printInfo("Interstitial did track impression: " + UNITID);
    }

    @Override
    public void onInterstitialClicked(final MegaAdsInterstitial interstitial) {
        printInfo("Interstitial clicked: " + UNITID);
    }

    @Override
    public void onInterstitialDismissed(final MegaAdsInterstitial interstitial) {
        printInfo("Interstitial hidden: " + UNITID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitial != null) {
            interstitial.destroy();
            interstitial = null;
        }
        ImpressionsEmitter.removeListener(this);
    }

    @Override
    public void printInfo(String msg) {
        super.printInfo(msg);
    }
}
