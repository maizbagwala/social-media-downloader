package tk.maizbagwala.social.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import tk.maizbagwala.social.R;
import tk.maizbagwala.social.databinding.ActivityGalleryBinding;
import tk.maizbagwala.social.fragment.FBDownloadedFragment;
import tk.maizbagwala.social.fragment.InstaDownloadedFragment;
import tk.maizbagwala.social.fragment.TwitterDownloadedFragment;
import tk.maizbagwala.social.fragment.WhatsAppDowndlededFragment;
import tk.maizbagwala.social.fragment.YoutubeDownloadedFragment;
import tk.maizbagwala.social.util.AppLangSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static tk.maizbagwala.social.util.Utils.createFileFolder;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GalleryActivity  extends AppCompatActivity {
    GalleryActivity activity;
    ActivityGalleryBinding binding;

    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        activity = this;

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        showBanner();
//        AdsUtils.showAdMobInterstitialAds(activity);
//        AdsUtils.showGoogleBannerAd(activity,binding.adView);

        initViews();
    }

    public void initViews() {
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TextView tv=(TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab_black,null);
            binding.tabs.getTabAt(i).setCustomView(tv);
        }

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new InstaDownloadedFragment(), "Instagram");
        adapter.addFragment(new WhatsAppDowndlededFragment(), "Whatsapp");
//        adapter.addFragment(new TikTokDownloadedFragment(), "TikTok");
        adapter.addFragment(new FBDownloadedFragment(), "Facebook");
        adapter.addFragment(new TwitterDownloadedFragment(), "Twitter");
        adapter.addFragment(new YoutubeDownloadedFragment(), "Youtube");
//        adapter.addFragment(new LikeeDownloadedFragment(), "Likee");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);



    }
    private void showBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView)findViewById(R.id.adView)).loadAd(adRequest);
    }

}
