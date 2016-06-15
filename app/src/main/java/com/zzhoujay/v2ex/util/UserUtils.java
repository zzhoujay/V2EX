package com.zzhoujay.v2ex.util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.interfaces.OnLoadCompleteListener;
import com.zzhoujay.v2ex.net.NetworkManager;
import com.zzhoujay.v2ex.net.PersistentCookieStore;
import com.zzhoujay.v2ex.ui.activity.LoginActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zzhoujay on 2015/7/24 0024.
 * UserUtils
 */
public class UserUtils {

    public static void login(final String username, final String password, @NonNull final OnLoadCompleteListener<Boolean> onLoadComplete) {
        getOnce(new OnLoadCompleteListener<String>() {
            @Override
            public void loadComplete(String s) {
                if (s != null) {
                    final RequestBody body = new FormEncodingBuilder()
                            .add("u", username)
                            .add("p", password)
                            .add("once", s)
                            .add("next", "/")
                            .build();
                    Request request = NetworkManager.getInstance().requestBuilder()
                            .addHeader("Origin", V2EX.SINGIN_URL)
                            .addHeader("Referer", "http://v2ex.com/signin")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .url(V2EX.SINGIN_URL)
                            .post(body)
                            .build();
                    NetworkManager.getInstance().request(request, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("login", "failure", e);
                            onLoadComplete.loadComplete(false);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            boolean flag = false;
                            if (response.code() >=200&&response.code()<400) {
                                flag = true;
                            }
                            Log.d("login", (flag ? "success" : "failure"));
                            onLoadComplete.loadComplete(flag);
                        }
                    });
                } else {
                    Log.d("login", "error once is null");
                    onLoadComplete.loadComplete(false);
                }
            }
        });
    }

    public static void getOnce(@NonNull final OnLoadCompleteListener<String> loadCompleteListener) {
        Request.Builder builder = NetworkManager.getInstance().requestBuilder();
        Request request = builder.url(V2EX.SINGIN_URL).get().build();
        NetworkManager.getInstance().requestString(request, new OnLoadCompleteListener<String>() {
            @Override
            public void loadComplete(String s) {
                loadCompleteListener.loadComplete(generateOnce(s));
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


    public static void replyTopic(int topicId, final String content, @NonNull final OnLoadCompleteListener<Boolean> loadCompleteListener) {
        final String url = V2EX.SITE_URL + "/t/" + topicId;
        getOnce(new OnLoadCompleteListener<String>() {
            @Override
            public void loadComplete(String s) {
                if (s != null) {
                    RequestBody body = new FormEncodingBuilder()
                            .add("content", content)
                            .add("once", s)
                            .build();
                    Request request = NetworkManager.getInstance().requestBuilder()
                            .addHeader("Origin", V2EX.SITE_URL)
                            .addHeader("Referer", url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .url(url)
                            .build();
                    NetworkManager.getInstance().request(request, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("replyTopic", "failure", e);
                            loadCompleteListener.loadComplete(false);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            boolean flag = false;
                            System.out.println(response.body().string());
                            Log.d("reply", "code:" + response.code());
                            if (response.code() == 302) {
                                flag = true;
                            } else if (response.code() == 403) {
                                V2EX.getInstance().toast(R.string.identity_time_out);
                                Intent intent = new Intent(V2EX.getInstance(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                V2EX.getInstance().startActivity(intent);
                                loadCompleteListener.loadComplete(false);
                                return;
                            }
                            Log.d("replyTopic", (flag ? "success" : "error"));
                            loadCompleteListener.loadComplete(flag);
                        }
                    });
                } else {
                    Log.d("replyTopic", "get once failure");
                    loadCompleteListener.loadComplete(false);
                }
            }
        });
    }


    public static void createTopic(String nodeName, final String title, final String content, @NonNull final OnLoadCompleteListener<Boolean> onLoadCompleteListener) {
        final String url = V2EX.SITE_URL + "/new/" + nodeName;
        getOnce(new OnLoadCompleteListener<String>() {
            @Override
            public void loadComplete(String s) {
                if (s != null) {
                    RequestBody body = new FormEncodingBuilder()
                            .add("once", s)
                            .add("title", title)
                            .add("content", content)
                            .build();
                    Request request = NetworkManager.getInstance().requestBuilder()
                            .addHeader("Origin", V2EX.SITE_URL)
                            .addHeader("Referer", url)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .url(url)
                            .build();
                    NetworkManager.getInstance().request(request, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("createTopic", "failure", e);
                            onLoadCompleteListener.loadComplete(false);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            boolean flag = false;
                            if (response.code() == 302) {
                                flag = true;
                            } else if (response.code() == 403) {
                                V2EX.getInstance().toast(R.string.identity_time_out);
                                Intent intent = new Intent(V2EX.getInstance(), LoginActivity.class);
                                V2EX.getInstance().startActivity(intent);
                                onLoadCompleteListener.loadComplete(false);
                                return;
                            }
                            Log.d("createTopic", (flag ? "success" : "error"));
                            onLoadCompleteListener.loadComplete(flag);
                        }
                    });
                } else {
                    Log.d("createTopic", "get once failure");
                    onLoadCompleteListener.loadComplete(false);
                }
            }
        });
    }


    public static boolean logout() {
        PersistentCookieStore cookieStore = new PersistentCookieStore(V2EX.getInstance());
        cookieStore.removeAll();
        return V2EX.getInstance().logout();
    }


}
