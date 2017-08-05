package indi.nevd.common.network;

import android.util.Log;

import indi.nevd.common.Util;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nevd on 8/3/2017.
 */

public class RetrofitUtils {
    private static final RetrofitUtils instance = new RetrofitUtils();
    private NetService apiService;
    private OkHttpClient okHttpClient;
    private final static int DEFAULT_TIMEOUT = 5;
    private final static String host = "http://xxx.xxx.com";

    private final static String TAG = "DingDingHunter";

    public final static String uri_notice = "notice";
    private static String baseLogPath = Util.getSDPath() + File.separator + "DingDingHunter" + File.separator + "log_";

    public static RetrofitUtils getInstance() {
        return instance;
    }


    private RetrofitUtils() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(host/*String.format(URL_FORMAT, (isUseHttps ? "https" : "http"), host)*/)
                .build();
        apiService = retrofit.create(NetService.class);
    }

    public void asyncPostNotice(final Map parameters) {
        Call<BaseNetModel> call = apiService.executeGet(uri_notice, parameters);

        call.enqueue(new Callback<BaseNetModel>() {

            @Override
            public void onResponse(Call<BaseNetModel> call, retrofit2.Response<BaseNetModel> response) {
                if (call != null && response != null && !call.isCanceled() && (response.raw().code() == 200 || response.raw().code() == 206 || response.raw().code() == 304)){
                    if(response.body().err_no == 0 && response.body().errno == 0 && response.body().errNo ==0){
                        String text = call.request().url() + "?" + Util.mapToUriString(parameters);
                        Log.i(TAG, text);
                        Util.appendStringToFile(baseLogPath + Util.getDateYMD(), text);
                        return;
                    }
                }
                onFailure(call, new RuntimeException("response error,detail = " + response.raw().toString()));
            }

            @Override
            public void onFailure(Call<BaseNetModel> call, Throwable throwable) {
                String text = call.request().url() + "?" + Util.mapToUriString(parameters);
                text = "发送失败: " + throwable.getMessage() + "###" + text;
                Log.e(TAG, "Send Failed : " + text);
                Util.appendStringToFile(baseLogPath + Util.getDateYMD() + ".wf", text);
            }
        });
    }
}
