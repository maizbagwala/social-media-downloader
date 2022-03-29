package tk.maizbagwala.social.activity;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static tk.maizbagwala.social.util.Utils.RootDirectoryYoutube;
import static tk.maizbagwala.social.util.Utils.TAG;
import static tk.maizbagwala.social.util.Utils.createFileFolder;
import static tk.maizbagwala.social.util.Utils.setToast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.reactivex.observers.DisposableObserver;
import tk.maizbagwala.social.R;
import tk.maizbagwala.social.adapter.YoutubeQuelityListAdapter;
import tk.maizbagwala.social.api.CommonClassForAPI;
import tk.maizbagwala.social.model.YtFormateResponse;
import tk.maizbagwala.social.util.AppLangSessionManager;
import tk.maizbagwala.social.util.Utils;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YoutubeActivity extends AppCompatActivity {
    private String VideoUrl;
    private ClipboardManager clipBoard;
    CommonClassForAPI commonClassForAPI;
    private EditText etText;
    private TextView tvPaste;

    private static final int ITAG_FOR_AUDIO = 140;

    private static String youtubeLink;

    private RelativeLayout mainLayout;
    //    private ArrayList<YtFragmentedVideo> formatsToShowList = new ArrayList<>();
    private ArrayList<YtFormateResponse.Formats.Video> formatsToShowList = new ArrayList<>();

    BottomSheetDialog dialogDownload;
    YoutubeQuelityListAdapter adapter;
    RecyclerView rvdownload;

    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        mainLayout = findViewById(R.id.main_layout);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        createFileFolder();
        initViews();


        dialogDownload = new BottomSheetDialog(YoutubeActivity.this, R.style.SheetDialog);
        dialogDownload.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDownload.setContentView(R.layout.dialog_youtube);
        rvdownload = dialogDownload.findViewById(R.id.rvdownload);
        dialogDownload.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            dialogDownload.dismiss();
        });
        rvdownload.setLayoutManager(new LinearLayoutManager(this));

        appLangSessionManager = new AppLangSessionManager(this);
        setLocale(appLangSessionManager.getLanguage());
        findViewById(R.id.login_btn1).setOnClickListener(v -> {
            String LL = etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(this, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(this, getResources().getString(R.string.enter_valid_url));
            } else {
                getYtFormat(LL);
//                getYoutubeDownloadUrl(LL);
            }
//            openSheet();

        });

    }

    private void getYtFormat(String videoUrl) {
        CommonClassForAPI.getInstance(this).getYtFormat(new DisposableObserver() {
            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext: " + o);
                YtFormateResponse model = (YtFormateResponse) o;
                if (!model.getError()) {
                    formatsToShowList.addAll(model.getFormats().getVideo());
                    rvdownload.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    dialogDownload.show();

                } else {
                    setToast(YoutubeActivity.this, "Something Went Wrong");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, "https://srv10.y2mate.is/listFormats", videoUrl);
    }

    private void initViews() {
        clipBoard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        etText = findViewById(R.id.et_text);
        tvPaste = findViewById(R.id.tv_paste);
        findViewById(R.id.rl_back_youtube).setOnClickListener(view -> onBackPressed());

//        binding.imInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
//            }
//        });


//        Glide.with(this)
//                .load(R.drawable.tw1)
//                .into(binding.layoutHowTo.imHowto1);
//
//        Glide.with(activity)
//                .load(R.drawable.tw2)
//                .into(binding.layoutHowTo.imHowto2);
//
//        Glide.with(activity)
//                .load(R.drawable.tw3)
//                .into(binding.layoutHowTo.imHowto3);
//
//        Glide.with(activity)
//                .load(R.drawable.tw4)
//                .into(binding.layoutHowTo.imHowto4);


//        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_twitter));
//        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.open_twitter));
//        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOTWITTER)) {
//            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOTWITTER, true);
//            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
//        } else {
//            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
//        }

//        loginBtn1.setOnClickListener(v -> {
//            String LL = etText.getText().toString();
//            if (LL.equals("")) {
//                Utils.setToast(this, getResources().getString(R.string.enter_url));
//            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
//                Utils.setToast(this, getResources().getString(R.string.enter_valid_url));
//            } else {
//                Utils.showProgressDialog(this);
//                GetTwitterData();
//                showInterstitial();
//            }
//        });

        tvPaste.setOnClickListener(v -> {
            PasteText();
        });

//        binding.LLOpenTwitter.setOnClickListener(v -> {
//            Utils.OpenApp(activity,"com.twitter.android");
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clipBoard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void PasteText() {
        try {
            etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("youtube.com") ||
                            clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("youtu.be/")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("youtube.com") ||
                            item.getText().toString().contains("youtu.be/")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("youtube.com") ||
                        CopyIntent.contains("youtu.be/")) {
                    etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

//    private void getYoutubeDownloadUrl(String youtubeLink) {
//        new YouTubeExtractor(this) {
//
//            @Override
//            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
//                if (ytFiles == null) {
//                    return;
//                }
//                formatsToShowList = new ArrayList<>();
//                for (int i = 0, itag; i < ytFiles.size(); i++) {
//                    itag = ytFiles.keyAt(i);
//                    YtFile ytFile = ytFiles.get(itag);
//
//                    if (ytFile.getFormat().getExt().equals("webm")) {
//                        continue;
//                    }
//
//                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
//                        addFormatToList(ytFile, ytFiles);
//                    }
//                }
//                Collections.sort(formatsToShowList, (lhs, rhs) -> lhs.height - rhs.height);
//                adapter = new YoutubeQuelityListAdapter(formatsToShowList, model -> {
//                    String filename;
//                    String videoTitle = vMeta.getTitle();
//                    if (videoTitle.length() > 55) {
//                        filename = videoTitle.substring(0, 55);
//                    } else {
//                        filename = videoTitle;
//                    }
//                    filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
//                    filename += (model.height == -1) ? "" : "-" + model.height + "p";
//                    String downloadIds = "";
//                    boolean hideAudioDownloadNotification = false;
//                    if (model.videoFile != null) {
//                        downloadIds += downloadFromUrl(model.videoFile.getUrl(), videoTitle,
//                                filename + "." + model.videoFile.getFormat().getExt(), false);
//                        downloadIds += "-";
//                        hideAudioDownloadNotification = true;
//                    }
//                    if (model.audioFile != null) {
//                        downloadIds += downloadFromUrl(model.audioFile.getUrl(), videoTitle,
//                                filename + "." + model.audioFile.getFormat().getExt(), hideAudioDownloadNotification);
//                    }
//                    if (model.audioFile != null)
//                        cacheDownloadIds(downloadIds);
//                    dialogDownload.dismiss();
////                    startDownload(model.videoFile.getUrl(),RootDirectoryYoutube,YoutubeActivity.this,getVideoFilenameFromURL(model.videoFile.getUrl()));
//                });
//                rvdownload.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                dialogDownload.show();
//
////                for (YtFragmentedVideo files : formatsToShowList) {
////                    addButtonToMainLayout(vMeta.getTitle(), files);
////                }
//            }
//        }.extract(youtubeLink);
//    }

    public String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

//    private void addFormatToList(YtFile ytFile, SparseArray<YtFile> ytFiles) {
//        int height = ytFile.getFormat().getHeight();
//        if (height != -1) {
//            for (YtFragmentedVideo frVideo : formatsToShowList) {
//                if (frVideo.height == height && (frVideo.videoFile == null ||
//                        frVideo.videoFile.getFormat().getFps() == ytFile.getFormat().getFps())) {
//                    return;
//                }
//            }
//        }
//        YtFragmentedVideo frVideo = new YtFragmentedVideo();
//        frVideo.height = height;
//        if (ytFile.getFormat().isDashContainer()) {
//            if (height > 0) {
//                frVideo.videoFile = ytFile;
//                frVideo.audioFile = ytFiles.get(ITAG_FOR_AUDIO);
//            } else {
//                frVideo.audioFile = ytFile;
//            }
//        } else {
//            frVideo.videoFile = ytFile;
//        }
//        formatsToShowList.add(frVideo);
//    }


    private void addButtonToMainLayout(final String videoTitle, final YtFragmentedVideo ytFrVideo) {
        // Display some buttons and let the user choose the format
        String btnText;
        if (ytFrVideo.height == -1)
            btnText = "Audio " + ytFrVideo.audioFile.getFormat().getAudioBitrate() + " kbit/s";
        else
            btnText = (ytFrVideo.videoFile.getFormat().getFps() == 60) ? ytFrVideo.height + "p60" :
                    ytFrVideo.height + "p";
        Button btn = new Button(this);
        btn.setText(btnText);
        btn.setOnClickListener(v -> {
            String filename;
            if (videoTitle.length() > 55) {
                filename = videoTitle.substring(0, 55);
            } else {
                filename = videoTitle;
            }
            filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
            filename += (ytFrVideo.height == -1) ? "" : "-" + ytFrVideo.height + "p";
            String downloadIds = "";
            boolean hideAudioDownloadNotification = false;
            if (ytFrVideo.videoFile != null) {
                downloadIds += downloadFromUrl(ytFrVideo.videoFile.getUrl(), videoTitle,
                        filename + "." + ytFrVideo.videoFile.getFormat().getExt(), false);
                downloadIds += "-";
                hideAudioDownloadNotification = true;
            }
            if (ytFrVideo.audioFile != null) {
                downloadIds += downloadFromUrl(ytFrVideo.audioFile.getUrl(), videoTitle,
                        filename + "." + ytFrVideo.audioFile.getFormat().getExt(), hideAudioDownloadNotification);
            }
            if (ytFrVideo.audioFile != null)
                cacheDownloadIds(downloadIds);
            finish();
        });
        mainLayout.addView(btn);
    }

    private long downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName, boolean hide) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);
        if (hide) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setVisibleInDownloadsUi(false);
        } else
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, RootDirectoryYoutube + fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }

    private void cacheDownloadIds(String downloadIds) {
        File dlCacheFile = new File(this.getCacheDir().getAbsolutePath() + "/" + downloadIds);
        try {
            dlCacheFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class YtFragmentedVideo {
        int height;
        YtFile audioFile;
        YtFile videoFile;

        public int getHeight() {
            return height;
        }

        public YtFile getAudioFile() {
            return audioFile;
        }

        public YtFile getVideoFile() {
            return videoFile;
        }
    }


}