package zhou.v2ex.model;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Member implements Serializable{
    public int id;
    public String username;
    public String website;
    public String twitter;
    public String location;
    public String tagline;
    public String bio;
    public String avatar_mini;
    public String avatar_normal;
    public String avatar_large;
    public long created;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                ", twitter='" + twitter + '\'' +
                ", location='" + location + '\'' +
                ", tagline='" + tagline + '\'' +
                ", bio='" + bio + '\'' +
                ", avatar_mini='" + avatar_mini + '\'' +
                ", avater_normal='" + avatar_normal + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                ", created=" + created +
                '}';
    }
}
