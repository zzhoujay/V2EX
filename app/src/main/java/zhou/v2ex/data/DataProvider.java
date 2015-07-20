package zhou.v2ex.data;

/**
 * Created by 州 on 2015/7/19 0019.
 */
public interface DataProvider<T> {

    void persistence();

    T get();

    void set(T t);

    void getFromLocal(OnLoadComplete<T> loadComplete);

    void getFromNet(OnLoadComplete<T> loadComplete);

    boolean hasLoad();

    interface OnLoadComplete<T> {
        void loadComplete(T t);
    }
}
