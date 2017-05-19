package id.unware.poken.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.unware.poken.BuildConfig;
import id.unware.poken.interfaces.OnDatabaseStateListener;
import id.unware.poken.pojo.BookingStatus;
import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoArea;
import id.unware.poken.pojo.PojoAreaPaket;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoCourierLocation;
import id.unware.poken.pojo.PojoHistory;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoTracking;
import id.unware.poken.pojo.PojoTrackingData;
import id.unware.poken.pojo.PojoTrackingStatus;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.RecentSearchKeyword;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by marzellamega on 3/18/16.
 * Controll Realm database on app.
 */
public class ControllerRealm implements RealmChangeListener<RealmResults<PojoBooking>> {

    private static ControllerRealm instance;

//    private OnRealmListener<RealmResults<PojoBooking>> realmResultsPojoBookingListener;
    private final String TAG = "ControllerRealm";
    private final int MAX_RECENT_KEYWORD_ITEM = 20;

    private final int MAX_SEARCH_RESULT = 32;

    private OnDatabaseStateListener<RealmResults<PojoBooking>> realmResultsPojoBookingListener;
    public static ControllerRealm getInstance() {
        if (instance == null) {
            instance = new ControllerRealm();
        }
        return instance;
    }

    public void addAllTrackingData(final PojoTrackingData pojoTrackingData) {

        Utils.Log(TAG, "Begin add tracking data from net.");

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoTracking pojoTracking : pojoTrackingData.getTrackings()) {
                    Utils.Log(TAG, "Add tracking: " + pojoTracking.getCnno());
                    PojoTracking newPojoTracking = realm.copyToRealmOrUpdate(pojoTracking);

                    for (PojoTrackingStatus pojoTrackingStatus : pojoTracking.getStatuses()) {
                        Utils.Log(TAG, "Add tracking status: noted: " + pojoTrackingStatus.getNoted());
                        newPojoTracking.getStatusList().add(pojoTrackingStatus);
                    }

                }
            }
        });
    }

    public void addAllPackage(final List<PojoBooking> listBooking) {

        Utils.Logs('i', TAG, "Insert " + listBooking.size() + " PojoBooking to Realm");

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(listBooking);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public List<PojoBooking> getFilteredPackages(
            OnDatabaseStateListener<RealmResults<PojoBooking>> dbListener) {

        Realm realm = Realm.getDefaultInstance();

        realmResultsPojoBookingListener = dbListener;

        // Send database query started status to caller
        String startIndicator = "";
        if (BuildConfig.DEV_MODE) {
            startIndicator = "Database query start on " + Calendar.getInstance().getTime().toString();
        }
        realmResultsPojoBookingListener.onDatabaseProcessStarted(startIndicator);

        Utils.Logs('d', TAG, "Begin to try query");

        RealmResults<PojoBooking> realmResultsPojoBooking = realm
                .where(PojoBooking.class)
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal())
                .findAllSorted(
                        new String[]{PojoBooking.KEY_BOOKING_STATUS_TEXT, PojoBooking.KEY_BOOKING_ID, PojoBooking.KEY_BOOKING_DATE},
                        new Sort[]{Sort.ASCENDING, Sort.DESCENDING, Sort.DESCENDING}
                );

        // Result is always empty
        if (!realmResultsPojoBooking.isEmpty()) {

            // Set last sender
            // Set Last sender data for New Package Sender field purpose
            Utils.setLastSenderData(realmResultsPojoBooking.get(0));

            printAllPojoBookingDataInfo(realmResultsPojoBooking);

            if (realmResultsPojoBookingListener != null) {
                realmResultsPojoBookingListener.onDatabaseProcessFinished(realmResultsPojoBooking);
            }

        } else {
            Utils.Logs('e', TAG, "Showable List PojoBooking is empty");

            if (realmResultsPojoBookingListener != null) {
                realmResultsPojoBookingListener.onDatabaseProcessFailed("Showable List PojoBooking is empty");
            }
        }

        return realmResultsPojoBooking;
    }

    /**
     * Search {@link PojoBooking} based on: <br />
     * - {@code strKeyword}  : Search on field {@link PojoBooking#booking_code} or {@link PojoBooking#to_name} <br />
     * - {@code bookingId} : Search directly based on {@link PojoBooking#booking_id} <br />
     * - {@code bookingStatusNumber} : Search only for certain {@link PojoBooking#status}
     *
     * @param strKeyword          String keyword to search
     * @param bookingId           Long bookingId
     * @param bookingStatusNumber Int status number
     * @return List of {@link PojoBooking} with limit MAX_SEARCH_RESULT
     */
    public List<PojoBooking> searchPackage(String strKeyword, long bookingId, int bookingStatusNumber) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PojoBooking> pojoBookingRealmResults = realm
                .where(PojoBooking.class)
                .contains("booking_code", strKeyword, Case.INSENSITIVE)
                .or()
                .contains("to_name", strKeyword, Case.INSENSITIVE)
                .findAll();

        int resultSize = pojoBookingRealmResults.size();
        int searchLimit = resultSize < MAX_SEARCH_RESULT ? resultSize : MAX_SEARCH_RESULT;
        Utils.Log(TAG, "Found data booking: " + resultSize);

        return pojoBookingRealmResults.subList(0, searchLimit);
    }

    /**
     * Logging all PojoBooking data size for each status.
     *
     * @param realmResultsPojoBooking : Realm data results of PojoBooking.
     * @since Nov 11 2016 - Reformat report and  add Fabric logging report.
     */
    private void printAllPojoBookingDataInfo(RealmResults<PojoBooking> realmResultsPojoBooking) {
        Locale l = ControllerDate.defaultLocale;
        final String strPackageReport = String.format(l, "total=%d, -> booked=%d, picked=%d, sent=%d, delivered=%d, returned=%d",
                realmResultsPojoBooking.size(),
                realmResultsPojoBooking.where().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal()).findAll().size(),
                realmResultsPojoBooking.where().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal()).findAll().size(),
                realmResultsPojoBooking.where().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal()).findAll().size(),
                realmResultsPojoBooking.where().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal()).findAll().size(),
                realmResultsPojoBooking.where().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal()).findAll().size()
        );

        // Fabric report
        MyLog.FabricLog(Log.INFO, TAG + " - Package size info: " + strPackageReport);

    }

    /**
     * Insert {@link PojoArea} to Realm
     *
     * @param pojoAreas Array of {@link PojoArea}
     * @since Nov 28 2016 - NEW!
     */
