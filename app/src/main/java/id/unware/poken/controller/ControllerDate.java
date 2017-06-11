package id.unware.poken.controller;

import android.content.Context;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import id.unware.poken.R;
import id.unware.poken.tools.Utils;

/**
 * Created by IT11 on 5/27/2015.
 * Date Controller
 */
public class ControllerDate {

    private static ControllerDate instance;
    private final String serverResponseFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private Locale defLocale = Locale.getDefault();
    private SimpleDateFormat defaultDateFormat = new SimpleDateFormat(serverResponseFormat, defLocale);
    private SimpleDateFormat formatPickupHistory = new SimpleDateFormat("dd MMM yyyy", defLocale);
    private SimpleDateFormat formatPickupRange = new SimpleDateFormat("hh a", defLocale);
    private SimpleDateFormat formatTrackingDate = new SimpleDateFormat("dd MMM, HH:mm", defLocale);
    private SimpleDateFormat formatBookingHistoryDate = new SimpleDateFormat("dd MMM, yyyy", defLocale);
    private SimpleDateFormat formatBookingHistoryTime = new SimpleDateFormat("HH:mm a", defLocale);
    private SimpleDateFormat formatTransactionHistory = new SimpleDateFormat("dd MMM yyyy, HH:mm", defLocale);
    private SimpleDateFormat formatBookingDate = new SimpleDateFormat("yyyy-MM-dd", defaultLocale);

    private TimeZone tzDefault = TimeZone.getDefault();
    public static final Locale defaultLocale = Locale.getDefault();

    public static ControllerDate getInstance() {
        if (instance == null) {
            instance = new ControllerDate();
        }
        return instance;
    }

    public String toTransactionHistory(String time) {
        try {
            Date date = defaultDateFormat.parse(time);
            return formatTransactionHistory.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "-";
    }

    /**
     * Get Locale. When active locale is not Indonesia the set locale to English
     *
     * @return Return English locale when active Locale is not English (US)
     */
    public Locale getDefLocale() {
        if (!defLocale.getCountry().equals("ID")) {
            return Locale.ENGLISH;
        } else {
            return defLocale;
        }
    }

    public String toPickupHistory(String time) {
        try {
            Date date = defaultDateFormat.parse(time);
            return formatPickupHistory.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public CharSequence toTrackingPageDateFormat(String strTime) {
        try {
            Date date = defaultDateFormat.parse(strTime);
            return formatTrackingDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public StringBuilder toTimeRange(String time_range_from, String time_range_to) {
        StringBuilder result = new StringBuilder();
        try {
            Date date = defaultDateFormat.parse(time_range_from);
            result.append(formatPickupRange.format(date));
            date = defaultDateFormat.parse(time_range_to);
            result.append(" - ");
            result.append(formatPickupRange.format(date));
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toBookingHistoryDate(String time){
        try {
            Date date = defaultDateFormat.parse(time);
            return formatBookingHistoryDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String toBookingHistoryTime(String time){
        try {
            Date date = defaultDateFormat.parse(time);
            return formatBookingHistoryTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public CharSequence getShortDateWithHourFormat(Date date) {
        return formatTrackingDate.format(date);
    }

    public static CharSequence getFormattedDate(String strDate, Context context) {
        try {
            // Server responds
            SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = mysqlDateFormat.parse(strDate);

            // Device specific date format
            Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
            //noinspection unused
            String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Field and style for month name
            int calendarStyle = Calendar.SHORT;
            Locale calendarLocale = Locale.getDefault();

            // Format:
            // EN: Sun, Jul 27
            // ID: Min, 27 Jul (Not implemented)
            return context.getString(R.string.lbl_date_format,
                    calendar.getDisplayName(Calendar.DAY_OF_WEEK, calendarStyle, calendarLocale),
                    String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                    calendar.getDisplayName(Calendar.MONTH, calendarStyle, calendarLocale)
            );

        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean moreThanToday(String booking_date) {
        try {
            Utils.Log("booking date", booking_date);
            Date bookingDate = formatBookingDate.parse(booking_date);
            Calendar nextMonth = Calendar.getInstance();
            nextMonth.add(Calendar.MONTH, -3);
            return bookingDate.before(nextMonth.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}