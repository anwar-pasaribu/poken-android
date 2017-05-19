package id.unware.poken.httpConnection;

/**
 * Created by marzellaalfamega on 3/20/17.
 *
 */

class ConstantsRetrofit {
    public static final String ENDPOINT_TRACKING = "track3/{resi}";
    public static final String ENDPOINT_SEARCH_AREA = "area/{query}";
    public static final String ENDPOINT_CHECK_TARIFF = "tariff2/{origin}/{dest}/{weight}";
    public static final String ENDPOINT_FETCH_NEARBY_BRANCHES = "location/{lat}/{lon}";

    //final String rateUrl = Config.endpoint(String.format("mobile_api/tariff2/%s/%s/%s",
    //origin, dest, weight));
}