//    public void addAllArea(final PojoArea[] pojoAreas) {
//
//        Utils.Logs('i', TAG, "Begin saving area data");
//
//        Realm realm = null;
//        try {
//            realm = Realm.getDefaultInstance();
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    int areaDataSize = realm.copyToRealmOrUpdate(Arrays.asList(pojoAreas)).size();
//
//                    Utils.Log(TAG, "New area size: " + areaDataSize);
//                }
//            });
//        } finally {
//            if (realm != null) {
//                realm.close();
//            }
//        }
//    }

    public void addAllVendorLocation(final PojoCourierLocation[] pojoCourierLocation) {

        Utils.Logs('i', TAG, "Begin saving vendor location");

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    int areaDataSize = realm.copyToRealmOrUpdate(Arrays.asList(pojoCourierLocation)).size();

                    Utils.Log(TAG, "New area size: " + areaDataSize);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Insert an array of {@link PojoHistory} to Realm.
     *
     * @param bookingHistories Array of PojoHistory
     * @since Nov 30 2016 - Implement try-catch block and one dimension array due API changed.
     */
    public void addAllHistory(final PojoHistory[] bookingHistories) {

        Utils.Log(TAG, "Try add array of booking history with length: " + bookingHistories.length);

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(Arrays.asList(bookingHistories));

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @SuppressWarnings("unused")
    public void addAllLocation(PojoCourierLocation[] location, Realm outsideRealm) {
        Realm realm;
        if (outsideRealm == null) {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
        } else {
            realm = outsideRealm;
        }

        for (PojoCourierLocation pojoCourierLocation : location) {
            realm.copyToRealmOrUpdate(pojoCourierLocation);
        }
        if (outsideRealm == null) {
            realm.commitTransaction();
        }
    }

    @SuppressWarnings("unused")
    public void addAllPickupHistory(PojoPickupHistory[] pickup, Realm outsideRealm) {
        Realm realm;
        if (outsideRealm == null) {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
        } else {
            realm = outsideRealm;
        }

        for (PojoPickupHistory pojoPickupHistory : pickup) {
            realm.copyToRealmOrUpdate(pojoPickupHistory);
        }

        if (outsideRealm == null) {
            realm.commitTransaction();
        }
    }

    /**
     * Add recent search keyword for certain Page (ex. Postcode)
     *
     * @param strKeyword String keyword to save
     * @param intPageTag Page tag (where keyword has been used)
     * @since Nov 22 2016 - NEW!
     */
    public void addRecentKeyword(final String strKeyword, final int intPageTag) {

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    // Remove last one (oldest) recent item when
                    // Recent item size more than MAX_RECENT_KEYWORD_ITEM
                    RealmResults<RecentSearchKeyword> recentSearchKeywords = realm
                            .where(RecentSearchKeyword.class)
                            .equalTo(RecentSearchKeyword.KEY_TAG, intPageTag)
                            .findAllSorted(RecentSearchKeyword.KEY_TIMESTAMP, Sort.DESCENDING);
                    if (recentSearchKeywords.size() >= MAX_RECENT_KEYWORD_ITEM) {
                        recentSearchKeywords.deleteLastFromRealm();
                    }

                    // Check if keyword already available
                    // to prevent adding the same keyword to current page tag.
                    // Then update data timeStamp to make it on top, finally abort process.
                    RecentSearchKeyword recentData = realm.where(RecentSearchKeyword.class)
                            .equalTo(RecentSearchKeyword.KEY_STRING_KEYWORD, strKeyword)
                            .equalTo(RecentSearchKeyword.KEY_TAG, intPageTag).findFirst();
                    if (recentData != null) {
                        Utils.Log(TAG, "Update time to already available recentKeyword: \"" + strKeyword + "\"");
                        recentData.setTimeStamp(Calendar.getInstance().getTime());
                        realm.copyToRealmOrUpdate(recentData);
                        return;
                    }

                    // Generate autoincrement id by get max id on Realm then add by one.
                    Number maxId = realm.where(RecentSearchKeyword.class).max(RecentSearchKeyword.KEY_ID);
                    long nextId = maxId == null ? 1 : maxId.longValue() + 1;

                    // Add new recent item to Realm
                    RecentSearchKeyword newRecentSearchKeyword = new RecentSearchKeyword();
                    newRecentSearchKeyword.setId(nextId);
                    newRecentSearchKeyword.setKeywordString(strKeyword);
                    newRecentSearchKeyword.setTimeStamp(Calendar.getInstance().getTime());
                    newRecentSearchKeyword.setTag(intPageTag);

                    realm.copyToRealm(newRecentSearchKeyword);

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public long getAllBookingCount() {
        Realm realm = Realm.getDefaultInstance();
        long allBookingCount = realm
                .where(PojoBooking.class)
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal())
                .count();
        realm.close();

        MyLog.FabricLog(Log.INFO, "All booking available offline: " + allBookingCount);

        return allBookingCount;
    }

    /**
     * Get all {@link PojoHistory} based on {@link PojoBooking#booking_id}.
     * The list is sorting {@link PojoHistory#datetime}, last date on top.
     *
     * @param booking_id String {@link PojoBooking#booking_id}
     * @return List of {@link PojoBooking}
     */
    public List<PojoHistory> getAllHistory(String booking_id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PojoHistory> bookingHistoryList = realm
                .where(PojoHistory.class)
                .equalTo(PojoBooking.KEY_BOOKING_ID, booking_id)
                .findAllSorted("datetime", Sort.DESCENDING);

        Utils.Log(TAG, "Booking history size: " + bookingHistoryList.size() + " for booking_id: " + booking_id);
        return bookingHistoryList;
    }

    /**
     * Get all {@link PojoCourier} data without any filter.
     *
     * @return List of {@link PojoCourier}
     * @since Nov 03 2016 - NEW!
     */
    public List<PojoCourier> getAllCourier() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(PojoCourier.class).findAll();
    }

    public List<PojoBooking> getAllContact() {

        Realm realm = Realm.getDefaultInstance();

        List<PojoBooking> results = realm.where(PojoBooking.class)
                .findAllSorted("to_name", Sort.ASCENDING)
                .distinct("to_name");
        List<PojoBooking> listBooking = new ArrayList<>();

        HashMap<String, PojoBooking> map = new HashMap<>();
        /*
         Filtering by name and phone
         */
        for (PojoBooking pojoBooking : results) {
            map.put(pojoBooking.getTo_name().trim() + pojoBooking.getTo_phone().trim(), pojoBooking);
        }

        Iterator it = map.entrySet().iterator();
        //noinspection WhileLoopReplaceableByForEach
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            listBooking.add((PojoBooking) pair.getValue());
        }

        Utils.Log(TAG, "Generated contact for Address Book size: " + listBooking.size());

        // Sorting by to_name (Implement comparable)
        Collections.sort(listBooking);

        return listBooking;
    }

    /**
     * Get all Sender dan Receiver data from PojoBooking. One PojoBooking contains two Addreses,
     * Sender and Receiver respectively. The datas then saved in PojoAddressBook object.
     *
     * @return List of PojoAddressBook
     * @see PojoAddressBook
     * @see PojoBooking
     * @since (Sep 19th 2016) - Version 41
     */
    public List<PojoAddressBook> getAllAddressBook() {

        Realm realm = Realm.getDefaultInstance();

        List<PojoBooking> results = realm.where(PojoBooking.class)
                .findAllSorted("to_name", Sort.ASCENDING)
                .distinct("to_name");

        List<PojoAddressBook> pojoAddressBookList = new ArrayList<>();
        for (PojoBooking bookingItem : results) {
            pojoAddressBookList.add(new PojoAddressBook(
                    bookingItem.getFrom_name(),
                    bookingItem.getFrom_phone(),
                    bookingItem.getFrom_address(),
                    PojoAddressBook.STATE_ADDRESS_NORMAL,
                    true));  // True means Sender data

            pojoAddressBookList.add(new PojoAddressBook(
                    bookingItem.getTo_name(),
                    bookingItem.getTo_phone(),
                    bookingItem.getTo_address(),
                    PojoAddressBook.STATE_ADDRESS_NORMAL,
                    false));  // False means Receiver data
        }

        Utils.Log(TAG, "Pojo address book size: " + pojoAddressBookList.size());

        List<PojoAddressBook> listFilteredAddressBook = new ArrayList<>();
        HashMap<String, PojoAddressBook> map = new HashMap<>();

        /* Filtering by name and phone. */
        for (PojoAddressBook pojoAddressBook : pojoAddressBookList) {
            map.put(pojoAddressBook.getName().trim() + pojoAddressBook.getPhoneNumber().trim(), pojoAddressBook);
        }

        Iterator it = map.entrySet().iterator();
        // noinspection WhileLoopReplaceableByForEach
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            listFilteredAddressBook.add((PojoAddressBook) pair.getValue());
        }

        Utils.Log(TAG, "Generated contact for Address Book size: " + listFilteredAddressBook.size());

        // Sorting by to_name (Implement comparable)
        Collections.sort(listFilteredAddressBook);

        if (BuildConfig.DEBUG) {
            for (PojoAddressBook pojoAddressBook : listFilteredAddressBook) {
                Utils.Log(TAG, String.format("%s %s %s",
                        pojoAddressBook.getName(),
                        pojoAddressBook.getPhoneNumber(),
                        pojoAddressBook.getAddress()));
            }
        }

        return listFilteredAddressBook;
    }

    /**
     * Get all related keyword to page tag.
     *
     * @param intTag Paget tag (ex. Postcode, Package, etc.)
     * @return List of {@link RecentSearchKeyword} object.
     */
    public RealmResults<RecentSearchKeyword> getAllRecentKeywordByTag(int intTag) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<RecentSearchKeyword> recentList = realm
                .where(RecentSearchKeyword.class)
                .equalTo(RecentSearchKeyword.KEY_TAG, intTag)
                .findAllSorted(RecentSearchKeyword.KEY_TIMESTAMP, Sort.DESCENDING);

        Utils.Log(TAG, "Recent keyword list size: " + recentList.size());

        return recentList;
    }

    /**
     * Get {@link PojoArea} from Realm
     *
     * @return List of {@link PojoArea}
     * @since Nov 28 2016 - NEW
     */
    public RealmResults<PojoAreaPaket> getAllArea() {

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<PojoAreaPaket> dataList = realm
                    .where(PojoAreaPaket.class)
                    .findAllSorted(PojoAreaPaket.KEY_AREA, Sort.ASCENDING);

            Utils.Log(TAG, "Area list size: " + dataList.size());

            return dataList;

        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * Get PojoBooking data from Realm based on booking_id
     *
     * @param bookingId {@link PojoBooking#booking_id}
     * @return PojoBooking : Selected PojoBooking object based on booking_id or NULL when not found.
     */
    public PojoBooking getBookingById(long bookingId) {

        Realm realm = Realm.getDefaultInstance();

        return realm.where(PojoBooking.class).equalTo(PojoBooking.KEY_BOOKING_ID, bookingId).findFirst();
    }

    public Map<String, Long> getMinAndMaxBookingId() {

        Realm realm = Realm.getDefaultInstance();

        Long longMax = realm.where(PojoBooking.class).max(PojoBooking.KEY_BOOKING_ID) != null
                ? realm.where(PojoBooking.class).max(PojoBooking.KEY_BOOKING_ID).longValue()
                : 0L;
        Long longMin = realm.where(PojoBooking.class).min(PojoBooking.KEY_BOOKING_ID) != null
                ? realm.where(PojoBooking.class).min(PojoBooking.KEY_BOOKING_ID).longValue()
                : 0L;

        Map<String, Long> minMaxBookingId = new HashMap<>();
        minMaxBookingId.put("MAX", longMax);
        minMaxBookingId.put("MIN", longMin);

        realm.close();

        return minMaxBookingId;
    }

    /**
     * Get Booked item count and set latest booking as first sender data.
     *
     * @return Long - number of Booked item.
     */
    public long getBookedPackageAvailability() {
        Realm realm = Realm.getDefaultInstance();
        long count = realm
                .where(PojoBooking.class)
                .equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                .count();

        RealmResults<PojoBooking> bookings = realm
                .where(PojoBooking.class)
                .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING);

        if (bookings.size() != 0) {
            PojoBooking latestBooking = bookings.first();
            // Set last sender
            // Set Last sender data for New Package Sender field purpose
            Utils.setLastSenderData(latestBooking);
        }

        Utils.Logs('i', TAG, "Booked package size: " + count);

        realm.close();

        return count;
    }

    /**
     * Get PojoBooking by list of booking_id.
     *
     * @param bookingIds ArrayList of booking_id String.
     * @return List of PojoBooking.
     */
    public List<PojoBooking> getBookingByIds(ArrayList<String> bookingIds) {

        List<PojoBooking> pojoBookingList;

        Realm myRealm = Realm.getDefaultInstance();
        // Begin Realm query to get the data
        RealmQuery<PojoBooking> pojoBookingRealmQuery = myRealm.where(PojoBooking.class);
        int i = 0;
        long longBookingId = 0L;
        for (String id : bookingIds) {
            longBookingId = Long.parseLong(id);
            if (i != 0) {
                pojoBookingRealmQuery = pojoBookingRealmQuery.or();
            }
            pojoBookingRealmQuery = pojoBookingRealmQuery.equalTo(PojoBooking.KEY_BOOKING_ID, longBookingId);
            i++;
        }

        pojoBookingList = pojoBookingRealmQuery.findAllSorted(PojoBooking.KEY_BOOKING_DATE, Sort.DESCENDING);

        MyLog.FabricLog(Log.INFO, TAG + " - Realm result based on booking ID size: " + pojoBookingList.size());

        myRealm.close();

        return pojoBookingList;
    }

    public List<PojoBooking> getBookingByStatus(ArrayList<String> bookingStatusText) {
        Realm myRealm = Realm.getDefaultInstance();

        List<PojoBooking> pojoBookingList = new ArrayList<>();
        RealmResults<PojoBooking> pojoBookingRealmResults;

        if (bookingStatusText.size() != 0) {

            // Begin Realm query to get the data
            RealmQuery<PojoBooking> pojoBookingRealmQuery = myRealm.where(PojoBooking.class);

            int i = 0;
            for (String statusText : bookingStatusText) {

                if (i != 0) {
                    pojoBookingRealmQuery = pojoBookingRealmQuery.or();
                }

                pojoBookingRealmQuery = pojoBookingRealmQuery
                        .equalTo(PojoBooking.KEY_BOOKING_STATUS_TEXT, statusText);
                i++;
            }

            pojoBookingRealmResults = pojoBookingRealmQuery.findAllSorted(
                    new String[]{ /*PojoBooking.KEY_BOOKING_STATUS_TEXT, PojoBooking.KEY_BOOKING_ID, PojoBooking.KEY_BOOKING_DATE*/ PojoBooking.KEY_BOOKING_ID},
                    new Sort[]{ /*Sort.ASCENDING, Sort.DESCENDING, Sort.DESCENDING*/Sort.DESCENDING}
            );


        } else {
//
//            int allBookingCount = (int) getAllBookingCount();
//            MyLog.FabricLog(Log.INFO, TAG + " - All booking count: " + allBookingCount + ", time to delete old one.");
//            if (allBookingCount > AppClass.MAX_PACKAGES_ON_DB) {
//
//                removeOverLimitPackages();
//            }

            // Query again after package is deleted
            pojoBookingRealmResults = myRealm
                    .where(PojoBooking.class)
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal())
                    .findAllSorted(
                            new String[]{ /*PojoBooking.KEY_BOOKING_STATUS_TEXT, PojoBooking.KEY_BOOKING_ID, PojoBooking.KEY_BOOKING_DATE*/ PojoBooking.KEY_BOOKING_ID},
                            new Sort[]{ /*Sort.ASCENDING, Sort.DESCENDING, Sort.DESCENDING*/Sort.DESCENDING}
                    );
        }

        int resultSize = pojoBookingRealmResults.size();
        int packageLimit = resultSize > Constants.MAX_PACKAGES_ON_DB
                ? Constants.MAX_PACKAGES_ON_DB
                : resultSize;

        for (PojoBooking pojoBooking : pojoBookingRealmResults.subList(0, packageLimit)) {
            Utils.Log(TAG, "[ready] Booking ID: " + pojoBooking.getBooking_id() + ", status: " + pojoBooking.getBooking_status_text());
            pojoBookingList.add(pojoBooking);
        }

        Utils.Log(TAG, "Realm result by status size: " + resultSize);

        return pojoBookingList;
    }

    public PojoPickupHistory getPickupHistoryById(String pickup_id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(PojoPickupHistory.class).equalTo("pickup_id", pickup_id).findFirst();
    }

    public void saveUserBankAccount(ArrayList<PojoUserBank> userBank) {
        Realm myRealm = Realm.getDefaultInstance();
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(userBank);
        myRealm.commitTransaction();
    }

    public PojoCourier getCourierById(long longVendorId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(PojoCourier.class).equalTo(PojoCourier.KEY_COURIER_ID, longVendorId).findFirst();
    }

    /**
     * Delete data from Realm. The data based on booking_id.
     *
     * @param bookingId : {@link PojoBooking#booking_id}.
     */
    public void removePackageWithId(final long bookingId) {

        Utils.Logs('w', TAG, "Delete from Realm package with booking_id : " + bookingId);

        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(PojoBooking.class)
                            .equalTo(PojoBooking.KEY_BOOKING_ID, bookingId)
                            .findFirst()
                            .deleteFromRealm();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

    }

    /**
     * !!! Delete all data from {@link Realm}
     *
     * @since Nov 21 2016 - Implement {@code deleteAll()} instead of delete each one by one.
     */
    public void removeAllData() {
        Utils.Logs('w', TAG, "Remove all data");
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.deleteAll();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public void removeOverLimitPackages() {
        MyLog.FabricLog(Log.WARN, TAG + " - BEGIN TO DELETE OVER LIMIT PACKAGES.");

        Realm myRealm = Realm.getDefaultInstance();

        int allBookingCount = (int) myRealm
                .where(PojoBooking.class).count();

        MyLog.FabricLog(Log.INFO, TAG + " - All booking count: " + allBookingCount + ", time to delete old one.");

        if (allBookingCount == 0) return;

        // !!! Delete package data when it's more than MAX_DB_SIZE
        int packageLimit = allBookingCount > Constants.MAX_PACKAGES_ON_DB
                ? Constants.MAX_PACKAGES_ON_DB
                : allBookingCount;

        // Get last ID on "allowed" package, item greater then it will be deleted
        List<PojoBooking> tempPojoBookingList = myRealm
                .where(PojoBooking.class)
                .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING)
                .subList(0, packageLimit);

        final long lastBookingId = tempPojoBookingList.get(tempPojoBookingList.size() - 1).getBooking_id();

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<PojoBooking> bookingRealmResults = realm
                        .where(PojoBooking.class)
                        .lessThan(PojoBooking.KEY_BOOKING_ID, lastBookingId)
                        .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING);

                boolean isDeleted = bookingRealmResults.deleteAllFromRealm();

                MyLog.FabricLog(Log.WARN, TAG + " - PACKAGE DELETED --> " + isDeleted + ",  last booking_id: " + lastBookingId);
            }
        });
    }

    /**
     * Remove all old booking data with.
     */
    public void removeOldBooking() {
        Utils.Logs('w', TAG, "Try remove old booking data");
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final RealmResults<PojoBooking> allBooking = realm.where(PojoBooking.class).findAll();

                    for (int i = 0; i < allBooking.size(); i++) {
                        if (ControllerDate.getInstance().moreThanToday(allBooking.get(i).getBooking_date())) {
                            allBooking.get(i).deleteFromRealm();
                        }
                    }

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * [WARNING] Remove all recent keyword for target Page tag
     *
     * @param intPagetTag Page tag to remove.
     * @since Nov 22 2016 - NEW!
     */
    public void removeRecentKeywordByTag(final int intPagetTag) {
        Utils.Logs('w', TAG, "Try remove recent keyword by tag: " + intPagetTag);
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(RecentSearchKeyword.class)
                            .equalTo(RecentSearchKeyword.KEY_TAG, intPagetTag)
                            .findAll().deleteAllFromRealm();

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public void onChange(RealmResults<PojoBooking> pojoBookingRealmResults) {
        if (BuildConfig.DEV_MODE) {
            String s = "Database query finished on " + Calendar.getInstance().getTime().toString();
            Utils.Log(TAG, s);
            Utils.Log(TAG, "Realm on change. size: " + pojoBookingRealmResults.size());
        }
        Utils.Log(TAG, "Realm on change. First PojoBooking code: " + pojoBookingRealmResults.get(0).getBooking_code());

        // Set Last sender data for New Package Sender field purpose
        Utils.setLastSenderData(pojoBookingRealmResults.get(0));

        printAllPojoBookingDataInfo(pojoBookingRealmResults);

        if (this.realmResultsPojoBookingListener != null) {
            this.realmResultsPojoBookingListener.onDatabaseProcessFinished(pojoBookingRealmResults);
        }

    }

    public ArrayList<PojoUserBank> getUserBankAccount() {
        Realm myRealm = Realm.getDefaultInstance();
        ArrayList<PojoUserBank> listUserBank = new ArrayList<>();
        listUserBank.addAll(myRealm.where(PojoUserBank.class).findAll());
        return listUserBank;
    }

    public PojoUserBank getUserBankAccount(String user_bank_id) {
        Realm myRealm = Realm.getDefaultInstance();
        return myRealm.where(PojoUserBank.class).equalTo("userBankId", user_bank_id).findFirst();
    }
}
