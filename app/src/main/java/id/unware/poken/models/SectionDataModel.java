package id.unware.poken.models;

import java.util.ArrayList;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Section;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 *
 */
public class SectionDataModel {



    private String headerTitle;
    private ArrayList<SingleItemModel> allItemsInSection;
    private ArrayList<Featured> featuredItems;
    private ArrayList<Category> categories;
    private ArrayList<Product> products;
    private Section section;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, ArrayList<SingleItemModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


    public ArrayList<Featured> getFeaturedItems() {
        return featuredItems;
    }

    public void setFeaturedItems(ArrayList<Featured> featuredItems) {
        this.featuredItems = featuredItems;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
