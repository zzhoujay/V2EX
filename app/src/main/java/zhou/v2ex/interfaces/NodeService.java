package zhou.v2ex.interfaces;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Node;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public interface NodeService {

    @GET("/api/nodes/show.json")
    void getNode(@Query("id") int id, Callback<Node> node);
}
