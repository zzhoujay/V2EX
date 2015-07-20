package zhou.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Topic implements Serializable, Parcelable {

    public static final String TOPIC = "topic";

    public int id;
    public String title;
    public String url;
    public String content;
    public String content_rendered;
    public int replies;
    public Member member;
    public Node node;
    public long created;
    public long lastModified;
    public long lastTouched;


    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", content_rendered='" + content_rendered + '\'' +
                ", replies=" + replies +
                ", member=" + member +
                ", node=" + node +
                ", created=" + created +
                ", lastModified=" + lastModified +
                ", lastTouched=" + lastTouched +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.content);
        dest.writeString(this.content_rendered);
        dest.writeInt(this.replies);
        dest.writeSerializable(this.member);
        dest.writeSerializable(this.node);
        dest.writeLong(this.created);
        dest.writeLong(this.lastModified);
        dest.writeLong(this.lastTouched);
    }

    public Topic() {
    }

    protected Topic(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.content = in.readString();
        this.content_rendered = in.readString();
        this.replies = in.readInt();
        this.member = (Member) in.readSerializable();
        this.node = (Node) in.readSerializable();
        this.created = in.readLong();
        this.lastModified = in.readLong();
        this.lastTouched = in.readLong();
    }

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
