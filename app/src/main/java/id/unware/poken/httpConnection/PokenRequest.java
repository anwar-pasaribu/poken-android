package id.unware.poken.httpConnection;

import java.util.Map;

import id.unware.poken.domain.CustomerCollectionDataRes;
import id.unware.poken.domain.CustomerSubscriptionDataRes;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.domain.ShoppingCartDataRes;
import id.unware.poken.domain.ShoppingOrderDataRes;
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

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_ORDER)
    Call<ShoppingOrderDataRes> reqShoppingOrderContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_CUSTOMER_COLLECTION)
    Call<CustomerCollectionDataRes> reqCustomerCollectionContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_CUSTOMER_SUBSCRIPTION)
    Call<CustomerSubscriptionDataRes> reqCustomerSubscriptionContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SELLER_PRODUCTS)
    Call<ProductDataRes> reqProductContent(@HeaderMap Map<String, String> headerMap);

}
