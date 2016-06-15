package com.zzhoujay.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 * 回复的模型类
 */
public class Replies implements Serializable, Parcelable {
    public int id;
    public int thanks;
    public String content;
    public String content_rendered;
    public Member member;
    public long created;
    public long last_modified;

    @Override
    public String toString() {
        return "Replies{" +
                "id=" + id +
                ", thanks=" + thanks +
                ", content='" + content + '\'' +
                ", content_rendered='" + content_rendered + '\'' +
                ", member=" + member +
                ", created=" + created +
                ", last_modified=" + last_modified +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.thanks);
        dest.writeString(this.content);
        dest.writeString(this.content_rendered);
        dest.writeSerializable(this.member);
        dest.writeLong(this.created);
        dest.writeLong(this.last_modified);
    }

    public Replies() {
    }

    protected Replies(Parcel in) {
        this.id = in.readInt();
        this.thanks = in.readInt();
        this.content = in.readString();
        this.content_rendered = in.readString();
        this.member = (Member) in.readSerializable();
        this.created = in.readLong();
        this.last_modified = in.readLong();
    }

    public static final Parcelable.Creator<Replies> CREATOR = new Parcelable.Creator<Replies>() {
        public Replies createFromParcel(Parcel source) {
            return new Replies(source);
        }

        public Replies[] newArray(int size) {
            return new Replies[size];
        }
    };
}
