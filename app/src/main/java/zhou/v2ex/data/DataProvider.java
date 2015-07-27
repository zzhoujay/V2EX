package zhou.v2ex.data;

import zhou.v2ex.interfaces.OnLoadCompleteListener;

/**
 * Created by 州 on 2015/7/19 0019.
 */
public interface DataProvider<T> {

    void persistence();

    T get();

    void set(T t);

    void getFromLocal(OnLoadCompleteListener<T> loadComplete);

    void getFromNet(OnLoadCompleteListener<T> loadComplete);

    boolean hasLoad();

    boolean needCache();

}
