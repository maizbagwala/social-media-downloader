package tk.maizbagwala.social.api;

import android.app.Activity;

import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import tk.maizbagwala.social.model.ResponseModel;
import tk.maizbagwala.social.model.TwitterResponse;
import tk.maizbagwala.social.model.YtFormateResponse;
import tk.maizbagwala.social.model.story.FullDetailModel;
import tk.maizbagwala.social.model.story.StoryModel;
import tk.maizbagwala.social.util.Utils;

public class CommonClassForAPI {
    private static Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;

    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }

    public void getYtFormat(final DisposableObserver observer, String URL, String videoUrl) {
        RestClient.getInstance(mActivity).getService().getYtFormat(URL, "https://y2mate.is", videoUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YtFormateResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(YtFormateResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getReelData(final DisposableObserver observer, String URL) {
        RestClient.getInstance(mActivity).getService().getReelData(URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });

    }

    public void callResult(final DisposableObserver observer, String URL, String Cookie) {
        if (Utils.isNullOrEmpty(Cookie)) {
            Cookie = "";
        }
        RestClient.getInstance(mActivity).getService().callResult(URL, Cookie,
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void callTwitterApi(final DisposableObserver observer, String URL, String
            twitterModel) {
        RestClient.getInstance(mActivity).getService().callTwitter(URL, twitterModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TwitterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TwitterResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getStories(final DisposableObserver observer, String Cookie) {
        if (Utils.isNullOrEmpty(Cookie)) {
            Cookie = "";
        }
        RestClient.getInstance(mActivity).getService().getStoriesApi("https://i.instagram.com/api/v1/feed/reels_tray/", Cookie,
                "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoryModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(StoryModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getFullDetailFeed(final DisposableObserver observer, String UserId, String
            Cookie) {
        RestClient.getInstance(mActivity).getService().getFullDetailInfoApi(
                "https://i.instagram.com/api/v1/users/" + UserId + "/full_detail_info?max_id="
                , Cookie, "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FullDetailModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(FullDetailModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


}