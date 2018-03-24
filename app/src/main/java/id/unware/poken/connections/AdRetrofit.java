package id.unware.poken.connections;

//import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import id.unware.poken.BuildConfig;
import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdRetrofit {

    private static Retrofit instance;

    public static Retrofit getInstancePoken() {
        if (instance == null) {

            int timeOut = BuildConfig.DEV_MODE ? 10 : 60;
            HttpLoggingInterceptor.Level debugLevel = BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE;

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(debugLevel);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .writeTimeout(timeOut, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    // .addNetworkInterceptor(new StethoInterceptor())  // Stetho Network Inspector
                    .build();

            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")  // DRF default format: 2017-06-07T14:28:30.127000Z
                    .create();

            instance = new Retrofit.Builder()
                    .baseUrl(BuildConfig.HOST)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return instance;
    }
}