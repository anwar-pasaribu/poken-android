package id.unware.poken.interfaces;


import id.unware.poken.pojo.PojoBase;

/**
 * Created by IT11 on 4/2/2015.
 *
 */
public interface VolleyResultListener {
    public void onStart(PojoBase clazz);

    public void onSuccess(PojoBase clazz);

    public void onFinish(PojoBase clazz);

    public boolean onError(PojoBase clazz);
}
