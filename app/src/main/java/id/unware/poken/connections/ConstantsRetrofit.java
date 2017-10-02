package id.unware.poken.connections;

@SuppressWarnings("WeakerAccess")
public class ConstantsRetrofit {
    public static final String ENDPOINT_POKEN_REGISTER = "insert_user/";
    public static final String ENDPOINT_POKEN_AUTH = "api-token-auth/";
    public static final String ENDPOINT_GET_CUSTOMER = "customer/{pk}";
    public static final String ENDPOINT_GET_FEATURED = "featured/{pk}";
    public static final String ENDPOINT_TRACKING = "track3/{resi}";
    public static final String ENDPOINT_SEARCH_AREA = "area/{query}";
    public static final String ENDPOINT_CHECK_TARIFF = "tariff2/{origin}/{dest}/{weight}";
    public static final String ENDPOINT_FETCH_NEARBY_BRANCHES = "location/{lat}/{lon}";
    public static final String ENDPOINT_FETCH_HOME_CONTENT = "home/";
    public static final String ENDPOINT_FETCH_PRODUCT_CATEGORY = "product_category/";
    public static final String ENDPOINT_FETCH_FEATURED_CATEGORY = "product_category_featured/";
    public static final String ENDPOINT_FETCH_SINGLE_PRODUCT_DETAIL = "product/{pk}/";
    public static final String ENDPOINT_FETCH_SHOPPING_CART = "cart/";
    public static final String ENDPOINT_PATCH_SHOPPING_CART = "cart/{pk}/";
    public static final String ENDPOINT_PATCH_ORDER_DETAILS_STATUS = "order_details/{pk}/";
    public static final String ENDPOINT_INSERT_SHOPPING_CART = "insert_cart/";
    public static final String ENDPOINT_INSERT_SELLER_SUBSCRIPTION = "insert_customer_subscribed/";
    public static final String ENDPOINT_INSERT_ORDER_DETAIL = "insert_order_detail/";
    public static final String ENDPOINT_INSERT_ORDERED_PRODUCT = "insert_ordered_product/";
    public static final String ENDPOINT_INSERT_ADDRESS_BOOK = "address_book/";
    public static final String ENDPOINT_DELETE_SHOPPING_CART = "cart/{pk}/";
    public static final String ENDPOINT_DELETE_ADDRESS_BOOK = "address_book/{pk}/";
    public static final String ENDPOINT_PATCH_ADDRESS_BOOK = "address_book/{pk}/";
    public static final String ENDPOINT_FETCH_SHOPPING_ORDER = "ordered_product/";
    public static final String ENDPOINT_FETCH_SHOPPING_ORDER_DETAIL = "ordered_product/{pk}";
    public static final String ENDPOINT_FETCH_ADDRESS_BOOK = "address_book/";
    public static final String ENDPOINT_FETCH_CUSTOMER_COLLECTION = "customer_collection/";
    public static final String ENDPOINT_FETCH_CUSTOMER_SUBSCRIPTION = "customer_subscription/";
    public static final String ENDPOINT_FETCH_PRODUCTS = "product/";
    public static final String ENDPOINT_SEARCH_PRODUCTS = "product/";
}
