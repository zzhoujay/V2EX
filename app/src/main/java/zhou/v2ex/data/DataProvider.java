package zhou.v2ex.data;

import zhou.v2ex.interfaces.OnLoadCompleteListener;

/**
 * Created by 州 on 2015/7/19 0019.
 * 数据提供器接口
 */
public interface DataProvider<T> {

    /**
     * 数据持久化
     */
    void persistence();

    /**
     * 获取数据
     *
     * @return 数据
     */
    T get();

    /**
     * 设置数据
     *
     * @param t 数据
     */
    void set(T t);

    /**
     * 从本地获取数据
     *
     * @param loadComplete 回调
     */
    void getFromLocal(OnLoadCompleteListener<T> loadComplete);

    /**
     * 从网络加载数据
     *
     * @param loadComplete 回调
     */
    void getFromNet(OnLoadCompleteListener<T> loadComplete);

    /**
     * 是否已经加载
     *
     * @return boolean
     */
    boolean hasLoad();

    /**
     * 是否需要缓存
     *
     * @return boolean
     */
    boolean needCache();

}
