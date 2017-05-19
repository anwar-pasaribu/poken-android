package id.unware.poken.pojo;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model to save recent search keyword. This model tend to minimalize retype the same
 * keyword overtime.
 *
 * @author Anwar Pasaribu
 * @since Nov 21 2016 - NEW!
 */

public class RecentSearchKeyword extends RealmObject {
    @PrimaryKey private long id;
    private String keywordString;
    private Date timeStamp;

    /** Tag contain info where keyword created (ex. Postcode, Package)*/
    private int tag;

    public static final String KEY_ID = "id";
    public static final String KEY_STRING_KEYWORD = "keywordString";
    public static final String KEY_TIMESTAMP = "timeStamp";
    public static final String KEY_TAG = "tag";


    public RecentSearchKeyword() {
    }

    public RecentSearchKeyword(long id, String keywordString, Date timeStamp, int tag) {
        this.id = id;
        this.keywordString = keywordString;
        this.timeStamp = timeStamp;
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeywordString() {
        return keywordString;
    }

    public void setKeywordString(String keywordString) {
        this.keywordString = keywordString;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
