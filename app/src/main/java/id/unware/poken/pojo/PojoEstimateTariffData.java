package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Represent {@code {{BASE-URL}}/apis/mobile/get_estimated_tariff/53052 } API response.
 *
 * @since Dec 5th 2016 - NEW
 * @author Anwar Pasaribu
 */
public class PojoEstimateTariffData extends PojoBase{

    @SerializedName("tariff") private PojoTarif[] tariffs;


    public PojoEstimateTariffData() {
    }

    public PojoTarif[] getTariffs() {
        return tariffs;
    }

    public void setTariffs(PojoTarif[] tariffs) {
        this.tariffs = tariffs;
    }
}
