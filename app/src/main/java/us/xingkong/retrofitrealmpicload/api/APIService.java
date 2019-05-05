package us.xingkong.retrofitrealmpicload.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Arcobaleno on 2017/7/8.
 */

public interface APIService {
    /**
     * 如果不需要转换成Json数据,可以用了ResponseBody;
     * @return call
     */
    @GET("apps?api_token={Your API Token}")
//    Call<AppInfo> getAppInfo();
    Call<ResponseBody> getAppInfo();
    // you can add some other meathod
}
