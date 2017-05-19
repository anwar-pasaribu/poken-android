package id.unware.poken.pojo;

/**
 * @author Anwar Pasaribu
 * @since Feb 08 2017
 */

public class PojoPackageService {

    private String serviceId;
    private String serviceName;
    private String serviceDescription;


    /**
     * Identify item is selected or no.
     */
    private boolean selected;

    public PojoPackageService() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
