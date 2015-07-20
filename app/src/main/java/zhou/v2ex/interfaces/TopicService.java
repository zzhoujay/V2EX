package zhou.v2ex.interfaces;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Topic;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public interface TopicService {

    @GET("/api/topics/show.json")
    void getTopic(@Query("id") int id, Callback<Topic> topicCallback);

}
