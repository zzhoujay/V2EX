package com.zzhoujay.v2ex.interfaces;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import com.zzhoujay.v2ex.model.Node;

/**
 * Created by 州 on 2015/7/20 0020.
 * Node的Service
 */
@SuppressWarnings("unused")
public interface NodeService {

    @GET("/api/nodes/show.json")
    @SuppressWarnings("unused")
    void getNode(@Query("id") int id, Callback<Node> node);
}
