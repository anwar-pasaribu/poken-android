package id.unware.poken.httpConnection;

import java.util.Map;

import id.unware.poken.domain.HomeDataRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface PokenRequest {
    @GET(ConstantsRetrofit.ENDPOINT_FETCH_HOME_CONTENT)
    Call<HomeDataRes> reqHomeContent(@HeaderMap Map<String, String> headerMap);
}
