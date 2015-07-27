package zhou.v2ex.data;

import android.util.Log;

import java.io.File;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import zhou.v2ex.R;
import zhou.v2ex.V2EX;
import zhou.v2ex.interfaces.MemberService;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Member;
import zhou.v2ex.util.FileUtils;

/**
 * Created by zzhoujay on 2015/7/22 0022.
 */
public class MemberProvider implements DataProvider<Member> {

    public static final String SElF = "self.cache";

    public String FILE_NAME = "member_";

    private Member member;
    private MemberService memberService;
    private boolean isSelf;
    private String username;

    public MemberProvider(RestAdapter restAdapter, String username, boolean self) {
        this.isSelf = self;
        this.username = username;
        memberService = restAdapter.create(MemberService.class);
        FILE_NAME = isSelf ? SElF : (FILE_NAME + username);
    }

    @Override
    public void persistence() {
        if (hasLoad()) {
            new Thread() {
                @Override
                public void run() {
                    File file = new File(V2EX.getInstance().getCacheDir(), FILE_NAME);
                    FileUtils.writeObject(file, member);
                }
            }.start();
        }
    }

    @Override
    public Member get() {
        return member;
    }

    @Override
    public void set(Member member) {
        this.member = member;
    }

    @Override
    public void getFromLocal(OnLoadCompleteListener<Member> loadComplete) {
        File file = new File(V2EX.getInstance().getCacheDir(), FILE_NAME);
        Member m = null;
        if (file.exists()) {
            try {
                m = (Member) FileUtils.readObject(file);
            } catch (Exception e) {
                Log.d("getFromLocal", "error", e);
            }
        }
        if (loadComplete != null) {
            loadComplete.loadComplete(m);
        }
    }

    @Override
    public void getFromNet(final OnLoadCompleteListener<Member> loadComplete) {
        if (!V2EX.getInstance().isNetworkConnected()) {
            //网络未连接
            V2EX.getInstance().toast(R.string.network_error);
            if (loadComplete != null) {
                loadComplete.loadComplete(null);
            }
            return;
        }
        memberService.getMember(username, new Callback<Member>() {
            @Override
            public void success(Member member, Response response) {
                Log.d("getFromNet", "success");
                if (loadComplete != null) {
                    loadComplete.loadComplete(member);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("getFromNet", "failure", error);
                if (loadComplete != null) {
                    loadComplete.loadComplete(null);
                }
            }
        });
    }

    @Override
    public boolean hasLoad() {
        return member != null;
    }

    @Override
    public boolean needCache() {
        return isSelf || V2EX.getInstance().saveCache();
    }

    public static Member getSelf() {
        File file = new File(V2EX.getInstance().getCacheDir(), SElF);
        Member self = null;
        if (file.exists()) {
            try {
                self = (Member) FileUtils.readObject(file);
            } catch (Exception e) {
                Log.d("getSelf", "error", e);
            }
        }
        return self;
    }
}
