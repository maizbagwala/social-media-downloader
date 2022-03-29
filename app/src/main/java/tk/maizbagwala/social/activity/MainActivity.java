package tk.maizbagwala.social.activity;

import static tk.maizbagwala.social.util.Utils.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tk.maizbagwala.social.BuildConfig;
import tk.maizbagwala.social.R;
import tk.maizbagwala.social.databinding.ActivityMainBinding;
import tk.maizbagwala.social.util.AdsUtils;
import tk.maizbagwala.social.util.AppLangSessionManager;
import tk.maizbagwala.social.util.ClipboardListener;
import tk.maizbagwala.social.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MainActivity activity;
    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    private ClipboardManager clipBoard;
    ArrayList<String> permissions = new ArrayList<>();
    String CopyKey = "";
    String CopyValue = "";
    private NativeAd nativeAd;
    private InterstitialAd mInterstitialAd;


    AppLangSessionManager appLangSessionManager;

    private static final int REQUEST_ACTION_OPEN_DOCUMENT_TREE = 12;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        showBanner();
        refreshAd();
//        ca-app-pub-3940256099942544/1033173712 test ad unit interstitial ad
        AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_home_id));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
//        AdsUtils.showGoogleBannerAd(activity,binding.adView);
        binding.btnSetting.setOnClickListener(view -> {
            AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_setting_id));
            startActivity(new Intent(this, SettingActivity.class));
        });

        //version check
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("version");
//        myRef.get().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.e("firebase", "Error getting data", task.getException());
//            }
//            else {
//                Log.d("firebase", String.valueOf(task.getResult().getValue()));
//            }
//        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                Log.d("myFirebase", "latest version code is: " + value);
                if (!value.equals("null") && !value.equals("")) {
                    int storeVersion = Integer.parseInt(value);
                    try {
                        PackageInfo pInfo = getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
                        int version = pInfo.versionCode;
                        if (version < storeVersion) {
                            displayUpdateDialog();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("myFirebase", error.toException().toString());

            }
        });
        initViews();
//        StartAppSDK.setTestAdsEnabled(true);
    }

    private void showBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView) findViewById(R.id.adView)).loadAd(adRequest);
    }

    private void displayUpdateDialog() {
        Dialog dialog = new Dialog(this, R.style.myfullscreendialog);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.btn_cancel)).setText("Exit");
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
//            dialog.dismiss();
            finish();
        });
        dialog.findViewById(R.id.btn_update).setOnClickListener(v -> {
            Utils.UpdateApp(this);
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }

//        binding.rvLikee.setOnClickListener(this);
        binding.btnInsta.setOnClickListener(this);
        binding.btnWhatsapp.setOnClickListener(this);
//        binding.rvTikTok.setOnClickListener(this);
        binding.btnFacebook.setOnClickListener(this);
        binding.btnTwitter.setOnClickListener(this);
        binding.btnGallery.setOnClickListener(this);
//        binding.rvAbout.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);
        binding.btnRate.setOnClickListener(this);
//        binding.rvMoreApp.setOnClickListener(this);
        binding.btnYoutube.setOnClickListener(this);


        //TODO :  Change Language Dialog Open
//        binding.rvChangeLang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                final BottomSheetDialog dialogSortBy = new BottomSheetDialog(MainActivity.this, R.style.SheetDialog);
//                dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialogSortBy.setContentView(R.layout.dialog_language);
//                final TextView tv_english = dialogSortBy.findViewById(R.id.tv_english);
//                final TextView tv_hindi = dialogSortBy.findViewById(R.id.tv_hindi);
//                final TextView tv_cancel = dialogSortBy.findViewById(R.id.tv_cancel);
//                final TextView tvArabic = dialogSortBy.findViewById(R.id.tvArabic);
//
//                dialogSortBy.show();
//
//
//                tv_english.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setLocale("en");
//                        appLangSessionManager.setLanguage("en");
//                    }
//                });
//                tv_hindi.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setLocale("hi");
//                        appLangSessionManager.setLanguage("hi");
//                    }
//                });
//                tvArabic.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setLocale("ar");
//                        appLangSessionManager.setLanguage("ar");
//                    }
//                });
//                tv_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialogSortBy.dismiss();
//                    }
//                });
//
//            }
//        });


        Utils.createFileFolder();

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("likee")) {

                try {
                    List<String> extractedUrls = extractUrls(CopiedText);
                    CopyValue = extractedUrls.get(0);

                    Log.d("LIKEEEEE MAIN", CopyValue);
                } catch (Exception ex) {

                }


                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
            } else if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("facebook.com") || CopiedText.contains("fb.")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            } else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            } else if (CopiedText.contains("youtube.com") || CopiedText.contains("youtu.be/")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callYouTubeActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        switch (v.getId()) {
//            case R.id.rvLikee:
//                if (Build.VERSION.SDK_INT >= 23) {
//                    checkPermissions(100);
//                } else {
//                    callLikeeActivity();
//                }
//                break;
            case R.id.btn_insta:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;

            case R.id.btn_whatsapp:
//                showMaintenance();
//
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
//            case R.id.rvTikTok:
//                if (Build.VERSION.SDK_INT >= 23) {
//                    checkPermissions(103);
//                } else {
//                    callTikTokActivity();
//                }
//                break;
            case R.id.btn_facebook:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
                break;
            case R.id.btn_gallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.btn_twitter:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
                break;
            case R.id.btn_youtube:
                showMaintenance();
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);

                } else {
                    callYouTubeActivity();
                }
                break;
