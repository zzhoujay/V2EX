package zhou.v2ex.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.squareup.picasso.Picasso;

import java.util.List;

import zhou.v2ex.R;
import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.DataProvider;
import zhou.v2ex.data.RepliesProvider;
import zhou.v2ex.model.Member;
import zhou.v2ex.model.Replies;
import zhou.v2ex.model.Topic;
import zhou.v2ex.ui.adapter.RepliesAdapter;
import zhou.v2ex.util.TimeUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class TopicDetailFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ImageView icon;
    private TextView user, time, replay, node, title, content;
    private Topic topic;
    private RepliesAdapter repliesAdapter;
    private RepliesProvider repliesProvider;
    private View detail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Topic.TOPIC)) {
            topic = bundle.getParcelable(Topic.TOPIC);
            repliesProvider = new RepliesProvider(DataManger.getInstance().getRestAdapter(), topic);
            DataManger.getInstance().addProvider(repliesProvider.FILE_NAME, repliesProvider);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_detail, container, false);
//        swipeRefreshLayout = (SwipeRefreshLayout) view;
        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.header_recycler_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initView(recyclerViewHeader);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewHeader.attachTo(recyclerView, true);
        initData(topic);
//        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        DataManger.getInstance().getData(repliesProvider.FILE_NAME, new DataProvider.OnLoadComplete<List<Replies>>() {
            @Override
            public void loadComplete(List<Replies> replies) {
                setUp(replies);
            }
        });
        return view;
    }

    private void initData(Topic topic) {
        Member member = topic.member;
        Picasso.with(getActivity()).load("http:" + member.avatar_normal).placeholder(R.drawable.default_image).into(icon);
        user.setText(member.username);
        time.setText(TimeUtils.friendlyFormat(topic.created * 1000));
        replay.setText(topic.replies + "个回复");
        node.setText(topic.node.name);
        title.setText(topic.title);
        content.setText(topic.content);
    }

    private void initView(View view) {
        icon = (ImageView) view.findViewById(R.id.topic_icon);
        user = (TextView) view.findViewById(R.id.topic_user);
        time = (TextView) view.findViewById(R.id.topic_time);
        replay = (TextView) view.findViewById(R.id.topic_replay);
        node = (TextView) view.findViewById(R.id.topic_node);
        title = (TextView) view.findViewById(R.id.topic_title);
        content = (TextView) view.findViewById(R.id.topic_content);
    }

    private void setUp(List<Replies> replies) {
        repliesAdapter = new RepliesAdapter(replies);
        recyclerView.setAdapter(repliesAdapter);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            DataManger.getInstance().refresh(repliesProvider.FILE_NAME, onLoadComplete);
        }
    };

    private DataProvider.OnLoadComplete<List<Replies>> onLoadComplete = new DataProvider.OnLoadComplete<List<Replies>>() {
        @Override
        public void loadComplete(List<Replies> replies) {
            if (replies != null) {
                repliesAdapter.setReplies(replies);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };


    public static TopicDetailFragment newInstance(@NonNull Topic topic) {
        TopicDetailFragment topicDetailFragment = new TopicDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Topic.TOPIC, topic);
        topicDetailFragment.setArguments(bundle);
        return topicDetailFragment;
    }
}
