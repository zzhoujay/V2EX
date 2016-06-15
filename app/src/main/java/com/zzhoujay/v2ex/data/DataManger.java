package com.zzhoujay.v2ex.data;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.interfaces.OnLoadCompleteListener;

/**
 * Created by 州 on 2015/7/20 0020.
 * 数据管理器（内存、本地缓存、联网加载）
 */
public class DataManger {

    private Map<String, DataProvider> providerMap;
    private RestAdapter restAdapter;

    private DataManger() {
        providerMap = new HashMap<>();
        restAdapter = new RestAdapter.Builder().setEndpoint("http://v2ex.com").build();
    }

    private static DataManger dataManger;

    public static DataManger getInstance() {
        if (dataManger == null) {
            dataManger = new DataManger();
        }
        return dataManger;
    }

    /**
     * 联网刷新数据
     *
     * @param key            key
     * @param onLoadComplete 回调
     * @param <T>            type
     */
    @SuppressWarnings("unchecked")
    public <T> void refresh(String key, @Nullable final OnLoadCompleteListener<T> onLoadComplete) {
        final DataProvider<T> provider = providerMap.get(key);
        if (provider != null) {
            //进行联网加载
            provider.getFromNet(new OnLoadCompleteListener<T>() {
                @Override
                public void loadComplete(T o) {
                    if (o != null) {
                        //联网加载成功
                        provider.set(o);
                        if (provider.needCache())
                            provider.persistence();
                    } else {
                        //联网加载失败
                        V2EX.getInstance().toast(R.string.load_error);
                    }
                    if (onLoadComplete != null) {
                        onLoadComplete.loadComplete(o);
                    }
                }
            });
        } else {
            if (onLoadComplete != null) {
                onLoadComplete.loadComplete(null);
            }
        }
    }

    /**
     * 按照 内存->本地缓存->网络数据 的顺序加载数据
     *
     * @param key            key
     * @param onLoadComplete 回调
     * @param <T>            type
     */
    @SuppressWarnings("unchecked")
    public <T> void getData(String key, final OnLoadCompleteListener<T> onLoadComplete) {
        final DataProvider<T> provider = providerMap.get(key);
        if (provider == null) {
            if (onLoadComplete != null) {
                onLoadComplete.loadComplete(null);
            }
            return;
        }
        if (provider.hasLoad()) {
            //已加载至内存
            if (onLoadComplete != null) {
                onLoadComplete.loadComplete(provider.get());
            }
        } else {
            //未加载到内存
            provider.getFromLocal(new OnLoadCompleteListener<T>() {
                @Override
                public void loadComplete(T o) {
                    if (o != null) {
                        //从本地加载到了
                        provider.set(o);
                        if (onLoadComplete != null) {
                            onLoadComplete.loadComplete(o);
                        }
                    } else {
                        //本地没有缓存，联网加载
                        provider.getFromNet(new OnLoadCompleteListener<T>() {
                            @Override
                            public void loadComplete(T o) {
                                if (o != null) {
                                    //联网加载成功
                                    provider.set(o);
                                    if (provider.needCache())
                                        provider.persistence();
                                } else {
                                    //联网加载失败
                                    V2EX.getInstance().toast(R.string.load_error);
                                }
                                if (onLoadComplete != null) {
                                    onLoadComplete.loadComplete(o);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * 添加数据提供器
     *
     * @param key      key
     * @param provider 数据提供器
     * @param <T>      type
     */
    public <T> void addProvider(String key, DataProvider<T> provider) {
        if (provider != null && key != null)
            if (!providerMap.containsKey(key)) {
                providerMap.put(key, provider);
            }
    }

    /**
     * 添加数据提供器
     *
     * @param key      key
     * @param provider 数据提供器
     * @param flag     是否强制添加
     * @param <T>      type
     */
    public <T> void addProvider(String key, DataProvider<T> provider, boolean flag) {
        if (provider != null && key != null) {
            if (flag) {
                providerMap.put(key, provider);
            } else {
                if (!providerMap.containsKey(key)) {
                    providerMap.put(key, provider);
                }
            }
        }
    }

    /**
     * 是否已经添加
     *
     * @param key key
     * @return boolean
     */
    @SuppressWarnings("unused")
    public boolean hasProvider(String key) {
        return providerMap.containsKey(key);
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public void removeProvider(String key) {
        providerMap.remove(key);
    }
}