//            case R.id.rvAbout:
//                i = new Intent(activity, AboutUsActivity.class);
//                startActivity(i);
//                break;
            case R.id.btn_share:
                Utils.ShareApp(activity);
                break;

            case R.id.btn_rate:
                Utils.RateApp(activity);
                break;
//            case R.id.rvMoreApp:
//                Utils.MoreApp(activity);
//                break;

        }
    }

    private void showMaintenance() {
        Dialog d = new Dialog(this, R.style.myfullscreendialog);
        d.setContentView(R.layout.under_maintain);
        d.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            d.dismiss();
        });
        d.show();

    }


    public void callLikeeActivity() {
        Intent i = new Intent(activity, LikeeActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callInstaActivity() {
        Intent i = new Intent(activity, InstagramActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_insta_id));
        startActivity(i);
    }


    public void callWhatsappActivity() {
        Intent i = new Intent(activity, WhatsappActivity.class);
        AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_whatsapp_id));
        startActivity(i);
    }

    public void callTikTokActivity() {
        Intent i = new Intent(activity, TikTokActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callFacebookActivity() {
        Intent i = new Intent(activity, FacebookActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_facebook_id));
        startActivity(i);

    }

    public void callTwitterActivity() {
        Intent i = new Intent(activity, TwitterActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_twitter_id));
        startActivity(i);
    }

    public void callYouTubeActivity() {
        Intent i = new Intent(activity, YoutubeActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }


    public void callGalleryActivity() {
        Intent i = new Intent(activity, GalleryActivity.class);
        AdsUtils.showAdMobInterstitialAds(this, getString(R.string.admob_inter_gallery_id));
        startActivity(i);
    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb.") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.mipmap.ic_launcher_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED && !p.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
                listPermissionsNeeded.add(p);
            }
        }
        if (type == 102 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (arePermissionDenied()) {

                Dialog d = new Dialog(this, R.style.myfullscreendialog);
                d.setContentView(R.layout.permission_dialog);
                d.show();
                d.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
                    d.dismiss();
                });
                d.findViewById(R.id.btn_give_permission).setOnClickListener(v -> {
                    getFolderPermission();

//                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
//
//                    startActivity(
//                            new Intent(
//                                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
//                                    uri
//                            )
//                    );
                    d.dismiss();
                });


                return false;
            } else {
                callWhatsappActivity();
                return true;
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        } else {
            if (type == 100) {
                callLikeeActivity();
            } else if (type == 101) {
                callInstaActivity();
            } else if (type == 102) {
                callWhatsappActivity();
            } else if (type == 103) {
                callTikTokActivity();
            } else if (type == 104) {
                callFacebookActivity();
            } else if (type == 105) {
                callGalleryActivity();
            } else if (type == 106) {
                callTwitterActivity();
            } else if (type == 107) {
                callYouTubeActivity();
            }

        }
        return true;
    }

    private void getFolderPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getPermissionQAbove();
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callLikeeActivity();
            } else {
            }
            return;
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callInstaActivity();
            } else {
            }
            return;
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            } else {
            }
            return;
        } else if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            } else {
            }
            return;
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callFacebookActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {
            }
            return;
        } else if (requestCode == 106) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTwitterActivity();
            } else {
            }
            return;
        } else if (requestCode == 107) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callYouTubeActivity();
            } else {
            }
            return;
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.setToast(activity, getResources().getString(R.string.pls_bck_again));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    //TODO :  Using for Set Locale
    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri getUri() {
        StorageManager sm = (StorageManager) getApplicationContext().getSystemService(Context.STORAGE_SERVICE);

        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        //String startDir = "Android";
        //String startDir = "Download"; // Not choosable on an Android 11 device
        //String startDir = "DCIM";
        //String startDir = "DCIM/Camera";  // replace "/", "%2F"
        //String startDir = "DCIM%2FCamera";
        // String startDir = "Documents";
        String startDir = "Android/media/com.whatsapp/WhatsApp";

        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();

        Log.d("maiz", "INITIAL_URI scheme: " + scheme);

        scheme = scheme.replace("/root/", "/document/");

        startDir = startDir.replace("/", "%2F");

        scheme += "%3A" + startDir;

        return Uri.parse(scheme);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getPermissionQAbove() {

        StorageManager sm = (StorageManager) getApplicationContext().getSystemService(Context.STORAGE_SERVICE);

        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

        Uri uri = getUri();

        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Log.d("maiz", "uri: " + uri.toString());

        ((Activity) MainActivity.this).startActivityForResult(intent, REQUEST_ACTION_OPEN_DOCUMENT_TREE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_ACTION_OPEN_DOCUMENT_TREE) {
            System.out.println("HEY");

            if (data == null) return; // TODO: Show error
            Uri uri = data.getData();
            if (uri == null) return;

            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }

    }

    private boolean arePermissionDenied() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("Size ", String.valueOf(getContentResolver().getPersistedUriPermissions().size()));
            return getContentResolver().getPersistedUriPermissions().size() <= 0;
        }

        return false;
    }

    void displayNativeAd() {
//        getString(R.string.admob_native_home_id)
        AdLoader.Builder builder = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {

                        ((ImageView) findViewById(R.id.ad_app_icon)).setImageURI(nativeAd.getIcon().getUri());
                        // Assumes you have a placeholder FrameLayout in your View layout
                        // (with id fl_adplaceholder) where the ad is to be placed.
//                        FrameLayout frameLayout =
//                                findViewById(R.id.fl_adplaceholder);
//                        // Assumes that your ad layout is in a file call native_ad_layout.xml
//                        // in the res/layout folder
//                        NativeAdView adView = (NativeAdView) getLayoutInflater()
//                                .inflate(R.layout.native_ad_layout, null);
//                        // This method sets the text, images and the native ad, etc into the ad
//                        // view.
//                        populateNativeAdView(nativeAd, adView);
//                        frameLayout.removeAllViews();
//                        frameLayout.addView(adView);
                    }
                });
