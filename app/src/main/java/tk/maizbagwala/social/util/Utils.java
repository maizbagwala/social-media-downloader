package tk.maizbagwala.social.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import tk.maizbagwala.social.R;
import tk.maizbagwala.social.model.Status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

public class Utils {
    public static Dialog customDialog;
    private static Context context;

    public static String TAG = "SOCIAL_";

    public static String RootDirectoryFacebook = "/Social/Facebook/";
    public static String RootDirectoryInsta = "/Social/Insta/";
    public static String RootDirectoryTikTok = "/Social/TikTok/";
    public static String RootDirectoryTwitter = "/Social/Twitter/";
    public static String RootDirectoryYoutube = "/Social/Youtube/";
    public static String RootDirectoryLikee = "/Social/Likee/";

    public static File RootDirectoryFacebookShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/Facebook");
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/Insta");
    public static File RootDirectoryTikTokShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/TikTok");
    public static File RootDirectoryTwitterShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/Twitter");
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/Whatsapp");
    public static File RootDirectoryLikeeShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/Likee");
    public static File RootDirectoryYouTubeShow = new File(Environment.getExternalStorageDirectory() + "/Download/Social/YouTube");

    public static String PrivacyPolicyUrl = "https://sites.google.com/view/social-media-downloader-";
    public static String APP_DIR = Environment.getExternalStorageDirectory() + "/Download/Social/Whatsapp";

    public Utils(Context _mContext) {
        context = _mContext;
    }


    public static void copyFile(Status status, Context context, RelativeLayout container) {

        File file = new File(APP_DIR);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Snackbar.make(container, "Something went wrong", Snackbar.LENGTH_SHORT).show();
            }
        }

        String fileName;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        if (status.isVideo()) {
            fileName = "VID_" + currentDateTime + ".mp4";
        } else {
            fileName = "IMG_" + currentDateTime + ".jpg";
        }

        File destFile = new File(file + File.separator + fileName);

        try {

            org.apache.commons.io.FileUtils.copyFile(status.getFile(), destFile);

            new SingleMediaScanner(context, file, null, null, null);
//            showNotification(context, container, destFile, status);
            Toast.makeText(context, context.getResources().getString(R.string.saved_to) + APP_DIR, Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void copyFileFromUri(Status status, Context context, RelativeLayout container, @Nullable Bitmap bitmap) {

        InputStream inputStream;
        OutputStream outputStream;

        File file = new File(APP_DIR);

        String fileName;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        if (status.isVideo()) {
            fileName = "VID_" + currentDateTime + ".mp4";
        } else {
            fileName = "IMG_" + currentDateTime + ".jpg";
        }

        File destFile = new File(file + File.separator + fileName);

        try {

            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(status.getFileUri());

            outputStream = new FileOutputStream(destFile);
            Log.i("Download Status: ", "Output Stream Opened successfully");

            byte[] buffer = new byte[1000];
            while (inputStream.read(buffer, 0, buffer.length) >= 0) {
                outputStream.write(buffer, 0, buffer.length);
            }

            new SingleMediaScanner(context, null, status, fileName, bitmap);
            Toast.makeText(context, context.getResources().getString(R.string.saved_to) + APP_DIR, Toast.LENGTH_LONG).show();


        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void createFileFolder() {
        if (!RootDirectoryFacebookShow.exists()) {
            RootDirectoryFacebookShow.mkdirs();
        }
        if (!RootDirectoryInstaShow.exists()) {
            RootDirectoryInstaShow.mkdirs();
        }
        if (!RootDirectoryTikTokShow.exists()) {
            RootDirectoryTikTokShow.mkdirs();
        }
        if (!RootDirectoryTwitterShow.exists()) {
            RootDirectoryTwitterShow.mkdirs();
        }
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
        if (!RootDirectoryLikeeShow.exists()) {
            RootDirectoryLikeeShow.mkdirs();
        }
        if (!RootDirectoryYouTubeShow.exists()) {
            RootDirectoryYouTubeShow.mkdirs();
        }

    }

    public static void showProgressDialog(Activity activity) {
        System.out.println("Show");
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        System.out.println("Hide");
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startDownload(String downloadPath, String destinationPath, Context context, String FileName) {
        setToast(context, context.getResources().getString(R.string.download_started));
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(FileName + ""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, destinationPath + FileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName).getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_txt));
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_image_via)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void shareImageVideoOnWhatsapp(Context context, String filePath, boolean isVideo) {
        Uri imageUri = Uri.parse(filePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        if (isVideo) {
            shareIntent.setType("video/*");
        } else {
            shareIntent.setType("image/*");
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            Utils.setToast(context, context.getResources().getString(R.string.whatsapp_not_installed));
        }
    }

    public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, context.getResources().getString(R.string.no_app_installed), Toast.LENGTH_LONG).show();
        }
    }

    public static void RateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void UpdateApp(Context context) {
        final String appName = context.getPackageName();
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
    }

    public static void MoreApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void ShareApp(Context context) {
        final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message) + appLink);
        sendInt.setType("text/plain");
        context.startActivity(Intent.createChooser(sendInt, "Share"));
    }

    public static void OpenApp(Context context, String Package) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Package);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            setToast(context, context.getResources().getString(R.string.app_not_available));
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null")) || (s.equalsIgnoreCase("0"));
    }

    public static boolean dir_exists(String dir_path) {
        boolean ret = false;
        File dir = new File(dir_path);
        if (dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }
}