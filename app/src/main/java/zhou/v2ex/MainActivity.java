package zhou.v2ex;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.TopicsProvider;
import zhou.v2ex.ui.activity.NodesActivity;
import zhou.v2ex.ui.fragment.TopicsFragment;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private TopicsFragment[] fragments;

    private int[] ids = {R.string.tab1, R.string.tab2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setLogo(R.mipmap.ic_launcher);
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
                switch (menuItem.getItemId()) {
                    case R.id.drawer_tab2:
                        Intent intent = new Intent(MainActivity.this, NodesActivity.class);
                        startActivity(intent);
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
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataManger.getInstance().removeProvider(TopicsProvider.TopicType.FILE_NAME_HOT);
        DataManger.getInstance().removeProvider(TopicsProvider.TopicType.FILE_NAME_LATEST);
    }
}
