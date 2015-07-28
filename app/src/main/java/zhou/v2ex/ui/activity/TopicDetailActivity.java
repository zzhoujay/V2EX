package zhou.v2ex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zhou.v2ex.R;
import zhou.v2ex.V2EX;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Replies;
import zhou.v2ex.model.Topic;
import zhou.v2ex.ui.fragment.ReplyFragment;
import zhou.v2ex.ui.fragment.TopicDetailFragment;

/**
 * Created by 州 on 2015/7/20 0020.
 * Topic详情
 */
public class TopicDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.topic_detail);
        Intent intent = getIntent();
        if (intent.hasExtra(Topic.TOPIC)) {
            Topic topic = intent.getParcelableExtra(Topic.TOPIC);
            final TopicDetailFragment topicDetailFragment = TopicDetailFragment.newInstance(topic);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, topicDetailFragment).commit();
            if (V2EX.getInstance().isLogin()) {
                final ReplyFragment replyFragment = ReplyFragment.newInstance(topic);
                replyFragment.setOnReplySuccessListener(new ReplyFragment.OnReplySuccessListener() {
                    @Override
                    public void replySuccess() {
                        topicDetailFragment.refresh();
                        topicDetailFragment.scrollToBottom();
                    }
                });
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_bottom, replyFragment).commit();

                topicDetailFragment.setOnItemClickCallback(new OnLoadCompleteListener<Replies>() {
                    @Override
                    public void loadComplete(Replies replies) {
                        String content = "@" + replies.member.username + " ";
                        replyFragment.setContent(content);
                        replyFragment.setSelection(content.length());
                    }
                });
            }

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
