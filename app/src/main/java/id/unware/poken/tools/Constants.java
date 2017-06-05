package id.unware.poken.tools;

/**
 * Created by marzellaalfamega on 4/7/16.
 * Various constants.
 */
public class Constants {
    public static final int TAG_HEADER_RECENT = -99;
    public static final int TAG_TRACKING = 1;
    public static final String LAST_ORIGIN = "LAST_ORIGIN";

    // HOME SECTION ID
    public static final int HOME_SECTION_TOP_SELLER = 2;
    public static final int HOME_SECTION_SALE_PRODUCT = 3;


    // SECTION ACTION
    public static final int TAG_PRODUCTS = 0;  // Grid/List/many product in one screen
    public static final int TAG_PRODUCTS_WITH_INTENTION = 1;  // Grid/List/many product in one screen
    public static final int TAG_PRODUCT_DETAIL = 2;  // Product detail












    public static boolean focusOnSender = false;

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
    // [V50] Visible tabs
    public static final int TAG_PACKAGE = 0;
    public static final int TAG_PICKUP_MAP = 22;
    public static final int TAG_PICKUP_FORM = 25;
    public static final int TAG_WALLET = 30;
    public static final int TAG_TARIFF_CHECK = 5;
    public static final int TAG_MORE = 8;
    public static final int TAG_DRIVER = 2;
    public static final int TAG_ADDRESS = 3;
    public static final int TAG_MAP = 4;  // Drop off location
    public static final int TAG_LOGOUT = 6;
    public static final int TAG_LOGIN = 30;
    public static final int TAG_POSTCODE = 7;
    public static final int TAG_PACKAGE_DETAIL = 20;
    public static final int TAG_RATE_ON_MARKET = 21;
    public static final int TAG_NEW_PACKAGE = 23;
    public static final int TAG_PICKUP_HISTORY = 24;
    public static final int TAG_PUSH_NOTIF = 26;
    public static final int TAG_HELP = 27;
    public static final int TAG_SETTINGS = 28;
    public static final int TAG_SUPPORT = 29;
    public static final String TAG_SUPPORT_MAP = "24";
    // Extra keys for cross activity/fragment
    public static final String EXTRA_FROM_NEW_PACKAGE = "fromNewPackage";
    public static final String EXTRA_ADDRESS_DETAIL = "address_detail";
    public static final String EXTRA_PHONE_NUMBER = "phone_number";
    // Extra for PojoBooking
    public static final String EXTRA_POJO_BOOKING_DATA = "data";
    // Extra to decide refresh Package list.
    public static final String EXTRA_REFRESH_PAGE = "refresh";
    public static final String EXTRA_TRIGGER_TUTORIAL = "trigger_tut";
    // Extra to store booking_id on an ArrayList
    public static final String EXTRA_ARRAYLIST_BOOKING_ID = "array_list_booking_id";
    // Extra flag whether in tut mode
    public static final String EXTRA_IS_TUTORIAL = "is_in_tut_mode";
    /**
     * Key to store {@link android.location.Address} data on Intent extra.
     */
    public static final String EXTRA_SELECTED_ADDRESS = "selected_address";
    // Param keys
    public static final String PARAM_USER_EMAIL = "user_email";
    public static final String PARAM_USER_PASSWORD = "user_password";
    public static final String PARAM_POSITION = "position";
    // Shared Preferences to record whether tutorial is learnt or not
    // 3 Main tutorials: (1) Open New Package, (2) Create New Package, (3) Request Pickup
    public static final String SHARED_TUTORIAL_MODE = "is_tutorial_mode";
    public static final String SHARED_LEARNT_OPEN_CREATE_PACKAGE = "new_pack_open_new_pack";
    public static final String SHARED_LEARNT_SENDER_FIELD = "new_pack_sender";
    public static final String SHARED_LEARNT_RECEIVER_FIELD = "new_pack_receiver";
    public static final String SHARED_LEARNT_PROCEDD_CREATE = "new_pack_proceed_create";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    // Shared Preferences key to track when no package available
    public static final String SHARED_IS_PACKAGE_EMPTY = "is_package_empty";
    // Shared Pref. key for last origin and destination (Tariff check)
    public static final String SHARED_LAST_ORIGIN = "last_origin";
    public static final String SHARED_LAST_DESTINATION = "last_destination";
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
    public static final String EXTRA_LIST_WALLET_TRANSACTION_HISTORY = "EXTRA_LIST_WALLET_TRANSACTION_HISTORY";
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
    /**
     * Minimum amount to withdraw.
     */
    public static long MIN_WITHDRAW_VALUE = 10000;
    /**
     * Maximum amount to withdraw.
     */
    public static int MAX_WITHDRAW_VALUE = 79890000;
    // Flag to indicate in tutorial mode on/off
    public static boolean isTutorialMode = false;

    public static int toolbarHeight = 0;
    public static int statusBarHeight = 0;

}
