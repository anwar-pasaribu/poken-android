package id.unware.poken.httpConnection;

import java.util.Map;

import id.unware.poken.domain.CustomerCollectionDataRes;
import id.unware.poken.domain.CustomerSubscriptionDataRes;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingCartDataRes;
import id.unware.poken.domain.ShoppingOrderDataRes;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

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

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_SHOPPING_CART)
    Call<ShoppingCart> postNewOrUpdateShoppingCart(
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @DELETE(ConstantsRetrofit.ENDPOINT_DELETE_SHOPPING_CART)
    Call<Object> deleteShoppingCartContent(@HeaderMap Map<String, String> headerMap, @Path("pk") long shoppingCartId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_ORDER)
    Call<ShoppingOrderDataRes> reqShoppingOrderContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_CUSTOMER_COLLECTION)
    Call<CustomerCollectionDataRes> reqCustomerCollectionContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_CUSTOMER_SUBSCRIPTION)
    Call<CustomerSubscriptionDataRes> reqCustomerSubscriptionContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SELLER_PRODUCTS)
    Call<ProductDataRes> reqProductContent(@HeaderMap Map<String, String> headerMap, @QueryMap Map<String, String> sellerData);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SELLER_PRODUCTS)
    Call<ProductDataRes> reqProductContentByCategory(@HeaderMap Map<String, String> credentials, @QueryMap Map<String, String> category);

}
