package com.example.adssample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.network.mega.ads.mobileads.MegaAdsErrorCode;
import com.network.mega.ads.mobileads.MegaAdsView;
import com.network.mega.ads.network.ImpressionData;
import com.network.mega.ads.network.ImpressionListener;
import com.network.mega.ads.network.ImpressionsEmitter;

public class BannerActivity extends BaseActivity implements ImpressionListener, MegaAdsView.BannerAdListener {

    private final String UNITID = "210026";
    private MegaAdsView bannerView;

    private MegaAdsView.MegaAdSize megaAdSize = MegaAdsView.MegaAdSize.HEIGHT_50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
    }

    private void initView() {
        RadioGroup sizeRg = findViewById(R.id.banner_rg_size);
        sizeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.banner_height_50) {
                    megaAdSize = MegaAdsView.MegaAdSize.HEIGHT_50;
                } else if (checkedId == R.id.banner_height_90) {
                    megaAdSize = MegaAdsView.MegaAdSize.HEIGHT_90;
                } else if (checkedId == R.id.banner_height_250) {
                    megaAdSize = MegaAdsView.MegaAdSize.HEIGHT_250;
                } else if (checkedId == R.id.banner_height_280) {
                    megaAdSize = MegaAdsView.MegaAdSize.HEIGHT_280;
                }
            }
        });
        bannerView = findViewById(R.id.banner_bannerview);
        Button loadBtn = findViewById(R.id.banner_load);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBannerAd();
            }
        });
        bannerView.setBannerAdListener(this);
        ImpressionsEmitter.addListener(this);
    }

    private void loadBannerAd() {
        onAdSizeSettingsChanged();
        bannerView.setAdUnitId(UNITID);
        bannerView.loadAd();
    }

    private void onAdSizeSettingsChanged() {
        bannerView.setAdSize(megaAdSize);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) bannerView.getLayoutParams();
        float density = getResources().getDisplayMetrics().density;
        layoutParams.height = (int) (megaAdSize.toInt() * density);
        bannerView.setLayoutParams(layoutParams);
    }


    @Override
    public void onImpression(@NonNull String adUnitId, @Nullable ImpressionData impressionData) {
        printInfo("onImpression");
    }

    @Override
    public void onBannerLoaded(@NonNull MegaAdsView banner) {
        printInfo("onBannerLoaded");
    }

    @Override
    public void onBannerFailed(MegaAdsView banner, MegaAdsErrorCode errorCode) {
        printInfo("onBannerFailed errorMsg:" + errorCode.toString());
    }

    @Override
    public void onBannerClicked(MegaAdsView banner) {
        printInfo("onBannerClicked");
    }

    @Override
    public void onBannerExpanded(MegaAdsView banner) {
        printInfo("onBannerExpanded");
    }

    @Override
    public void onBannerCollapsed(MegaAdsView banner) {
        printInfo("onBannerCollapsed");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImpressionsEmitter.removeListener( this );
        if (bannerView != null) {
            bannerView.destroy();
            bannerView = null;
        }
    }
}
