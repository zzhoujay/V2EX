package com.zzhoujay.v2ex.data;

import android.util.Log;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.interfaces.OnLoadCompleteListener;
import com.zzhoujay.v2ex.interfaces.RepliesService;
import com.zzhoujay.v2ex.model.Replies;
import com.zzhoujay.v2ex.model.Topic;
import com.zzhoujay.v2ex.util.FileUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 * Replies的数据提供器的实现
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
        if(replies==null){
            return;
        }
        new Thread() {
            @Override
            public void run() {
                File file = new File(V2EX.getInstance().getCacheDir(), FILE_NAME);
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
    @SuppressWarnings("unchecked")
    public void getFromLocal(OnLoadCompleteListener<List<Replies>> loadComplete) {
        File file = new File(V2EX.getInstance().getCacheDir(), FILE_NAME);
        List<Replies> rs = null;
        if (file.exists()) {
            try {
                rs = (List<Replies>) FileUtils.readObject(file);
            } catch (Exception e) {
                Log.d("getFromLocal", "RepliesProvider", e);
            }
        }
        if (loadComplete != null) {
            loadComplete.loadComplete(rs);
        }
    }

    @Override
    public void getFromNet(final OnLoadCompleteListener<List<Replies>> loadComplete) {
        if (!V2EX.getInstance().isNetworkConnected()) {
            //网络未连接
            V2EX.getInstance().toast(R.string.network_error);
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

    @Override
    public boolean needCache() {
        return V2EX.getInstance().saveCache();
    }

}
