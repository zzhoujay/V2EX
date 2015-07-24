package zhou.v2ex.util;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zhou.v2ex.Z2EX;
import zhou.v2ex.data.DataProvider;

/**
 * Created by zzhoujay on 2015/7/24 0024.
 *
 */
public class UserUtils {

    public static void login(final String username, final String password, final DataProvider.OnLoadComplete<Boolean> onLoadComplete) {
        getOnce(new DataProvider.OnLoadComplete<String>() {
            @Override
            public void loadComplete(String s) {
                if (s != null) {
                    RequestBody body = new FormEncodingBuilder()
                            .add("u", username)
                            .add("p", password)
                            .add("next", "/")
                            .add("once", s)
                            .build();
                    Request request = Z2EX.getInstance().getRequestBuilder()
                            .post(body)
                            .url(Z2EX.SINGIN_URL)
                            .build();
                    Z2EX.getInstance().AsynRequest(request, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("login", "failure", e);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            boolean flag = false;
                            if (response.priorResponse() != null && response.priorResponse().code() == 302) {
                                flag = true;
                            }
                            Log.d("login", flag ? "success" : "error");
                            if (onLoadComplete != null) {
                                onLoadComplete.loadComplete(flag);
                            }
                        }
                    });
                }
            }
        });

    }

    public static void getOnce(final DataProvider.OnLoadComplete<String> onLoadComplete) {
        Request request = Z2EX.getInstance().getRequestBuilder().url(Z2EX.SINGIN_URL).get().build();
        Z2EX.getInstance().AsynRequest(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("getOnce", "failure", e);
                if (onLoadComplete != null) {
                    onLoadComplete.loadComplete(null);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (onLoadComplete != null) {
                    onLoadComplete.loadComplete(generateOnce(response.body().string()));
                }
                Log.d("getOnce", "success");
            }
        });
    }

    public static String generateOnce(String content) {
        Pattern pattern = Pattern.compile("<input type=\"hidden\" value=\"([0-9]+)\" name=\"once\" />");
        final Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }
}
