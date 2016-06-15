package com.zzhoujay.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import com.zzhoujay.v2ex.model.Replies;

/**
 * Created by 州 on 2015/7/18 0018.
 * Replies的Service
 */
public interface RepliesService {
    @GET("/api/replies/show.json")
    void getReplise(@Query("topic_id") int topic_id, Callback<List<Replies>> callback);

    @SuppressWarnings("unused")
    @GET("/api/replies/show.json")
    void getReplise(@Query("topic_id") int topic_id, @Query("page") int page, @Query("page_size") int page_size, Callback<List<Replies>> callback);
}
