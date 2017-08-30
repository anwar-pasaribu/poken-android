package id.unware.poken.httpConnection;

import java.util.Map;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.AddressBookDataRes;
import id.unware.poken.domain.CategoryDataRes;
import id.unware.poken.domain.Customer;
import id.unware.poken.domain.CustomerCollectionDataRes;
import id.unware.poken.domain.CustomerSubscriptionDataRes;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingCartDataRes;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderDataRes;
import id.unware.poken.domain.ShoppingOrderInserted;
import id.unware.poken.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface PokenRequest {

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_POKEN_AUTH)
    Call<User> postPokenLogin(
            @FieldMap() Map<String, String> postData);

    @GET(ConstantsRetrofit.ENDPOINT_GET_CUSTOMER)
    Call<Customer> getCustomerData(@Path("pk") String customerIdOrToken);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_HOME_CONTENT)
    Call<HomeDataRes> reqHomeContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_PRODUCT_CATEGORY)
    Call<CategoryDataRes> reqProductCategoriesContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_FEATURED_CATEGORY)
    Call<FeaturedCategoryProductDataRes> reqFeaturedProductCategoriesContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_GET_FEATURED)
    Call<Featured> reqSingleFeaturedItemDetail(@Path("pk") String featuredId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SINGLE_PRODUCT_DETAIL)
    Call<Product> reqSingleProductDetail(@Path("pk") long productId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_CART)
    Call<ShoppingCartDataRes> reqShoppingCartContent(@HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_SHOPPING_CART)
    Call<ShoppingCart> postNewOrUpdateShoppingCart(
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_SHOPPING_CART)
    Call<ShoppingCart> patchShoppingCartExtraNote(
            @Path("pk") long itemId,
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_ORDER_DETAIL)
    Call<OrderDetail> postNewOrUpdateOrderDetails(
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_ORDERED_PRODUCT)
    Call<ShoppingOrderInserted> postNewOrderedProduct(
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData,
            @Field("shopping_carts") long[] shoppingCartIds);


    @POST(ConstantsRetrofit.ENDPOINT_INSERT_ADDRESS_BOOK)
    Call<AddressBook> postNewAddressBook(
            @Header("Content-Type") String contentType,
            @HeaderMap Map<String, String> headerMap,
            @Body AddressBook addressBook);

    @DELETE(ConstantsRetrofit.ENDPOINT_DELETE_SHOPPING_CART)
    Call<Object> deleteShoppingCartContent(@HeaderMap Map<String, String> headerMap, @Path("pk") long shoppingCartId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_ADDRESS_BOOK)
    Call<AddressBookDataRes> reqAddressBookContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_ORDER)
    Call<ShoppingOrderDataRes> reqShoppingOrderContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_ORDER_DETAIL)
    Call<ShoppingOrder> reqShoppingOrderDetail(@HeaderMap Map<String, String> headerMap, @Path("pk") long orderId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_CUSTOMER_COLLECTION)
    Call<CustomerCollectionDataRes> reqCustomerCollectionContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_CUSTOMER_SUBSCRIPTION)
    Call<CustomerSubscriptionDataRes> reqCustomerSubscriptionContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_PRODUCTS)
    Call<ProductDataRes> reqProductContent(
            @HeaderMap Map<String, String> headerMap,
            @QueryMap Map<String, String> sellerData
    );

    @GET(ConstantsRetrofit.ENDPOINT_SEARCH_PRODUCTS)
    Call<ProductDataRes> reqSearchProductContent(
            @QueryMap Map<String, String> sellerData
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_PRODUCTS)
    Call<ProductDataRes> reqProductContentByCategory(
            @HeaderMap Map<String, String> credentials,
            @QueryMap Map<String, String> category
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_PRODUCTS)
    Call<ProductDataRes> reqProductContentByCategory(
            @QueryMap Map<String, String> category
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_PRODUCTS)
    Call<ProductDataRes> reqProductContentByActionId(
            @QueryMap Map<String, String> category
    );

}
