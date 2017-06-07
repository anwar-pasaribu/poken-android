package id.unware.poken.httpConnection;

import java.util.Map;

import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShoppingCartDataRes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface PokenRequest {

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_HOME_CONTENT)
    Call<HomeDataRes> reqHomeContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SINGLE_PRODUCT_DETAIL)
    Call<Product> reqSingleProductDetail(@Path("pk") long productId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_CART)
    Call<ShoppingCartDataRes> reqShoppingCartContent(@HeaderMap Map<String, String> headerMap);
}
