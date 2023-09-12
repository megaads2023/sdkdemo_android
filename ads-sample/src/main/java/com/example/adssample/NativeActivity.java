package com.example.adssample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.network.mega.ads.nativeads.MegaAdsCustomEventNative;
import com.network.mega.ads.nativeads.MegaAdsNative;
import com.network.mega.ads.nativeads.MegaAdsStaticNativeAdRenderer;
import com.network.mega.ads.nativeads.NativeAd;
import com.network.mega.ads.nativeads.NativeErrorCode;
import com.network.mega.ads.nativeads.NativeImageHelper;
import com.network.mega.ads.nativeads.NativeRendererHelper;
import com.network.mega.ads.nativeads.RequestParameters;
import com.network.mega.ads.nativeads.ViewBinder;

import java.util.EnumSet;

public class NativeActivity extends BaseActivity implements MegaAdsNative.MegaNativeNetworkListener {

    private final String UNITID = "210008";

    private LinearLayout adContainer;

    private RelativeLayout nativeOuterView;
    private ImageView nativeIconImage;
    private TextView nativeTitle;
    private TextView nativeText;
    private ImageView nativeMainImage;
    private TextView nativeSponsoredTextView;
    private Button nativeCta;
    private ImageView nativePrivacyInformationIconImage;
    private NativeAd mCurNativeAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        initView();
    }

    private void initView() {
        adContainer = findViewById(R.id.parent_view);
        Button loadad = findViewById(R.id.native_load);
        loadad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNativeAd();
            }
        });

        //Native view
        nativeOuterView = (RelativeLayout) findViewById(R.id.native_outer_view);
        nativeIconImage = (ImageView) findViewById(R.id.native_icon_image);
        nativeTitle = (TextView) findViewById(R.id.native_title);
        nativeText = (TextView) findViewById(R.id.native_text);
        nativeMainImage = (ImageView) findViewById(R.id.native_main_image);
        nativeSponsoredTextView = (TextView) findViewById(R.id.native_sponsored_text_view);
        nativeCta = (Button) findViewById(R.id.native_cta);
        nativePrivacyInformationIconImage = (ImageView) findViewById(R.id.native_privacy_information_icon_image);
    }

    private void loadNativeAd() {
        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
                RequestParameters.NativeAdAsset.SPONSORED
        );
        RequestParameters requestParameters = new RequestParameters.Builder()
                .desiredAssets(desiredAssets)
                .build();

        MegaAdsNative megaAdsNative = new MegaAdsNative(this, UNITID, this);
        MegaAdsStaticNativeAdRenderer staticNativeAdRenderer = new MegaAdsStaticNativeAdRenderer(new ViewBinder.Builder(0).build());
        megaAdsNative.registerAdRenderer(staticNativeAdRenderer);
        megaAdsNative.makeRequest(requestParameters);
    }

    @Override
    public void onNativeLoad(NativeAd nativeAd) {
        mCurNativeAd = nativeAd;
        if (!(nativeAd.getBaseNativeAd() instanceof MegaAdsCustomEventNative.MegaAdsStaticNativeAd)) {
            printInfo("sdk返回数据转型失败");
            return;
        }
        NativeAd.MegaNativeEventListener megaNativeEventListener = new NativeAd.MegaNativeEventListener() {
            @Override
            public void onImpression(View view) {
                printInfo("onImpression");
            }

            @Override
            public void onClick(View view) {
                printInfo("onClick");
            }
        };
        nativeAd.setMegaNativeEventListener(megaNativeEventListener);
        nativeAd.prepare(adContainer);
        adContainer.setVisibility(View.VISIBLE);
        MegaAdsCustomEventNative.MegaAdsStaticNativeAd staticNativeAd = (MegaAdsCustomEventNative.MegaAdsStaticNativeAd) nativeAd.getBaseNativeAd();
        setAdViewData(staticNativeAd);
    }

    private void setAdViewData(MegaAdsCustomEventNative.MegaAdsStaticNativeAd staticNativeAd) {
        NativeRendererHelper.addTextView(nativeTitle,
                staticNativeAd.getTitle());
        NativeRendererHelper.addTextView(nativeText, staticNativeAd.getText());
        NativeRendererHelper.addTextView(nativeCta,
                staticNativeAd.getCallToAction());
        NativeImageHelper.loadImageView(staticNativeAd.getMainImageUrl(),
                nativeMainImage);
        NativeImageHelper.loadImageView(staticNativeAd.getIconImageUrl(),
                nativeIconImage);
        NativeRendererHelper.addPrivacyInformationIcon(
                nativePrivacyInformationIconImage,
                staticNativeAd.getPrivacyInformationIconImageUrl(),
                staticNativeAd.getPrivacyInformationIconClickThroughUrl());
        NativeRendererHelper.addSponsoredView(staticNativeAd.getSponsored(),
                nativeSponsoredTextView);
    }


    @Override
    public void onNativeFail(NativeErrorCode errorCode) {
        printInfo("onNativeFail errorMsg:" + errorCode.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurNativeAd != null) {
            mCurNativeAd.destroy();
            mCurNativeAd = null;
        }
    }
}
