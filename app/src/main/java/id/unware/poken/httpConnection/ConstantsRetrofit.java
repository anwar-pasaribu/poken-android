package id.unware.poken.httpConnection;

/**
 * Created by marzellaalfamega on 3/20/17.
 *
 */

public class ConstantsRetrofit {
    public static final String ENDPOINT_TRACKING = "track3/{resi}";
    public static final String ENDPOINT_SEARCH_AREA = "area/{query}";
    public static final String ENDPOINT_CHECK_TARIFF = "tariff2/{origin}/{dest}/{weight}";
    public static final String ENDPOINT_FETCH_NEARBY_BRANCHES = "location/{lat}/{lon}";

    public static final String ENDPOINT_FETCH_HOME_CONTENT = "home/";
    public static final String ENDPOINT_FETCH_SINGLE_PRODUCT_DETAIL = "product/{pk}/";
    public static final String ENDPOINT_FETCH_SHOPPING_CART = "cart/";
    public static final String ENDPOINT_FETCH_SHOPPING_ORDER = "ordered_product/";

    //final String rateUrl = Config.endpoint(String.format("mobile_api/tariff2/%s/%s/%s",
    //origin, dest, weight));
}
