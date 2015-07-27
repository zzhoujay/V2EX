package zhou.v2ex.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import zhou.v2ex.V2EX;
import zhou.v2ex.interfaces.OnLoadCompleteListener;

/**
 * Created by zzhoujay on 2015/7/27 0027.
 */
public class NetworkManager {

    private static NetworkManager networkManager;
    private OkHttpClient client;
    private Handler mainHandler;


    private NetworkManager() {
        client = new OkHttpClient();
        CookieManager manager = new CookieManager(new PersistentCookieStore(V2EX.getInstance()), CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(manager);
        client.setFollowRedirects(false);
        client.getCookieHandler();
        client.setConnectTimeout(3, TimeUnit.SECONDS);
        client.setReadTimeout(3, TimeUnit.SECONDS);
        client.setWriteTimeout(3, TimeUnit.SECONDS);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    public Request.Builder requestBuilder() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7")
                .addHeader("Accept-Language", "zh-CN, en-US")
                .addHeader("Host", "v2ex.com")
                .addHeader("X-Requested-With", "com.android.browser")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.2.1; en-us; M040 Build/JOP40D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        return builder;
    }

    public void request(Request request, final Callback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(request, e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                mainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (callback != null) {
                            try {
                                callback.onResponse(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    public void requestString(Request request, @NonNull final OnLoadCompleteListener<String> loadComplete) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("requestString", "failure", e);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadComplete.loadComplete(null);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("requestString", "success");
                final String str = response.body().string();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadComplete.loadComplete(str);
                    }
                });
            }
        });
    }
}
