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
import zhou.v2ex.interfaces.RepliesService;
import zhou.v2ex.model.Replies;
import zhou.v2ex.model.Topic;
import zhou.v2ex.util.FileUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class RepliesProvider implements DataProvider<List<Replies>> {

    public String FILE_NAME = "topic_replies_";

    private List<Replies> replies;
    private Topic topic;
    private RepliesService repliesService;

    public RepliesProvider(RestAdapter restAdapter, Topic topic) {
        this.topic = topic;
        FILE_NAME = FILE_NAME + topic.id;
        repliesService = restAdapter.create(RepliesService.class);
    }

    @Override
    public void persistence() {
        new Thread() {
            @Override
            public void run() {
                File file = new File(Z2EX.getInstance().getCacheDir(), FILE_NAME);
                FileUtils.writeObject(file, replies);
            }
        }.start();
    }

    @Override
    public List<Replies> get() {
        return replies;
    }

    @Override
    public void set(List<Replies> replies) {
        this.replies = replies;
    }

    @Override
    public void getFromLocal(OnLoadComplete<List<Replies>> loadComplete) {
        File file = new File(Z2EX.getInstance().getCacheDir(), FILE_NAME);
        List<Replies> rs = null;
        if (file.exists()) {
            rs = (List<Replies>) FileUtils.readObject(file);
        }
        if (loadComplete != null) {
            loadComplete.loadComplete(rs);
        }
    }

    @Override
    public void getFromNet(final OnLoadComplete<List<Replies>> loadComplete) {
        if (!Z2EX.getInstance().isNetworkConnected()) {
            //网络未连接
            Z2EX.getInstance().toast(R.string.network_error);
            if (loadComplete != null) {
                loadComplete.loadComplete(null);
            }
            return;
        }
        repliesService.getReplise(topic.id, new Callback<List<Replies>>() {
            @Override
            public void success(List<Replies> replies, Response response) {
                Log.d("getFromNet", "success");
                if (loadComplete != null) {
                    loadComplete.loadComplete(replies);
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
        return replies != null;
    }

}
