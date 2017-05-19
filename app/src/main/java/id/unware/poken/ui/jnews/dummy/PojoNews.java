package id.unware.poken.ui.jnews.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PojoNews {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<News> ITEMS = new ArrayList<News>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, News> ITEM_MAP = new HashMap<String, News>();

    static {
        // Add some sample items.
        ITEMS.addAll(createDummyItem());
        for (int i = 0; i < ITEMS.size(); i++) {
            addItem(ITEMS.get(i));
        }
    }

    private static void addItem(News item) {
        ITEM_MAP.put(item.id, item);
    }

    private static ArrayList<News> createDummyItem() {
        ArrayList<News> newsArrayList = new ArrayList<>();
        News news1 = new News("0",
                "JNE Hadirkan Produk Khas Dalam Negeri untuk Meriahkan Gebyar Bekasi Festival 2017",
                "Bekasi merupakan kota dengan penduduk terbanyak keempat di Indonesia dan menjadi sentra industri serta tempat tinggal masyarakat urban.",
                "19 March 2017",
                "http://jne.co.id/id/berita/berita-detail/jne-hadirkan-produk-khas-dalam-negeri-untuk-meriahkan-gebyar-bekasi-festival-2017");

        News news2 = new News("1",
                "JNE Tantang Perusahaan E-Commerce dan Media Massa dalam JNE Ping The Pong Cup 2017",
                "Olahraga tenis meja, atau yang dikenal juga dengan nama olahraga pingpong, dibuat di Inggris pada abad ke-19.",
                "14 March 2017",
                "http://jne.co.id/id/berita/berita-detail/jne-tantang-perusahaan-e-commerce-dan-media-massa-dalam-jne-ping-the-pong-cup-2017");

        News news3 = new News("1",
                "Dukungan JNE untuk Mahasiswa dan Siswa Berprestasi Terus Berlanjut",
                "Dalam menjalankan langkah-langkah untuk kemajuan perusahaan, JNE tidak hanya selalu berupaya maksimal dalam memberikan kualitas pelayanan prima kepada pelanggan, tapi juga memiliki tujuan agar setiap langkah yang dijalankan dapat memberikan dampak positif terhadap masyarakat.",
                "06 March 2017",
                "http://jne.co.id/id/berita/berita-detail/dukungan-jne-untuk-mahasiswa-dan-siswa-berprestasi-terus-berlanjut");

        newsArrayList.add(news1);
        newsArrayList.add(news2);
        newsArrayList.add(news3);

        return newsArrayList;
    }

    public static class News {
        public final String id;
        public final String newsTitle;
        public final String newsDesc;
        public final String newsPublishDate;
        public final String newsUrl;

        public News(String id, String newsTitle, String newsDesc, String publishDate, String url) {
            this.id = id;
            this.newsTitle = newsTitle;
            this.newsDesc = newsDesc;
            this.newsPublishDate = publishDate;
            this.newsUrl = url;
        }

        @Override
        public String toString() {
            return newsTitle;
        }
    }
}
