package zhou.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Topic;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public interface TopicService {
    @GET("/api/topics/show.json")
    void getTopic(@Query("id") int id, Callback<Topic> topicCallback);

    @GET("/api/topics/hot.json")
    void getHot(Callback<List<Topic>> listCallback);

    @GET("/api/topics/latest.json")
    void getlatest(Callback<List<Topic>> listCallback);
}