//        builder.loadAd(new AdRequest.Builder().build());
    }

    private void refreshAd() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_home_id));

        // OnNativeAdLoadedListener implementation.
        builder.forNativeAd(
                unifiedNativeAd -> {
                    // If this callback occurs after the activity is destroyed, you must call
                    // destroy and return or you may get a memory leak.
                    boolean isDestroyed = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = isDestroyed();
                    }
                    if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                        unifiedNativeAd.destroy();
                        return;
                    }
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
                    NativeAdView adView =
                            (NativeAdView) getLayoutInflater()
                                    .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        String error =
                                                String.format(
                                                        "domain: %s, code: %d, message: %s",
                                                        loadAdError.getDomain(),
                                                        loadAdError.getCode(),
                                                        loadAdError.getMessage());
                                        Toast.makeText(
                                                MainActivity.this,
                                                "Failed to load native ad with error " + error,
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
//        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }

//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }

//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }

//        if (nativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }

//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
//        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
//        if (vc.hasVideoContent()) {
//
//            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
//            // VideoController will call methods on this object when events occur in the video
//            // lifecycle.
//            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
//                @Override
//                public void onVideoEnd() {
//                    // Publishers should allow native ads to complete video playback before
//                    // refreshing or replacing them with another ad in the same UI location.
//                    super.onVideoEnd();
//                }
//            });
//        } else {
//
//        }
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

}
