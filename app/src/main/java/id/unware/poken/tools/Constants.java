package id.unware.poken.tools;

/**
 * Created by marzellaalfamega on 4/7/16.
 * Various constants.
 */

public class Constants {
    public static final String KEY_DOMAIN_ITEM_ID = "id";

    public static final int TAG_TRACKING = 1;
    public static final String LAST_ORIGIN = "LAST_ORIGIN";

    // HOME SECTION ID
    public static final int HOME_SECTION_TOP_SELLER = 2;
    public static final int HOME_SECTION_SALE_PRODUCT = 3;


    // SECTION ACTION
    public static final int TAG_PRODUCTS = 0;  // Grid/List/many product in one screen
    public static final int TAG_PRODUCTS_WITH_INTENTION = 1;  // Grid/List/many product in one screen
    public static final int TAG_PRODUCT_DETAIL = 2;  // Product detail

    // SP (Shared Preferences)
    public static final String SP_AUTH_TOKEN = "AUTH_TOKEN";
    public static final String SP_AUTH_USERNAME = "AUTH_USERNAME";
    public static final String SP_AUTH_EMAIL = "AUTH_EMAIL";
    public static final String SP_AUTH_PASSWORD = "AUTH_PASSWORD";
    public static final String SP_AUTH_CUSTOMER_DATA = "AUTH_CUSTOMER_DATA";

    // SELLER DATA
    public static final String SP_SELLER_ID = "SP_SELLER_ID";

    // STATUS NUMBER FOR NETWORK CALL STATUS
    public static final int NETWORK_CALLBACK_FAILURE = -1;

    // PRE CHECK WHEN ID NOT AVAILABLE
    public static final int ID_NOT_AVAILABLE = -1;

    public static final String EXTRA_ORDER_ID = "SHOPPING_ORDER_ID";
    public static final String EXTRA_ORDER_REF = "EXTRA_ORDER_REF";
    public static final String EXTRA_ORDER_STATUS = "EXTRA_TOPUP_STATUS";
    public static final String EXTRA_GENERAL_INTENT_ID = "INTENT_ID";
    public static final String EXTRA_GENERAL_INTENT_VALUE = "INTENT_VAL";
    public static final String EXTRA_IS_BROWSE_BY_CATEGORY = "IS_BROWSE_BY_CAT";
    public static final String EXTRA_IS_LAUNCH_FAVORITE = "is_launch_favo";
    public static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String EXTRA_TOTAL_SHOPPING_COST = "TOTAL_SHOPPING_COST";
    public static final String EXTRA_PAYMENT_DUE = "PAYMENT_DUE";
    public static final String EXTRA_REQUESTED_PAGE = "EXTRA_REQUESTED_PAGE";
    public static final String EXTRA_SELECTED_ADDRESS_BOOK = "EXTRA_SELECTED_ADDRESS_BOOK";
    public static final String EXTRA_PARCELABLE_CUSTOMER = "EXTRA_PARCELABLE_CUSTOMER";
    public static final String EXTRA_DOMAIN_SERIALIZED_STRING = "EXTRA_DOMAIN_SERIALIZED_STRING";
    public static final String EXTRA_DOMAIN_PARCELABLE_DATA = "EXTRA_DOMAIN_PARCELABLE_DATA";
    public static final String EXTRA_PRODUCT_DATA = "EXTRA_PRODUCT_DATA";
    public static final String EXTRA_PRODUCT_DETAIL_IS_EDIT = "EXTRA_PRODUCT_DETAIL_IS_EDIT";

    public static final int INTENT_BROWSE_BY_CATEGORY = -33;

    public static final int VIEWFLIPPER_CHILD_SALE = 0;
    public static final int VIEWFLIPPER_CHILD_DEFAULT = 1;
    public static final String FABRIC_CONTENT_TYPE = "Page";


    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 314;

    public static final int POS_FROM_SEARCH_PACKAGE_RESULTS = -99;

    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    // Various STATE for view state while loading data
    public static final int STATE_ERROR = -2;
    public static final int STATE_NODATA = -1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_FINISHED = 1;
    public static final int STATE_DEFAULT = 2;

    public static final String PACKAGE_NAME = "com.indoskyware.paket.services";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String SHARED_UPDATED_ON_WALLET = "UPDATED_ON_WALLET";
    public static final String SHARED_COOKIE = "SHARED_COOKIE";
    public static final String SHARED_PROFILE_NAME = "profile_name";
    public static final String SHARED_PROFILE_PHONE = "profile_phone";
    public static final String SHARED_PROFILE_PHONE_VERIFY = "profile_phone_verify";
    public static final String SHARED_NAME = "name";
    public static final String SHARED_ADDRESS = "address";
    public static final String SHARED_POST_CODE = "post_code";
    public static final String SHARED_PHONE = "phone";
    public static final String SHARED_EMAIL = "email";
    public static final String SHARED_LAST_UPDATE = "last_update";
    public static final String SHARED_DATABSE_VERSION = "db_version";
    public static final String SHARED_LOAD_AREA_LV0 = "is_load_area_lv_0";

