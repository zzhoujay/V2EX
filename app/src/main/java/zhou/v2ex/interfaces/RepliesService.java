package zhou.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Replies;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public interface RepliesService {
    @GET("/api/replies/show.json")
    void getReplise(@Query("topic_id") int topic_id, Callback<List<Replies>> callback);

    @GET("/api/replies/show.json")
    void getReplise(@Query("topic_id") int topic_id, @Query("page") int page, @Query("page_size") int page_size, Callback<List<Replies>> callback);
}
