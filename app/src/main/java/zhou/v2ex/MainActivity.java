package zhou.v2ex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.blueware.agent.android.BlueWare;
import com.squareup.picasso.Picasso;

import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.TopicsProvider;
import zhou.v2ex.interfaces.Notifier;
import zhou.v2ex.model.Member;
import zhou.v2ex.ui.activity.LoginActivity;
import zhou.v2ex.ui.activity.MemberActivity;
import zhou.v2ex.ui.activity.NodesActivity;
import zhou.v2ex.ui.dialog.ContentDialog;
import zhou.v2ex.ui.fragment.TopicsFragment;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private TopicsFragment[] fragments;
    private ImageView icon;
    private TextView name, bio;
    private View header;
    private DrawerLayout drawerLayout;
    private ContentDialog about;

    private int[] ids = {R.string.tab1, R.string.tab2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BlueWare.withApplicationToken("36DF853CC86E94516EC02E282C2DF70809").start(this.getApplication());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        fragments = new TopicsFragment[2];
        fragments[0] = TopicsFragment.newInstance(TopicsProvider.TopicType.LATEST);
        fragments[1] = TopicsFragment.newInstance(TopicsProvider.TopicType.HOT);

        initView();
        initData();
        initEvent();

    }

    private void initEvent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_tab2:
                        Intent intent = new Intent(MainActivity.this, NodesActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.drawer_about:
                        about = ContentDialog.newInstance(getString(R.string.about), getString(R.string.about_content));
                        about.show(getSupportFragmentManager(), "about");
                        return true;
                }
                return false;
            }
        });
    }

    private void initData() {
        tabLayout.addTab(tabLayout.newTab().setText(ids[0]));
        tabLayout.addTab(tabLayout.newTab().setText(ids[1]));

        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(ids[position]);
            }
        };

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPage);

        header = findViewById(R.id.header);
        icon = (ImageView) findViewById(R.id.header_icon);
        name = (TextView) findViewById(R.id.header_name);
        bio = (TextView) findViewById(R.id.header_bio);

    }

    private void setUserInfo(Member member) {
        if (member == null) {
            name.setText(R.string.login);
            icon.setImageResource(R.mipmap.ic_launcher);
            bio.setText("");
            return;
        }
        name.setText(member.username);
        bio.setText(member.bio);
        Picasso.with(this).load("http:" + member.avatar_large).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(icon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Member user = V2EX.getInstance().getSelf();
        if (user != null) {
            setUserInfo(user);
            name.setOnClickListener(userInfoListener);
            header.setOnClickListener(userInfoListener);
            icon.setOnClickListener(userInfoListener);
        } else {
            setUserInfo(null);
            icon.setOnClickListener(loginListener);
            name.setOnClickListener(loginListener);
        }
    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener userInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MemberActivity.class);
            intent.putExtra(Member.MEMBER, (Parcelable) V2EX.getInstance().getSelf());
            startActivity(intent);
        }
    };

    private Notifier notifier = new Notifier() {
        @Override
        public void notice() {
            Member user = V2EX.getInstance().getSelf();
            if (user != null) {
                setUserInfo(user);
                name.setOnClickListener(null);
                header.setOnClickListener(userInfoListener);
            } else {
                name.setOnClickListener(loginListener);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataManger.getInstance().removeProvider(TopicsProvider.TopicType.FILE_NAME_HOT);
        DataManger.getInstance().removeProvider(TopicsProvider.TopicType.FILE_NAME_LATEST);
        V2EX.getInstance().removeSelfChangeNotifier(notifier);
    }
}
