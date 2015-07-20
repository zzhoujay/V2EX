package zhou.v2ex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zhou.v2ex.R;
import zhou.v2ex.model.Topic;
import zhou.v2ex.ui.fragment.TopicDetailFragment;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class TopicDetailActivity extends AppCompatActivity {
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
        setTitle(R.string.topic_detail);
        Intent intent = getIntent();
        if (intent.hasExtra(Topic.TOPIC)) {
            Topic topic = intent.getParcelableExtra(Topic.TOPIC);
            TopicDetailFragment topicDetailFragment = TopicDetailFragment.newInstance(topic);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, topicDetailFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
