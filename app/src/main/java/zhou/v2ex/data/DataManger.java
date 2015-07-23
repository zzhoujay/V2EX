package zhou.v2ex.data;

import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import zhou.v2ex.R;
import zhou.v2ex.Z2EX;

/**
 * Created by 州 on 2015/7/20 0020.
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

    public void refresh(String key, final DataProvider.OnLoadComplete onLoadComplete) {
        final DataProvider provider = providerMap.get(key);
        if (provider != null) {
            //进行联网加载
            provider.getFromNet(new DataProvider.OnLoadComplete() {
                @Override
                public void loadComplete(Object o) {
                    if (o != null) {
                        //联网加载成功
                        provider.set(o);
                        if (provider.needCache())
                            provider.persistence();
                    } else {
                        //联网加载失败
                        Z2EX.getInstance().toast(R.string.load_error);
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

    public void getData(String key, final DataProvider.OnLoadComplete onLoadComplete) {
        final DataProvider provider = providerMap.get(key);
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
            provider.getFromLocal(new DataProvider.OnLoadComplete() {
                @Override
                public void loadComplete(Object o) {
                    if (o != null) {
                        //从本地加载到了
                        provider.set(o);
                        if (onLoadComplete != null) {
                            onLoadComplete.loadComplete(o);
                        }
                    } else {
                        //本地没有缓存，联网加载
                        provider.getFromNet(new DataProvider.OnLoadComplete() {
                            @Override
                            public void loadComplete(Object o) {
                                if (o != null) {
                                    //联网加载成功
                                    provider.set(o);
                                    if (provider.needCache())
                                        provider.persistence();
                                } else {
                                    //联网加载失败
                                    Z2EX.getInstance().toast(R.string.load_error);
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

    public <T> void addProvider(String key, DataProvider<T> provider) {
        if (provider != null && key != null)
            if(!providerMap.containsKey(key)){
                providerMap.put(key, provider);
            }
    }

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

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public void removeProvider(String key) {
        providerMap.remove(key);
    }
}
