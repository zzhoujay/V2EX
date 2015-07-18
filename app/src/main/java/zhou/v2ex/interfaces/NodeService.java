package zhou.v2ex.interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Node;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public interface NodeService {
    @GET("/api/nodes/all.json")
    void listNode(Callback<List<Node>> ns);

    @GET("/api/nodes/show.json")
    void getNode(@Query("id") int id, Callback<Node> node);
}
