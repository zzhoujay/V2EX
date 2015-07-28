package zhou.v2ex.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zhou.v2ex.R;
import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.NodesProvider;
import zhou.v2ex.interfaces.ClickCallback;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Node;
import zhou.v2ex.ui.activity.NodeActivity;
import zhou.v2ex.ui.adapter.NodesAdapter;

/**
 * Created by 州 on 2015/7/20 0020.
 * 显示Node列表的Fragment
 */
public class NodesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NodesAdapter nodesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NodesProvider nodesProvider = new NodesProvider(DataManger.getInstance().getRestAdapter());
        DataManger.getInstance().addProvider(NodesProvider.FILE_NAME, nodesProvider);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        DataManger.getInstance().getData(NodesProvider.FILE_NAME, new OnLoadCompleteListener<List<Node>>() {
            @Override
            public void loadComplete(List<Node> nodes) {
                setUp(nodes);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            DataManger.getInstance().refresh(NodesProvider.FILE_NAME, onLoadComplete);
        }
    };

    private OnLoadCompleteListener<List<Node>> onLoadComplete = new OnLoadCompleteListener<List<Node>>() {
        @Override
        public void loadComplete(List<Node> nodes) {
            if (nodes != null) {
                nodesAdapter.setNodes(nodes);
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private ClickCallback<Node> clickCallback = new ClickCallback<Node>() {
        @Override
        public void callback(Node node) {
            Intent intent = new Intent(getActivity(), NodeActivity.class);
            intent.putExtra(Node.NODE, (Parcelable) node);
            startActivity(intent);
        }
    };

    private void setUp(List<Node> nodes) {
        nodesAdapter = new NodesAdapter(nodes);
        nodesAdapter.setClickCallback(clickCallback);
        recyclerView.setAdapter(nodesAdapter);
    }

    public static NodesFragment newInstance() {
        NodesFragment nodesFragment;
        nodesFragment = new NodesFragment();
        return nodesFragment;
    }
}
