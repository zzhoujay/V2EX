package zhou.v2ex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import zhou.v2ex.R;
import zhou.v2ex.data.TopicsProvider;
import zhou.v2ex.model.Node;
import zhou.v2ex.ui.fragment.TopicsFragment;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class NodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setLogo(R.mipmap.ic_launcher);
        }
        Intent intent = getIntent();
        if (intent.hasExtra(Node.NODE)) {
            Node node = intent.getParcelableExtra(Node.NODE);
            TopicsProvider.TopicType topicType = TopicsProvider.TopicType.newTopicTypeByNodeId("node" + node.id, node.id);
            TopicsFragment topicsFragment = TopicsFragment.newInstance(topicType);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, topicsFragment).commit();
        }
    }
}
