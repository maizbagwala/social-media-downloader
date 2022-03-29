package tk.maizbagwala.social.api;

import tk.maizbagwala.social.model.ResponseModel;
import tk.maizbagwala.social.model.TiktokModel;
import tk.maizbagwala.social.model.TwitterResponse;
import tk.maizbagwala.social.model.story.FullDetailModel;
import tk.maizbagwala.social.model.story.StoryModel;
import tk.maizbagwala.social.model.YtFormateResponse;

import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import retrofit2.http.Query;

public interface APIServices {

    @GET
    Observable<YtFormateResponse> getYtFormat(@Url String url, @Header("Origin") String origin, @Query("url") String videourl);

    @GET
    Observable<ResponseModel> getReelData(@Url String Value);

    @GET
    Observable<JsonObject> callResult(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @FormUrlEncoded
    @POST
    Observable<TwitterResponse> callTwitter(@Url String Url, @Field("id") String id);

    @POST
    Observable<TiktokModel> getTiktokData(@Url String Url, @Body HashMap<String, String> hashMap);

    @GET
    Observable<StoryModel> getStoriesApi(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @GET
    Observable<FullDetailModel> getFullDetailInfoApi(@Url String Value, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);
}