package zhou.v2ex;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import zhou.v2ex.model.Member;
import zhou.v2ex.util.FileUtils;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Z2EX extends Application {

    public static final String HOT_TOPIC_NAME = "hot.cache";
    public static final String LATEST_TOPIC_NAME = "latest.cache";
    public static final String NODE_NAME = "node.cache";

    public static final String SINGIN_URL = "https://v2ex.com/signin";


    private static Z2EX z2EX;

    private Member self;
    private OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        z2EX = this;
    }

    public static Z2EX getInstance() {
        return z2EX;
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
        return false;
    }

    public OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            client.setCookieHandler(cookieManager);
            client.setConnectTimeout(3, TimeUnit.SECONDS);
            client.setWriteTimeout(3, TimeUnit.SECONDS);
            client.setReadTimeout(3, TimeUnit.SECONDS);
        }

        return client;
    }

    public Request.Builder getRequestBuilder() {

        Request.Builder builder = new Request.Builder()
                .addHeader("Origin", SINGIN_URL)
                .addHeader("Referer", "http://v2ex.com/signin")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7")
                .addHeader("Accept-Language", "zh-CN, en-US")
                .addHeader("Host", "v2ex.com")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");

        return builder;
    }

    public void AsynRequest(Request request, Callback callback) {
        getClient().newCall(request).enqueue(callback);
    }

}
