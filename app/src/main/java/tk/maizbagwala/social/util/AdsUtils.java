package tk.maizbagwala.social.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdsUtils {

//    public static void showGoogleBannerAd(Context context, com.google.android.gms.ads.AdView googleAdView) {
//
//        googleAdView.setVisibility(View.VISIBLE);
//        //Load Banner Ad
//        MobileAds.initialize(context, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
//        googleAdView.loadAd(adRequest);
//    }


    public static void showAdMobInterstitialAds(Activity context, String adUnit) {
        final InterstitialAd[] mInterstitialAd = new InterstitialAd[1];

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, adUnit, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd[0] = interstitialAd;
                        mInterstitialAd[0].show(context);
                        Log.i(Utils.TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(Utils.TAG, loadAdError.getMessage());
                        mInterstitialAd[0] = null;
                    }
                });




    }

}
