package com.zzhoujay.v2ex;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.zzhoujay.v2ex.data.DataManger;
import com.zzhoujay.v2ex.data.MemberProvider;
import com.zzhoujay.v2ex.interfaces.Notifier;
import com.zzhoujay.v2ex.model.Member;

/**
 * Created by 州 on 2015/7/18 0018.
 * Application
 */
public class V2EX extends Application {

    public static final String SINGIN_URL = "http://v2ex.com/signin";
    public static final String SITE_URL = "http://v2ex.com";


    private static V2EX v2EX;

    private Member self;
    private List<Notifier> selfStateChangeNotifier;

    @Override
    public void onCreate() {
        super.onCreate();
        v2EX = this;
        selfStateChangeNotifier = new ArrayList<>();
        setSelf(MemberProvider.getSelf());
        if (self != null) {
            MemberProvider memberProvider = new MemberProvider(DataManger.getInstance().getRestAdapter(), self.username, true);
            DataManger.getInstance().addProvider(memberProvider.FILE_NAME, memberProvider);
            DataManger.getInstance().refresh(memberProvider.FILE_NAME, null);
        }
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
        for (Notifier notifier : selfStateChangeNotifier) {
            notifier.notice();
        }
    }

    public Member getSelf() {
        return self;
    }

    public boolean isLogin() {
        return self != null;
    }

    public boolean logout() {
        setSelf(null);
        for (Notifier notifier : selfStateChangeNotifier) {
            notifier.notice();
        }
        return MemberProvider.clearSelf();
    }

    @SuppressWarnings("unused")
    public void addSelfChangeNotifier(Notifier notifier) {
        selfStateChangeNotifier.add(notifier);
    }

    public void removeSelfChangeNotifier(Notifier notifier) {
        selfStateChangeNotifier.remove(notifier);
    }

}
