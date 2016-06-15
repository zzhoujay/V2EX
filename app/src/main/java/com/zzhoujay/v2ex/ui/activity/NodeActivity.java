package com.zzhoujay.v2ex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.data.TopicsProvider;
import com.zzhoujay.v2ex.model.Node;
import com.zzhoujay.v2ex.ui.fragment.TopicsFragment;

/**
 * Created by 州 on 2015/7/20 0020.
 * Node详情
 */
public class NodeActivity extends AppCompatActivity {

    private Node node;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_floatactionbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionBar);

        Intent intent = getIntent();
        if (intent.hasExtra(Node.NODE)) {
            node = intent.getParcelableExtra(Node.NODE);
            setTitle(node.name);
            TopicsProvider.TopicType topicType = TopicsProvider.TopicType.newTopicTypeByNodeId("node_" + node.id, node.id);
            TopicsFragment topicsFragment = TopicsFragment.newInstance(topicType);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, topicsFragment).commit();
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (node != null) {
                        Intent i = new Intent(NodeActivity.this, NewTopicActivity.class);
                        i.putExtra(Node.NODE_NAME, node.name);
                        startActivity(i);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (V2EX.getInstance().isLogin()) {
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
