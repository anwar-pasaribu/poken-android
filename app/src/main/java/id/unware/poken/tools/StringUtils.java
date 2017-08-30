package id.unware.poken.tools;

import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String TAG = "StringUtils";

    private static Random r = new Random();

    public static String getFileExtension(String fullFileName) {
        String result = "";
        int dot = fullFileName.lastIndexOf(".");
        result = fullFileName.substring(dot);

        return result;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("") || str.trim().toLowerCase().equals("null");
    }

    public final static boolean isValidEmail(String target) {
        if (isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static String delimiterChanger(String oldDelimiter,
                                          String newDelimiter, String msgStr) {
        String result = msgStr.replace(oldDelimiter, newDelimiter);

        return result;
    }

    public static String urlEncode(String value) {
        String encodeResult = "";

        try {
            // encodeResult = URLEncoder.encode(value, "utf-8");
            encodeResult = android.net.Uri.encode(value, "UTF-8");
        } catch (Exception e) {
            Log.v(TAG, "ERROR ENCODE URL UTILS: " + e.getMessage());
        }

        return encodeResult;
    }

    public static String urlDecode(String value) {
        String decodeResult = "";

        try {
            decodeResult = URLDecoder.decode(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodeResult;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "MMMM dd 'at' hh:mm a", Locale.US);
    private static final SimpleDateFormat sdfHour = new SimpleDateFormat(
            "hh 'hours ago'", Locale.US);
    private static final SimpleDateFormat sdfMinutes = new SimpleDateFormat(
            "mm 'minutes ago'", Locale.US);



//	public static String longToDate(long time) {
//
//		final int one_day = 24 * 60 * 60 * 1000;
//
//		long different = Utils.getCurentTime() - (time);
//
//		if (different > one_day * 2) {
//			Date date = new Date(time);
//			return sdf.format(date);
//		} else if (different > one_day) {
//			return "yesterday";
//		} else {
//			if (different > -1) {
//				if (different < 60 * 1000) {
//					return "Just now";
//				} else if (different < 60 * 60 * 1000) {
//					Date date = new Date(different);
//					return sdfMinutes.format(date);
//				} else if (different <= one_day) {
//					Date date = new Date(different);
//					return sdfHour.format(date);
//				}
//			}
//		}
//		return "unknown";
//	}

    private static final SimpleDateFormat sdfEcash = new SimpleDateFormat(
            "MMMM dd 'at' hh:mm a", Locale.US);

//    public static String dateFormatEcash(long time) {
//        long different = Utils.getCurentTime() - time;
//        final int one_day = 24 * 60 * 60 * 1000;
//        if (different > one_day * 2) {
//            Date date = new Date(time);
//            return sdfEcash.format(date);
//        } else {
//            if (different < 60 * 60 * 1000) {
//                Date date = new Date(different);
//                return sdfMinutes.format(date);
//            } else if (different < 24 * 60 * 60 * 1000) {
//                Date date = new Date(different);
//                return sdfHour.format(date);
//            } else {
//                return "yesterday";
//            }
//
//        }
//    }

    public static String adjustmentLine(String msg, int lengthAdjust,
                                        boolean isPadLeft, String delimiter) {
        String result = msg;

        int lengthWord = msg.length();
        int remainSpace = 0;

        if (lengthWord < lengthAdjust) {
            remainSpace = lengthAdjust - lengthWord;
            String space = "";

            for (int i = 0; i < remainSpace; i++) {
                space = space + delimiter;
            }

            if (isPadLeft == true) {
                result = space + result;
            } else {
                result = result + space;
            }
        }

        return result;

    }

    public static String justifyLine(String msg, int lengthAdjust,
                                     String delimiter) {
        String result = msg;

        int lengthWord = msg.length();
        int remainSpace = 0;

        if (lengthWord < lengthAdjust) {
            remainSpace = lengthAdjust - lengthWord;
            String space = "";

            int leftPad = remainSpace / 2;
            int rightPad = remainSpace - leftPad;

            for (int i = 0; i < leftPad; i++) {
                space = space + delimiter;
            }

            result = space + result;

            for (int i = 0; i < rightPad; i++) {
                space = space + delimiter;
            }

            result = result + space;

        }

        return result;

    }

    public static boolean regexPasswordChecker(String regex, String password) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static String formatCurrency(String value) {
        double convert = Double.parseDouble(value);

        DecimalFormat formatter = new DecimalFormat(
                "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

        return "Rp " + formatter.format(convert);
    }

    public static String generateAlphaNumeric(int length) {
        String C = "QWERTYUIOPLKJHGFDAZXCVBNM0987654321";
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            int idx = r.nextInt(C.length());
            sb.append(C.substring(idx, idx + 1));
        }
        return sb.toString();
    }

    public static String md5(String input) {

        String md5 = null;

        if (null == input)
            return null;

        try {

            // Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            // Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }

    /**
     * Remove NON-ASCII chars.
     * @param string : Source string to format
     * @return ASCII String.
     */
    public static String getAsciiString(String string) {
        try {
            Properties p = new Properties();
            p.load(new StringReader("key=" + string));

            return  p.getProperty("key").replaceAll("[^\\x00-\\x7F]", "");

        } catch (IOException | NullPointerException exception) {
            exception.printStackTrace();
        }

        return "";
    }

/*    private SpannableString clickableLocality(String locality) {
        locality += " (Ubah ?)";
        int length = locality.length();
        SpannableString ss = new SpannableString(locality);

        ClickableSpan clickLocality = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                listener.onSetLocationClicked();
            }

        };

        ss.setSpan(clickLocality, length - 8, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }*/

}
