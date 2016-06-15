package com.zzhoujay.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 * Member的模型类
 */
public class Member implements Serializable, Parcelable {

    public static final String MEMBER = "member";
    public static final String MEMBER_NAME = "member_name";

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.website);
        dest.writeString(this.twitter);
        dest.writeString(this.location);
        dest.writeString(this.tagline);
        dest.writeString(this.bio);
        dest.writeString(this.avatar_mini);
        dest.writeString(this.avatar_normal);
        dest.writeString(this.avatar_large);
        dest.writeLong(this.created);
    }

    public Member() {
    }

    protected Member(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.website = in.readString();
        this.twitter = in.readString();
        this.location = in.readString();
        this.tagline = in.readString();
        this.bio = in.readString();
        this.avatar_mini = in.readString();
        this.avatar_normal = in.readString();
        this.avatar_large = in.readString();
        this.created = in.readLong();
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
