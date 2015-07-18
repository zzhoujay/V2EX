package zhou.v2ex.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;
import zhou.v2ex.interfaces.TopicService;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class DataProvider {

    public static final String LATEST_TOPIC = "latest_topic";
    public static final String HOT_TOPIC = "hot_topic";
    public static final String NODE = "node";


    private Map<String, Object> cache;
    private RestAdapter restAdapter;
    private TopicService topicService;


    private static DataProvider dataCache;

    private DataProvider() {
        cache = new HashMap<>();
        restAdapter = new RestAdapter.Builder().setEndpoint("http://www.v2ex.com").build();
        topicService = restAdapter.create(TopicService.class);
    }

    public static DataProvider getInstance() {
        if (dataCache == null) {
            dataCache = new DataProvider();
        }
        return dataCache;
    }

    public Object put(String key, Object value) {
        return cache.put(key, value);
    }

    public Object get(String key) {
        return cache.remove(key);
    }


    private void loadTopicListFromLocal(String flag){

    }

    public boolean isLoaded() {
        return false;
    }


    public interface OnLoadCompleteListener {
        void loadComplete(boolean flag);
    }
}
