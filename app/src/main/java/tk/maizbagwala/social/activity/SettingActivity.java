package tk.maizbagwala.social.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import tk.maizbagwala.social.R;
import tk.maizbagwala.social.util.AdsUtils;
import tk.maizbagwala.social.util.AppLangSessionManager;
import tk.maizbagwala.social.util.AppLangSessionManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    AppLangSessionManager appLangSessionManager;
    CardView rvChangeLang;
    CardView cvAboutUs;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backBtn = findViewById(R.id.imBack);
        appLangSessionManager = new AppLangSessionManager(this);
        rvChangeLang = findViewById(R.id.rvChangeLang);
        cvAboutUs = findViewById(R.id.cvAboutUs);
        showBanner();
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        cvAboutUs.setOnClickListener(v -> {
            AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_about_id));
            startActivity(new Intent(this, AboutUsActivity.class));
        });
        rvChangeLang.setOnClickListener(v -> {


            final BottomSheetDialog dialogSortBy = new BottomSheetDialog(SettingActivity.this, R.style.SheetDialog);
            dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSortBy.setContentView(R.layout.dialog_language);
            final TextView tv_english = dialogSortBy.findViewById(R.id.tv_english);
            final TextView tv_hindi = dialogSortBy.findViewById(R.id.tv_hindi);
            final TextView tv_cancel = dialogSortBy.findViewById(R.id.tv_cancel);
            final TextView tvArabic = dialogSortBy.findViewById(R.id.tvArabic);

            dialogSortBy.show();


            tv_english.setOnClickListener(view -> {
                setLocale("en");
                appLangSessionManager.setLanguage("en");
            });
            tv_hindi.setOnClickListener(view -> {
                setLocale("hi");
                appLangSessionManager.setLanguage("hi");
            });
            tvArabic.setOnClickListener(view -> {
                setLocale("ar");
                appLangSessionManager.setLanguage("ar");
            });
            tv_cancel.setOnClickListener(view -> dialogSortBy.dismiss());

        });
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


        Intent refresh = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    private void showBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView) findViewById(R.id.adView)).loadAd(adRequest);
    }
}