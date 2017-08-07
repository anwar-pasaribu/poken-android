package id.unware.poken.interfaces;


import id.unware.poken.pojo.PojoBase;

/**
 * Created by IT11 on 4/2/2015.
 *
 */
public interface VolleyResultListener {
    void onStart(PojoBase clazz);

    void onSuccess(PojoBase clazz);

    void onFinish(PojoBase clazz);

    boolean onError(PojoBase clazz);
}
