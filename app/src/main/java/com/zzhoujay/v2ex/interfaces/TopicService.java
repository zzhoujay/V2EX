package com.zzhoujay.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import com.zzhoujay.v2ex.model.Topic;

/**
 * Created by 州 on 2015/7/20 0020.
 * Topic的Service
 */
public interface TopicService {

    @GET("/api/topics/show.json")
    void getTopic(@Query("id") int id, Callback<List<Topic>> topicCallback);

}
