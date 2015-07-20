package zhou.v2ex.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zhou.v2ex.R;
import zhou.v2ex.Z2EX;
import zhou.v2ex.interfaces.TopicsService;
import zhou.v2ex.model.Topic;
import zhou.v2ex.util.FileUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class TopicsProvider implements DataProvider<List<Topic>> {

    private TopicType topicType;

    private List<Topic> topics;
    private TopicsService topicsService;

    public TopicsProvider(RestAdapter restAdapter, TopicType topicType) {
        topicsService = restAdapter.create(TopicsService.class);
        this.topicType = topicType;
    }

    @Override
    public void persistence() {
        if (topics == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                File file = new File(Z2EX.getInstance().getCacheDir(), topicType.fileName);
                FileUtils.writeObject(file, topics);
            }
        }.start();
    }

    @Override
    public List<Topic> get() {
        return topics;
    }

    @Override
    public void set(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public void getFromLocal(OnLoadComplete<List<Topic>> loadComplete) {
        File file = new File(Z2EX.getInstance().getCacheDir(), topicType.fileName);
        List<Topic> ts = null;
        if (file.exists()) {
            ts = (List<Topic>) FileUtils.readObject(file);
        }
        if (loadComplete != null) {
            loadComplete.loadComplete(ts);
        }
    }


    @Override
    public void getFromNet(final OnLoadComplete<List<Topic>> loadComplete) {
        if (!Z2EX.getInstance().isNetworkConnected()) {
            //网络未连接
            Z2EX.getInstance().toast(R.string.network_error);
            if (loadComplete != null) {
                loadComplete.loadComplete(null);
            }
            return;
        }
        //加载完成后的回调
        Callback<List<Topic>> callback = new Callback<List<Topic>>() {
            @Override
            public void success(List<Topic> topics, Response response) {
                Log.d("getFromNet", "success");
                if (loadComplete != null) {
                    loadComplete.loadComplete(topics);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("getFromNet", "failure", error);
                if (loadComplete != null) {
                    loadComplete.loadComplete(null);
                }
            }
        };

        if (TopicType.FILE_NAME_LATEST.equals(topicType.fileName)) {//最新主题
            topicsService.getLatest(callback);
        } else if (TopicType.FILE_NAME_HOT.equals(topicType.fileName)) {//最热主题
            topicsService.getHot(callback);
        } else {//其他主题
            if (topicType.nodeName != null) {//通过节点名字查找
                topicsService.getTopicsByNodeName(topicType.nodeName, callback);
            } else if (topicType.userName != null) {//通过用户名查找
                topicsService.getTopicsByUserName(topicType.userName, callback);
            } else if (topicType.nodeId != -1) {//通过节点ID查找
                topicsService.getTopicByNodeId(topicType.nodeId, callback);
            } else {//异常情况
                Z2EX.getInstance().toast(R.string.unknown_error);
                if (loadComplete != null) {
                    loadComplete.loadComplete(null);
                }
            }
        }
    }


    @Override
    public boolean hasLoad() {
        return topics != null;
    }

    public static class TopicType implements Serializable, Parcelable {

        public static final String FILE_NAME_LATEST = "latest.cache";
        public static final String FILE_NAME_HOT = "hot.cache";

        public String fileName;
        public String userName;
        public String nodeName;
        public int nodeId;

        public static final TopicType LATEST = new TopicType(FILE_NAME_LATEST);
        public static final TopicType HOT = new TopicType(FILE_NAME_HOT);

        private TopicType(String fileName) {
            this.fileName = fileName;
            userName = null;
            nodeName = null;
            nodeId = -1;
        }

        public static TopicType newTopicTypeByUserName(String fileName, String userName) {
            TopicType topicType = new TopicType(fileName);
            topicType.userName = userName;
            return topicType;
        }

        public static TopicType newTopicTypeByNodeName(String fileName, String nodeName) {
            TopicType topicType = new TopicType(fileName);
            topicType.nodeName = nodeName;
            return topicType;
        }

        public static TopicType newTopicTypeByNodeId(String fileName, int nodeId) {
            TopicType topicType = new TopicType(fileName);
            topicType.nodeId = nodeId;
            return topicType;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.fileName);
            dest.writeString(this.userName);
            dest.writeString(this.nodeName);
            dest.writeInt(this.nodeId);
        }

        protected TopicType(Parcel in) {
            this.fileName = in.readString();
            this.userName = in.readString();
            this.nodeName = in.readString();
            this.nodeId = in.readInt();
        }

        public static final Parcelable.Creator<TopicType> CREATOR = new Parcelable.Creator<TopicType>() {
            public TopicType createFromParcel(Parcel source) {
                return new TopicType(source);
            }

            public TopicType[] newArray(int size) {
                return new TopicType[size];
            }
        };
    }
}
