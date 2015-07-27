package zhou.v2ex.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zhou.v2ex.R;
import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.TopicsProvider;
import zhou.v2ex.interfaces.ClickCallback;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Topic;
import zhou.v2ex.ui.activity.TopicDetailActivity;
import zhou.v2ex.ui.adapter.TopicsAdapter;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class TopicsFragment extends Fragment {

    public static final String TYPE = "type";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TopicsProvider.TopicType topicType;
    private TopicsAdapter topicsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey(TYPE)) {
            topicType = bundle.getParcelable(TYPE);
            if (topicType != null) {
                TopicsProvider topicsProvider = new TopicsProvider(DataManger.getInstance().getRestAdapter(), topicType);
                DataManger.getInstance().addProvider(topicType.fileName, topicsProvider);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        setUp(null);
        DataManger.getInstance().getData(topicType.fileName, new OnLoadCompleteListener<List<Topic>>() {
            @Override
            public void loadComplete(List<Topic> topics) {
                if (topics != null && topics.size() > 0) {
                    topicsAdapter.setTopics(topics);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        return view;
    }

    private void setUp(List<Topic> topics) {
        topicsAdapter = new TopicsAdapter(topics);
        topicsAdapter.setClickCallback(clickCallback);
        recyclerView.setAdapter(topicsAdapter);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            DataManger.getInstance().refresh(topicType.fileName, refreshListener);
        }
    };

    private OnLoadCompleteListener<List<Topic>> refreshListener = new OnLoadCompleteListener<List<Topic>>() {
        @Override
        public void loadComplete(List<Topic> topics) {
            if (topics != null && topics.size() > 0) {
                topicsAdapter.setTopics(topics);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private ClickCallback<Topic> clickCallback = new ClickCallback<Topic>() {
        @Override
        public void callback(Topic topic) {
            Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
            intent.putExtra(Topic.TOPIC, (Parcelable) topic);
            startActivity(intent);
        }
    };

    public void setSwipeRefreshEnable(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    public static TopicsFragment newInstance(@NonNull TopicsProvider.TopicType topicType) {
        TopicsFragment topicsFragment = new TopicsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TYPE, topicType);
        topicsFragment.setArguments(bundle);
        return topicsFragment;
    }
}
