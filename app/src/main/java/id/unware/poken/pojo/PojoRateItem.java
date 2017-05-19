package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import id.unware.poken.tools.Utils;

public class PojoRateItem {

    private final String TAG = this.getClass().getSimpleName();

    @SerializedName("SERVICE") private String service;
    @SerializedName("DESCRIPTION") private String description;
    @SerializedName("TARIFF") private float tariff;
    @SerializedName("EST_DAY") private String estDay;
    @SerializedName("CURRENCY") private String currency;

    public PojoRateItem() {
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTariff() {
        return tariff;
    }

    public void setTariff(int tariff) {
        this.tariff = tariff;
    }

    public String getEstDay() {
        if (this.estDay != null) {
            return estDay;
        } else {
            Utils.Log(TAG, "EST Day is NULL");
            return "";
        }
    }

    public void setEstDay(String estDay) {
        this.estDay = estDay;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
