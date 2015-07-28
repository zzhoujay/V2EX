package zhou.v2ex.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import zhou.v2ex.R;
import zhou.v2ex.V2EX;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Node;
import zhou.v2ex.util.UserUtils;

/**
 * Created by zzhoujay on 2015/7/28 0028.
 * 新建主题
 */
public class NewTopicActivity extends AppCompatActivity {

    private String nodeName;
    private MenuItem item;
    private EditText title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(R.string.new_topic);

        Intent intent = getIntent();
        if (intent.hasExtra(Node.NODE_NAME)) {
            nodeName = intent.getStringExtra(Node.NODE_NAME);
        }
        title = (EditText) findViewById(R.id.new_topic_title);
        content = (EditText) findViewById(R.id.new_topic_content);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (title.getText().toString().isEmpty()) {
                    item.setEnabled(false);
                } else {
                    item.setEnabled(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item = menu.add(0, 10086, 0, R.string.new_topic_publish);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_done_white);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 10086:
                Log.d("test", "clicked:" + nodeName);
                String c = content.getText().toString();
                if (c.isEmpty()) {
                    V2EX.getInstance().toast(R.string.content_empty);
                } else {
                    if (nodeName != null) {
                        String t = title.getText().toString();
                        if (t.isEmpty()) {
                            V2EX.getInstance().toast(R.string.title_empty);
                        } else {
                            final ProgressDialog progressDialog = new ProgressDialog(NewTopicActivity.this);
                            progressDialog.setMessage(getString(R.string.new_topic_progress));
                            progressDialog.show();
                            UserUtils.createTopic(nodeName, t, c, new OnLoadCompleteListener<Boolean>() {
                                @Override
                                public void loadComplete(Boolean aBoolean) {
                                    progressDialog.dismiss();
                                    if (aBoolean) {
                                        V2EX.getInstance().toast(R.string.new_topic_success);
                                        finish();
                                    } else {
                                        V2EX.getInstance().toast(R.string.new_topic_error);
                                    }
                                }
                            });
                        }
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
