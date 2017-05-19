package id.unware.poken.pojo;

/**
 * Created by PID-T420S on 10/2/2016. <br />
 *
 * Pojo representative for <code>mobile_api/tariff/%s/%s/%s</code>
 *
 * @since (Oct. 7, 2016) V22 - NEW API URL - <code>mobile_api/tariff2/%s/%s/%s</code>
 */
public class PojoTariffCheck extends PojoBase {

    private String from;
    private String to;
    private float weight;
    private PojoRateItem[] tariff;


    public PojoTariffCheck() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsg() {
        return super.msg;
    }

    public void setMsg(String msg) {
        super.msg = msg;
    }

    public int getSuccess() {
        return super.success;
    }

    public void setSuccess(int success) {
        super.success = success;
    }

    public PojoRateItem[] getTariff() {

        return tariff;
    }

    public void setTariff(PojoRateItem[] tariff) {

        this.tariff = tariff;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {

        this.weight = weight;
    }
}