    public static final int TAG_PROFILE = 2;
    public static final int TAG_LOGIN = 30;
    public static final int TAG_SHOPPING_CART = 31;
    public static final int TAG_ADD_SHOPPING_CART = 32;
    public static final int TAG_BUY_NOW = 33;
    public static final int TAG_FEATURED = 34;
    public static final int TAG_FEATURE_SUBSCRIPTION = 35;
    public static final int TAG_EDIT_PROFILE = 37;

    public static final int TAG_STORE_SUMMARY = 38;
    public static final int TAG_STORE_MANAGE_PRODUCT = 39;

    public static final String EXTRA_SELECTED_SHOPPING_CART_IDS = "selected_shopping_cart_ids";
    public static final String EXTRA_SELECTED_SHOPPING_CART = "selected_shopping_cart";
    // Shared number of OTW driver
    public static final String SHARED_OTW_DRIVER = "otw_driver";
    // Shared to identify whether user has modified Expandable RecyclerView
    public static final String SHARED_HAS_MODIFY_PACKAGE_LIST = "has_modify_package_list";
    public static final String SHARED_HAS_UPDATED = "app_has_updated";
    /**
     * Waiting duration for non-immediate network request ex. autosave on profile
     */
    public static final int DURATION_SUPER_LONG = 1000;
    /**
     * Max PojoBooking on database
     */
    public static final int MAX_PACKAGES_ON_DB = 1000;
    /**
     * Indicate package/PojoBooking item is header on list
     */
    public static final int HEADER_ITEM_ID = -99;
    /**
     * Indicate package item is footer for Load More feature
     */
    public static final int FOOTER_LOAD_MORE_ITEM_ID = -999;
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    //------------------------ BUNDLE EXTRA -----------------------//
    public static final String EXTRA_TOPUP_STATUS = "EXTRA_TOPUP_STATUS";
    public static final String EXTRA_WITHDRAWAL_STATUS = "EXTRA_WITHDRAWAL_STATUS";
    public static final String EXTRA_WITHDRAWAL_USER_BANK_LIST = "EXTRA_WITHDRAWAL_USER_BANK_LIST";
    public static final String EXTRA_WITHDRAWAL_BANK_LIST = "EXTRA_WITHDRAWAL_BANK_LIST";
    public static final String EXTRA_POJO_WITHDRAW = "EXTRA_POJO_WITHDRAW";
    public static final String EXTRA_WALLET_CONFIG = "EXTRA_WALLET_CONFIG";
    public static final String EXTRA_WALLET_USER_BALANCE = "EXTRA_WALLET_USER_BALANCE";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    /*
     * [V49]
     * New instance argument for New Package summary.
     * {@link PojoNewPackage} implements Parcelable
     */
    public static final String EXTRA_NEW_PACKAGE_DATA = "EXTRA_NEW_PACKAGE_DATA";
    public static final String EXTRA_PACKAGE_POSITION = "package_pos_on_list";
    public static final String EXTRA_SHOW_ACTIVE_PICKUP = "show_active";
    public static final String VERSION_CODE = "VERSION_CODE";
    public static final String USE_FRAGMENT = "user_fragment";
    public static final String USER_ID = "user_Id";
    public static final String GOOGLE_TOKEN_ID = "GOOGLE_TOKEN_ID";
    public static final String GCM_TOKEN = "token";
    public static final String FB_TOKEN_ID = "FB_TOKEN_ID";

    public static int PRODUCT_CATEGORY_ALL = -99;
    // Flag to indicate in tutorial mode on/off
    public static boolean isTutorialMode = false;

    public static int toolbarHeight = 0;
    public static int statusBarHeight = 0;

    public static int SECTION_ID_POPULAR_STORE = 2;
    public static int SECTION_ID_SALE_PRODUCT = 3;


    public static int REQ_CODE_ADDRESS_BOOK = 99;

    // KEY VALUE PAIR STRUCTURE
    public static String KV_ID = "KV_ID";
    public static String KV_PRICE = "KV_PRICE";
    public static String KV_NAME = "KV_NAME";
    public static String KV_CATEGORY = "KV_CATEGORY";
    public static String KV_SHIPPING_FEE = "KV_SHIPPING_FEE";
    public static String KV_QUANTITY = "KV_QUANTITY";


}
