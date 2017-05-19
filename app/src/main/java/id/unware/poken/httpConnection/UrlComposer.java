package id.unware.poken.httpConnection;

import id.unware.poken.BuildConfig;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;

/**
 * Created by marzellamega on 4/26/16.
 */
public class UrlComposer {

    private static UrlComposer instance;

    private StringBuilder sb = new StringBuilder();

    public static UrlComposer getInstance(){
        if(instance == null){
            instance = new UrlComposer();
        }
        return instance;
    }
    private void clearBuilder() {
        sb.setLength(0);
    }

    private void resetBuilder() {
        clearBuilder();
        String baseUrl = BuildConfig.HOST;
        sb.append(baseUrl);
    }

    /**
     * Generate URL to checkNewPackage both with get package data.
     * @return : URL String
     */
    public String composeLogin() {
        resetBuilder();
        sb.append("get_data");
        return sb.toString();
    }

    public String composeGetBookingHistory(String strBookingId, String strBookingHistoryId) {
        resetBuilder();
        sb.append("get_booking_history/").append(strBookingId).append("/").append(strBookingHistoryId);
        return sb.toString();
    }

    public String composeGetCourierLocation() {
        resetBuilder();
        sb.append("get_vendor_location");
        return sb.toString();
    }

    public String composeForgetPassword() {
        resetBuilder();
        sb.append("forgetPass");
        return sb.toString();
    }

    public String composeRegister() {
        resetBuilder();
        sb.append("register");
        return sb.toString();
    }

    public String composeNewpackage() {
        resetBuilder();
        sb.append("create_booking");
        return sb.toString();
    }

    public String composeDeletePackage() {
        resetBuilder();
        sb.append("delete_booking");
        return sb.toString();
    }

    /**
     * Tariff check.
     *
     * @return Formatted API URL complete with HOST.
     *
     * @since Nov 27 2016 - Update API node version to {@code tariff3}, with entirely new response.
     */
    public String composeCheckTariff() {
        resetBuilder();
        sb.append("tariff3");  // Changed from tariff
        return sb.toString();
    }

    /**
     * Create estimate tariff URL when user successfully created a new package.
     *
     * @param bookingId Booking id to check it's estimate tariff.
     * @return Formatted estimated tariff URL.
     */
    public String composeCheckEstimateTariff(String bookingId) {
        resetBuilder();
        sb.append("get_estimated_tariff/").append(bookingId);
        return sb.toString();
    }

    /**
     * Creating a URL for append selected tariff to booking's note.
     *
     * @param bookingId Booking ID
     * @return Formatted URL to select service on note.
     */
    public String composeSetServiceTariff(String bookingId) {
        resetBuilder();
        sb.append("set_service_tariff/").append(bookingId);
        return sb.toString();
    }

    /**
     * Compose URL to check postal code based on keyword whether place name
     * or ZIP code itself.
     *
     * @param strKeyword Keyword to search what postal code is
     * @return Formatted URL.
     *
     * @since Dec 16 2016 - V46 : Encode keyword.
     */
    public String composeCheckPostcode(String strKeyword) {
        resetBuilder();
        sb.append("zip/").append(StringUtils.urlEncode(strKeyword.trim())).append("/1/10");

        // zip/string/max_level/show

        Utils.Log("UrlComposer", "Search Postcode URL: " + sb.toString());

        return sb.toString();
    }

    /**
     * Compose paged request for packages.
     *
     * @param strMinBookingId Minimum booking id available
     * @param strLimit Request limit (default 50)
     *
     * @return Formatted URL for Paged Packages.
     */
    public String composeGetPagedPackages(String strMinBookingId, String strLimit) {
        resetBuilder();
        sb.append("get_page/").append(strMinBookingId).append("/").append(strLimit);
        return sb.toString();
    }

    /**
     * Create search package URL.
     *
     * @param strKeyword        Keyword to search (encoding applied on this method)
     * @param strMinBookingId   Minimum id available offline
     * @param strMaxBookingId   Maximum id available offline
     * @param strLimit          String  rquest limit
     *
     * @return Formatted URL
     *
     * @since Dec 16 2016 - V46 : Encode keyword.
     */
    public String composeSearchPackages(String strKeyword, String strMinBookingId, String strMaxBookingId, String strLimit) {
        resetBuilder();
        sb.append("search_booking")
                .append("/").append(StringUtils.urlEncode(strKeyword.trim()))
                .append("/").append(strMinBookingId)
                .append("/").append(strMaxBookingId)
                .append("/").append(strLimit);
        return sb.toString();
    }

    /**
     * All Area API URL composer
     *
     * @return Formatted API URL
     * @since Nov 28 2016 - NEW!
     */
    public String composeGetAreaData(String strKeyword, String limit) {
        resetBuilder();

        if (Utils.isEmpty(strKeyword)) {
            sb.append("zip/0/0/0");
        } else {
            sb.append("zip/").append(StringUtils.urlEncode(strKeyword)).append("/1/").append(limit);
        }

        Utils.Log("UrlComposer", "Get area data URL: " + sb.toString());

        return sb.toString();
    }

    public String composePickupCheck() {
        //params : lat, lon, name, telephone, address, zip_code, vehicle_type : 1-> motor | 2-> mobil
        //params : pickup_id, response : 1->ok | 2-> no
        resetBuilder();
        sb.append("pickup/check");
        return sb.toString();
    }

    public String composePickupBook() {
        resetBuilder();
        sb.append("pickup/book");
        return sb.toString();
    }

    /** Compose URL for Pickup cancel */
    public String composePickupCancel() {
        resetBuilder();
        sb.append("pickup/cancel");
        return sb.toString();
    }

    public static String getImageUrl(String reference_code) {
        return "http://paket.id/etc/barcode/" + reference_code + "20";
    }

    public String composeShareUrl(String bookingCode) {
        sb.setLength(0);
        sb.append("https://paket.id/is/").append(bookingCode);
        return sb.toString();
    }

    public String composeLoginWithGoogle(String token) {
        resetBuilder();
        sb.append("google_check/");
        sb.append(token);
        return sb.toString();
    }
    public String composeRequestOtp() {
        resetBuilder();
        sb.append("settings/send_otp");
        return sb.toString();
    }

    public String composeVerifyOtp() {
        resetBuilder();
        sb.append("settings/verify_otp");
        return sb.toString();
    }

    public String composeSaveProfile() {
        resetBuilder();
        sb.append("settings/save_settings");
        return sb.toString();
    }

    public String composeLoadWalletData() {
        resetBuilder();
        sb.append("wallet_info");
        return sb.toString();
    }

    public String composeWalletTopup() {
        resetBuilder();
        sb.append("wallet_add_deposit");
        return sb.toString();
    }

    public String composeCancelTopup() {
        resetBuilder();
        sb.append("wallet_cancel_deposit");
        return sb.toString();
    }

    public String composeCreateBankUser() {
        resetBuilder();
        sb.append("wallet_add_user_bank");
        return sb.toString();
    }

    public String composeRequestWithdrawal() {
        resetBuilder();
        sb.append("wallet_add_withdraw");
        return sb.toString();
    }

    public String composeCancelWithdraw() {
        resetBuilder();
        sb.append("wallet_cancel_withdraw");
        return sb.toString();
    }

}