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
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.interfaces.TopicService;
import zhou.v2ex.model.Topic;
import zhou.v2ex.util.FileUtils;

/**
 * Created by zzhoujay on 2015/7/22 0022.
 */
public class TopicProvider implements DataProvider<Topic> {

    public String FILE_NAME = "topic_";

    private Topic topic;
    private TopicService topicService;
    private int id;

    public TopicProvider(RestAdapter restAdapter, int id) {
        this.id = id;
        topicService = restAdapter.create(TopicService.class);
        FILE_NAME += id;
    }


    @Override
    public void persistence() {
        if (!hasLoad()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                File file = new File(Z2EX.getInstance().getCacheDir(), FILE_NAME);
                FileUtils.writeObject(file, topic);
            }
        }.start();
    }

    @Override
    public Topic get() {
        return topic;
    }

    @Override
    public void set(Topic topic) {
        this.topic = topic;
    }

    @Override
    public void getFromLocal(OnLoadCompleteListener<Topic> loadComplete) {
        File file = new File(Z2EX.getInstance().getCacheDir(), FILE_NAME);
        Topic t = null;
        if (file.exists()) {
            try {
                t = (Topic) FileUtils.readObject(file);
            } catch (Exception e) {
                Log.d("getFromLocal", "error", e);
            }
        }
        if (loadComplete != null) {
            loadComplete.loadComplete(t);
        }
    }

    @Override
    public void getFromNet(final OnLoadCompleteListener<Topic> loadComplete) {
        if (!Z2EX.getInstance().isNetworkConnected()) {
            //网络未连接
            Z2EX.getInstance().toast(R.string.network_error);
            if (loadComplete != null) {
                loadComplete.loadComplete(null);
            }
            return;
        }
        topicService.getTopic(id, new Callback<List<Topic>>() {
            @Override
            public void success(List<Topic> topic, Response response) {
                Log.d("getFromNet", "success_topic");
                if (loadComplete != null) {
                    loadComplete.loadComplete(topic.get(0));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("getFromNet", "failure_topic", error);
                if (loadComplete != null) {
                    loadComplete.loadComplete(null);
                }
            }
        });
    }

    @Override
    public boolean hasLoad() {
        return topic != null;
    }

    @Override
    public boolean needCache() {
        return Z2EX.getInstance().saveCache();
    }
}
