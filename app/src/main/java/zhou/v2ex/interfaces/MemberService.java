package zhou.v2ex.interfaces;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import zhou.v2ex.model.Member;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public interface MemberService {
    @GET("/api/members/show.json")
    void getMember(@Query("username") String username, Callback<Member> memberCallback);
}
