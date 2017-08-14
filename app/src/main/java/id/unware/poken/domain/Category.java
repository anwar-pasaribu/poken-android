package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 03 2017
 */

public class Category {

    private long id;
    private String name;
    private String imageUrl;
    private int imageResource;

    public Category() {
    }

    public Category(String name, long id, String imageUrl, int imageResource) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
