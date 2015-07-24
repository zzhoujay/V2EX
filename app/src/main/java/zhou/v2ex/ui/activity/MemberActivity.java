package zhou.v2ex.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import zhou.v2ex.R;
import zhou.v2ex.Z2EX;
import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.DataProvider;
import zhou.v2ex.data.MemberProvider;
import zhou.v2ex.data.TopicsProvider;
import zhou.v2ex.model.Member;
import zhou.v2ex.ui.fragment.ContentFragment;
import zhou.v2ex.ui.fragment.TopicsFragment;

/**
 * Created by zzhoujay on 2015/7/22 0022.
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
                    , username, Z2EX.getInstance().isSelf(username));
            DataManger.getInstance().addProvider(memberProvider.FILE_NAME, memberProvider);
            DataManger.getInstance().getData(memberProvider.FILE_NAME, memberOnLoadComplete);
        } else if (member != null) {
            initData(member);
        }

    }

    private void initData(Member member) {
        if (member != null) {
//            name.setText(member.username);
            collapsingToolbarLayout.setTitle(member.username);
            Picasso.with(this).load("http:" + member.avatar_large).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(icon);
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

    private DataProvider.OnLoadComplete<Member> memberOnLoadComplete = new DataProvider.OnLoadComplete<Member>() {
        @Override
        public void loadComplete(Member member) {
            initData(member);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        topicsFragment.setSwipeRefreshEnable(i == 0);
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
        if (member != null && Z2EX.getInstance().isSelf(member.username)) {
            DataManger.getInstance().removeProvider(memberProvider.FILE_NAME);
        }
    }
}
