package id.unware.poken.connections;

import java.util.Map;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.AddressBookDataRes;
import id.unware.poken.domain.Customer;
import id.unware.poken.domain.CustomerCollectionDataRes;
import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.domain.CustomerSubscriptionDataRes;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.domain.OrderCreditDataRes;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductCategoryDataRes;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.ProductInserted;
import id.unware.poken.domain.SellerDataRes;
import id.unware.poken.domain.ShippingRatesDataRes;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingCartDataRes;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderDataRes;
import id.unware.poken.domain.ShoppingOrderInserted;
import id.unware.poken.domain.StoreSummaryDataRes;
import id.unware.poken.domain.User;
import id.unware.poken.domain.UserBankDataRes;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */
public interface PokenRequest {

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_POKEN_REGISTER)
    Call<User> postPokenRegisterEmail(
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_POKEN_AUTH)
    Call<User> postPokenLogin(
            @FieldMap() Map<String, String> postData);

    @GET(ConstantsRetrofit.ENDPOINT_GET_CUSTOMER)
    Call<Customer> getCustomerData(@Path("pk") String customerIdOrToken);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_HOME_CONTENT)
    Call<HomeDataRes> reqHomeContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_FEATURED_CATEGORY)
    Call<FeaturedCategoryProductDataRes> reqFeaturedProductCategoriesContent(
            @QueryMap Map<String, String> queryMap);

    @GET(ConstantsRetrofit.ENDPOINT_GET_FEATURED)
    Call<Featured> reqSingleFeaturedItemDetail(@Path("pk") String featuredId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SINGLE_PRODUCT_DETAIL)
    Call<Product> reqSingleProductDetail(@Path("pk") long productId);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SHOPPING_CART)
    Call<ShoppingCartDataRes> reqShoppingCartContent(@HeaderMap Map<String, String> headerMap);

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_STORE_SUMMARY)
    Observable<StoreSummaryDataRes> reqStoreSummary(
            @HeaderMap Map<String, String> credentials
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_STORE_PRODUCT)
    Observable<ProductDataRes> reqStoreProduct(
            @HeaderMap Map<String, String> credentials
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_STORE_CREDIT)
    Observable<OrderCreditDataRes> reqStoreCredit(
            @HeaderMap Map<String, String> credentials,
            @QueryMap() Map<String, String> queryParams
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_PRODUCT_CATEGORY)
    Observable<ProductCategoryDataRes> reqProductCategory(
            @HeaderMap Map<String, String> credentials
    );

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_SHOPPING_CART)
    Call<ShoppingCart> postNewOrUpdateShoppingCart(
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_CUSTOMER_PROFILE)
    Call<Customer> patchCustomerProfile(
            @Path("pk") long itemId,
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_SHOPPING_CART)
    Call<ShoppingCart> patchShoppingCartExtraNote(
            @Path("pk") long itemId,
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_ORDER_DETAILS_STATUS)
    Call<OrderDetail> patchOrderDetailsStatus(
            @Path("pk") long itemId,
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @FormUrlEncoded
    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_ORDER_DETAILS_TRACKING_ID)
    Call<OrderDetail> patchOrderDetailsTrackingId(
            @Path("pk") long itemId,
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_ADDRESS_BOOK)
    Call<AddressBook> patchAddressBookChanges(
            @Header("Content-Type") String contentType,
            @Path("pk") long itemId,
            @HeaderMap Map<String, String> headerMap,
            @Body AddressBook addressBook);

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

    @POST(ConstantsRetrofit.ENDPOINT_INSERT_PRODUCT)
    Observable<ProductInserted> postNewProduct(
            @Header("Content-Type") String contentType,
            @HeaderMap Map<String, String> credentials,
            @Body ProductInserted postBody);

    @PATCH(ConstantsRetrofit.ENDPOINT_PATCH_PRODUCT)
    Observable<ProductInserted> patchProduct(
            @HeaderMap Map<String, String> credentials,
            @Header("Content-Type") String contentType,
            @Path("pk") long itemId,
            @Body ProductInserted postBody);


    @POST(ConstantsRetrofit.ENDPOINT_INSERT_ADDRESS_BOOK)
    Call<AddressBook> postNewAddressBook(
            @Header("Content-Type") String contentType,
            @HeaderMap Map<String, String> headerMap,
            @Body AddressBook addressBook);

    @FormUrlEncoded
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_SELLER_SUBSCRIPTION)
    Call<CustomerSubscription> postSellerSubscription(
            @HeaderMap Map<String, String> headerMap,
            @FieldMap() Map<String, String> postData);

    @DELETE(ConstantsRetrofit.ENDPOINT_DELETE_SHOPPING_CART)
    Call<Object> deleteShoppingCartContent(@HeaderMap Map<String, String> headerMap, @Path("pk") long shoppingCartId);

    @DELETE(ConstantsRetrofit.ENDPOINT_DELETE_ADDRESS_BOOK)
    Call<Object> deleteAddressBookContent(@HeaderMap Map<String, String> headerMap, @Path("pk") long addressBookId);

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

    @GET(ConstantsRetrofit.ENDPOINT_SHIPPING_RATES)
    Call<ShippingRatesDataRes> reqShippingRates(
            @HeaderMap Map<String, String> credentials,
            @QueryMap Map<String, String> category
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_SELLERS)
    Call<SellerDataRes> reqSellerList(
            @QueryMap Map<String, String> category
    );

    @GET(ConstantsRetrofit.ENDPOINT_FETCH_POKEN_BANK_LIST)
    Call<UserBankDataRes> getBankList(@QueryMap Map<String, String> credentialHashMap);

    /**
     * Post product image.
     */
    @Multipart
    @POST(ConstantsRetrofit.ENDPOINT_INSERT_PRODUCT_IMAGE)
    Observable<ProductImage> uploadProductImage(@HeaderMap Map<String, String> credentials,
                                                @Part MultipartBody.Part file,
                                                @Part("path") RequestBody name,
                                                @Part("title") RequestBody title,
                                                @Part("description") RequestBody description);
}
