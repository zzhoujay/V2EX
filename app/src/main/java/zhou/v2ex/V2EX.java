package zhou.v2ex;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import zhou.v2ex.data.MemberProvider;
import zhou.v2ex.model.Member;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class V2EX extends Application {

    public static final String HOT_TOPIC_NAME = "hot.cache";
    public static final String LATEST_TOPIC_NAME = "latest.cache";
    public static final String NODE_NAME = "node.cache";

    public static final String SINGIN_URL = "http://v2ex.com/signin";
    public static final String SITE_URL = "http://v2ex.com";


    private static V2EX v2EX;

    private Member self;
    private boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        v2EX = this;
        setSelf(MemberProvider.getSelf());
    }

    public static V2EX getInstance() {
        return v2EX;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }


    public boolean saveCache() {
        return false;
    }

    public boolean isSelf(String username) {
        return self != null && self.username.equals(username);
    }

    public void setSelf(Member member) {
        this.self = member;
    }

    public Member getSelf() {
        return self;
    }

    public boolean isLogin() {
        return self != null;
    }


}
