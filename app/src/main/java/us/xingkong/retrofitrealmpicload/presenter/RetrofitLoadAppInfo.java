package us.xingkong.retrofitrealmpicload.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import us.xingkong.retrofitrealmpicload.api.APIService;
import us.xingkong.retrofitrealmpicload.bean.AppInfo;

import static us.xingkong.retrofitrealmpicload.bean.AppInfo.appInfo;

/**
 * Created by Arcobaleno on 2017/7/9.
 */

public class RetrofitLoadAppInfo {

    private String url;

    private Retrofit retrofit;

    private APIService service;

    private Call<ResponseBody> call;

    private Context context;

    public RetrofitLoadAppInfo(Context context) {
        url = "http://api.fir.im/";
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
        call = service.getAppInfo();
        this.context = context;
    }

    public RetrofitLoadAppInfo(String url, Retrofit retrofit, APIService service, Call<ResponseBody> call) {
        this.url = url;
        this.retrofit = retrofit;
        this.service = service;
        this.call = call;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setService(APIService service) {
        this.service = service;
    }

    public APIService getService() {
        return service;
    }

    public void setCall(Call<ResponseBody> call) {
        this.call = call;
    }

    public Call<ResponseBody> getCall() {
        return call;
    }

    public void call() {
        //异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("appInfo----", "I am ready");
                // Get result bean from response.body()
                ResponseBody appInfoResponse = response.body();
                // Get header item from response
//                String links = response.headers().get("Link");

                try {
                    String appInfoString = null;
                    if (appInfoResponse != null) {
                        appInfoString = appInfoResponse.string();
                    }
                    if (appInfoString != null) {
                        appInfo = new Gson().fromJson(appInfoString, AppInfo.class);
                        upLoadData();
                        Log.i("appInfo----", String.valueOf(appInfo.getItems().get(0).getId()));

                    }
                    Log.i("appInfo----", appInfoString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //取消
                call.cancel();
                /**
                 * 不同于retrofit1 可以同时操作序列化数据javabean和header
                 */
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("appInfo----", "I am Failure");
                try {
                    appInfo = queryAllAppInfo().get(0);
                    Log.i("appIcon--",appInfo.getItems().get(3).getIcon_url());
                } catch (IndexOutOfBoundsException e) {
                    if (appInfo == null) {
                        Toast.makeText(context, queryAllAppInfo().size()+"Cache is empty! Please check your Network!", Toast.LENGTH_SHORT).show();
                    }
                }
                t.printStackTrace();
            }
        });
    }

    //        try {
//            Response<List<AppInfo>> appInfo = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        call只能调用一次。否则会抛 IllegalStateException
//        Call<List<AppInfo>> clone = call.clone();


//        Log.i("appInfo----", "I am ready");

     List<AppInfo> queryAllAppInfo() {
        Realm mRealm=Realm.getDefaultInstance();
        RealmResults<AppInfo> appInfo = mRealm.where(AppInfo.class).findAll();
        return mRealm.copyFromRealm(appInfo);
    }

    void upLoadData() {
        Realm m_realm = Realm.getDefaultInstance();
        // Copy the object to Realm. Any further changes must happen on realmUser
        m_realm.beginTransaction();
        try {
            m_realm.deleteAll();
            m_realm.copyToRealm(appInfo);
            Toast.makeText(context, "UpLoad Data to Realm is successful!", Toast.LENGTH_SHORT).show();
            m_realm.commitTransaction();
            Log.i("UpLoad---", "success");
        } catch (RealmPrimaryKeyConstraintException | IllegalArgumentException e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        m_realm.close();
    }

}
