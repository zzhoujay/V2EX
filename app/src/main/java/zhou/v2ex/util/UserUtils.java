package zhou.v2ex.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zhou.v2ex.V2EX;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Node;
import zhou.v2ex.net.NetworkManager;

/**
 * Created by zzhoujay on 2015/7/24 0024.
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
                            if (response.code() == 302) {
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

    public static List<Node> getNodesFromResponse(String content) {
        Pattern pattern = Pattern.compile("<a class=\"grid_item\" href=\"/go/([^\"]+)\" id=([^>]+)><div([^>]+)><img src=\"([^\"]+)([^>]+)><([^>]+)></div>([^<]+)");
        Matcher matcher = pattern.matcher(content);
        ArrayList<Node> collections = new ArrayList<>();
        while (matcher.find()) {
            Node node = new Node();
            node.name = matcher.group(1);
            node.title = matcher.group(7);
            node.url = matcher.group(4);
            if (node.url.startsWith("//"))
                node.url = "http:" + node.url;
            else
                node.url = V2EX.SITE_URL + node.url;
            collections.add(node);
        }
        return collections;
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
                            Log.d("xxx", "code:" + response.code());
                            if (response.code() == 302) {
                                flag = true;
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

    public static void collectionNode(String node, final OnLoadCompleteListener<Boolean> onLoadCompleteListener) {
        String url = V2EX.SITE_URL + "/go/" + node;
        Request request = NetworkManager.getInstance().requestBuilder()
                .addHeader("Referer", V2EX.SITE_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(url)
                .get()
                .build();
        NetworkManager.getInstance().requestString(request, new OnLoadCompleteListener<String>() {
            @Override
            public void loadComplete(String s) {
                if (s != null) {
                    String u = getCollectionFromResponse(s);
                    if (u.isEmpty()) {
                        u = getCollectionFromEnResponse(s);
                    }

                    if (!u.isEmpty()) {
                        boolean flag = u.contains("unfavorite");
                        Request r = NetworkManager.getInstance().requestBuilder()
                                .addHeader("Referer", V2EX.SITE_URL)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .url(V2EX.SITE_URL + u)
                                .get()
                                .build();
                        NetworkManager.getInstance().request(r, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.d("collectionNode", "failure", e);
                                onLoadCompleteListener.loadComplete(false);
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                boolean f = false;
                                if (response.code() == 302) {
                                    f = true;
                                }
                                Log.d("collectionNode", (f ? "success" : "error"));
                                onLoadCompleteListener.loadComplete(true);
                            }
                        });
                    } else {
                        Log.d("collectionNode", "url is empty");
                        onLoadCompleteListener.loadComplete(false);
                    }
                } else {
                    Log.d("collectionNode", "s is null");
                    onLoadCompleteListener.loadComplete(false);
                }
            }
        });
    }

    public static String getCollectionFromResponse(String response) {
        Pattern pattern = Pattern.compile("<a href=\"(.*)\">加入收藏</a>");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find())
            return matcher.group(1);

        pattern = Pattern.compile("<a href=\"(.*)\">取消收藏</a>");
        matcher = pattern.matcher(response);
        if (matcher.find())
            return matcher.group(1);

        return "";
    }

    public static String getCollectionFromEnResponse(String response) {
        Pattern pattern = Pattern.compile("<a href=\"(.*)\">Favorite This Node</a>");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find())
            return matcher.group(1);

        pattern = Pattern.compile("<a href=\"(.*)\">Unfavorite</a>");
        matcher = pattern.matcher(response);
        if (matcher.find())
            return matcher.group(1);

        return "";
    }

    public static void collectionTopic(int topicId, final OnLoadCompleteListener<Boolean> onLoadCompleteListener) {
        String url = V2EX.SITE_URL + "/t/" + topicId;
        Request request = NetworkManager.getInstance().requestBuilder()
                .addHeader("Referer", V2EX.SITE_URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url(url)
                .get()
                .build();
        NetworkManager.getInstance().requestString(request, new OnLoadCompleteListener<String>() {
            @Override
            public void loadComplete(String s) {
                if (s != null) {
                    String u = getCollectionFromResponse(s);

                    if (!u.isEmpty()) {
                        u = u.replace("\" class=\"tb", "");
                        Request r = NetworkManager.getInstance().requestBuilder()
                                .addHeader("Referer", V2EX.SITE_URL)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .url(V2EX.SITE_URL + u)
                                .get()
                                .build();
                        NetworkManager.getInstance().request(r, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.d("collectionTopic", "failure", e);
                                onLoadCompleteListener.loadComplete(false);
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                boolean f = false;
                                if (response.code() == 302) {
                                    f = true;
                                }
                                Log.d("collectionTopic", (f ? "success" : "error"));
                                onLoadCompleteListener.loadComplete(f);
                            }
                        });
                    } else {
                        Log.d("collectionTopic", "u is empty");
                        onLoadCompleteListener.loadComplete(false);
                    }
                } else {
                    Log.d("collectionTopic", "s is null");
                    onLoadCompleteListener.loadComplete(false);
                }
            }
        });
    }


}
