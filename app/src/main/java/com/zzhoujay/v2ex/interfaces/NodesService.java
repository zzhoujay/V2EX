package com.zzhoujay.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import com.zzhoujay.v2ex.model.Node;

/**
 * Created by 州 on 2015/7/18 0018.
 * Node列表的Service
 */
public interface NodesService {
    @GET("/api/nodes/all.json")
    void listNode(Callback<List<Node>> ns);
}
