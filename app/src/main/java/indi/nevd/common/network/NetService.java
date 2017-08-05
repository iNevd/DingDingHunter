package indi.nevd.common.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by nevd on 8/3/2017.
 */

public interface NetService {
    @FormUrlEncoded
    @POST("{url}")
    Call<BaseNetModel> executeGet(
            @Path("url") String url,
            @FieldMap Map<String, String> parameters);
}
