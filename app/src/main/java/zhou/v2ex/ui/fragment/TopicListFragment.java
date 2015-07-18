package zhou.v2ex.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zhou.v2ex.R;
import zhou.v2ex.data.DataProvider;
import zhou.v2ex.interfaces.TopicService;
import zhou.v2ex.model.Topic;
import zhou.v2ex.ui.adapter.TopicListAdapter;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class TopicListFragment extends Fragment {

    public static final String TOPIC_LIST = "topic_list:";
    public static final String TOPIC_LIST_KEY = "topic_list_key";

    private RecyclerView recyclerView;
    private TopicListAdapter topicListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        /*if (bundle.containsKey(TOPIC_LIST_KEY)) {
            List<Topic> topics = (List<Topic>) DataProvider.getInstance().get(bundle.getString(TOPIC_LIST_KEY));
            if (topics != null) {
                topicListAdapter = new TopicListAdapter(topics);
            }
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.v2ex.com").build();
        TopicService topicService = restAdapter.create(TopicService.class);
        topicService.getlatest(new Callback<List<Topic>>() {
            @Override
            public void success(List<Topic> topics, Response response) {
                Log.i("success", topics.toString());
                setUp(topics);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("failure", "error", error);
            }
        });
        return view;
    }


    private void setUp(List<Topic> topics) {
        topicListAdapter = new TopicListAdapter(topics);
        recyclerView.setAdapter(topicListAdapter);


    }

    public static TopicListFragment newInstance(List<Topic> topics) {
        TopicListFragment topicListFragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOPIC_LIST_KEY, TOPIC_LIST + topics.hashCode());
        DataProvider.getInstance().put(TOPIC_LIST + topics.hashCode(), topics);
        topicListFragment.setArguments(bundle);
        return topicListFragment;
    }
}
