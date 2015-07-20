package zhou.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Topic;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public interface TopicsService {


    @GET("/api/topics/show.json")
    void getTopicsByUserName(@Query("username") String username, Callback<List<Topic>> callback);

    @GET("/api/topics/show.json")
    void getTopicByNodeId(@Query("node_id") int node_id, Callback<List<Topic>> callback);

    @GET("/api/topics/show.json")
    void getTopicsByNodeName(@Query("node_name") String node_name, Callback<List<Topic>> callback);

    @GET("/api/topics/hot.json")
    void getHot(Callback<List<Topic>> listCallback);

    @GET("/api/topics/latest.json")
    void getLatest(Callback<List<Topic>> listCallback);
}
