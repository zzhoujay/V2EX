package com.zzhoujay.v2ex.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.data.DataManger;
import com.zzhoujay.v2ex.data.MemberProvider;
import com.zzhoujay.v2ex.data.TopicsProvider;
import com.zzhoujay.v2ex.interfaces.OnLoadCompleteListener;
import com.zzhoujay.v2ex.model.Member;
import com.zzhoujay.v2ex.ui.fragment.TopicsFragment;
import com.zzhoujay.v2ex.util.UserUtils;

/**
 * Created by zzhoujay on 2015/7/22 0022.
 * Member详细资料
 */
public class MemberActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Member member;
    private ImageView icon;
    private TextView name;
    private AppBarLayout appBarLayout;
    private MemberProvider memberProvider;
    private TopicsFragment topicsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        Intent intent = getIntent();
        Uri uri = intent.getData();
        String username = null;
        if (uri != null) {
            username = uri.getPathSegments().get(1);
        } else {
            if (intent.hasExtra(Member.MEMBER)) {
                member = intent.getParcelableExtra(Member.MEMBER);
            } else if (intent.hasExtra(Member.MEMBER_NAME)) {
                username = intent.getStringExtra(Member.MEMBER_NAME);
            }
        }


        initView();


        if (username != null) {
            memberProvider = new MemberProvider(DataManger.getInstance().getRestAdapter()
                    , username, V2EX.getInstance().isSelf(username));
            DataManger.getInstance().addProvider(memberProvider.FILE_NAME, memberProvider);
            DataManger.getInstance().getData(memberProvider.FILE_NAME, memberOnLoadComplete);
        } else if (member != null) {
            initData(member);
        }

    }

    private void initData(Member member) {
        if (member != null) {
            collapsingToolbarLayout.setTitle(member.username);
            name.setText(TextUtils.isEmpty(member.bio) ? getString(R.string.describe_empty) : member.bio);
            Glide
                    .with(this)
                    .load("http:" + member.avatar_large)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .crossFade()
                    .into(icon);
            topicsFragment = TopicsFragment.newInstance(TopicsProvider.TopicType.newTopicTypeByUserName("member_topic_" + member.username, member.username));
            getSupportFragmentManager().beginTransaction().add(R.id.member_fragment, topicsFragment).commit();
        }
    }

    private void initView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        icon = (ImageView) findViewById(R.id.member_icon);
        name = (TextView) findViewById(R.id.member_name);


    }

    private OnLoadCompleteListener<Member> memberOnLoadComplete = new OnLoadCompleteListener<Member>() {
        @Override
        public void loadComplete(Member member) {
            initData(member);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (V2EX.getInstance().isSelf(member == null ? null : member.username)) {
            MenuItem item = menu.add(0, 10086, 1, R.string.logout);
            item.setIcon(R.drawable.ic_exit_to_app_white);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            MenuItem item1 = menu.add(0, 10010, 0, R.string.refresh);
            item1.setIcon(R.drawable.ic_refresh_white);
            item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 10086:
                if (V2EX.getInstance().isNetworkConnected()) {
                    if (UserUtils.logout()) {
                        V2EX.getInstance().toast(R.string.logout_success);
                        finish();
                    }
                } else {
                    V2EX.getInstance().toast(R.string.network_error);
                }
                return true;
            case 10010:
                if (member != null && V2EX.getInstance().isSelf(member.username)) {
                    DataManger.getInstance().refresh(MemberProvider.SElF, new OnLoadCompleteListener<Member>() {
                        @Override
                        public void loadComplete(Member member) {
                            if (member != null) {
                                initData(member);
                                V2EX.getInstance().toast(R.string.refresh_success);
                            } else {
                                V2EX.getInstance().toast(R.string.refresh_error);
                            }
                        }
                    });
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (topicsFragment != null) {
            topicsFragment.setSwipeRefreshEnable(i == 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (member != null && memberProvider != null && V2EX.getInstance().isSelf(member.username)) {
            DataManger.getInstance().removeProvider(memberProvider.FILE_NAME);
        }
    }
}
