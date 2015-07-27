package zhou.v2ex.data;

import android.util.Log;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zhou.v2ex.R;
import zhou.v2ex.Z2EX;
import zhou.v2ex.interfaces.NodesService;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Node;
import zhou.v2ex.util.FileUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class NodesProvider implements DataProvider<List<Node>> {

    public static final String FILE_NAME = "node.cache";

    private List<Node> nodes;
    private NodesService nodesService;

    public NodesProvider(RestAdapter restAdapter) {
        nodesService = restAdapter.create(NodesService.class);
    }

    @Override
    public void persistence() {
        if (nodes == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                File file = new File(Z2EX.getInstance().getCacheDir(), FILE_NAME);
                FileUtils.writeObject(file, nodes);
            }
        }.start();
    }

    @Override
    public List<Node> get() {
        return nodes;
    }

    @Override
    public void set(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void getFromLocal(OnLoadCompleteListener<List<Node>> loadComplete) {
        File file = new File(Z2EX.getInstance().getCacheDir(), FILE_NAME);
        List<Node> ns = null;
        if (file.exists()) {
            try {
                ns = (List<Node>) FileUtils.readObject(file);
            } catch (Exception e) {
                Log.d("getFromLocal", "NodesProvider", e);
            }
        }
        if (loadComplete != null) {
            loadComplete.loadComplete(ns);
        }
    }

    @Override
    public void getFromNet(final OnLoadCompleteListener<List<Node>> loadComplete) {
        if (!Z2EX.getInstance().isNetworkConnected()) {
            //网络未连接
            Z2EX.getInstance().toast(R.string.network_error);
            if (loadComplete != null) {
                loadComplete.loadComplete(null);
            }
            return;
        }
        nodesService.listNode(new Callback<List<Node>>() {
            @Override
            public void success(List<Node> nodes, Response response) {
                Log.d("getFromNet", "success");
                if (loadComplete != null) {
                    loadComplete.loadComplete(nodes);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("getFromNet", "failure", error);
                if (loadComplete != null) {
                    loadComplete.loadComplete(null);
                }
            }
        });
    }

    @Override
    public boolean hasLoad() {
        return nodes != null;
    }

    @Override
    public boolean needCache() {
        return true;
    }
}
