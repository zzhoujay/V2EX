package zhou.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Node implements Serializable, Parcelable {

    public static final String NODE = "node";

    public int id;
    public String name;
    public String url;
    public String title;
    public String title_alternative;
    public int topics;
    public String header;
    public String footer;
    public long created;

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", title_alternative='" + title_alternative + '\'' +
                ", topics=" + topics +
                ", num='" + header + '\'' +
                ", footer='" + footer + '\'' +
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
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.title_alternative);
        dest.writeInt(this.topics);
        dest.writeString(this.header);
        dest.writeString(this.footer);
        dest.writeLong(this.created);
    }

    public Node() {
    }

    protected Node(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        this.title = in.readString();
        this.title_alternative = in.readString();
        this.topics = in.readInt();
        this.header = in.readString();
        this.footer = in.readString();
        this.created = in.readLong();
    }

    public static final Parcelable.Creator<Node> CREATOR = new Parcelable.Creator<Node>() {
        public Node createFromParcel(Parcel source) {
            return new Node(source);
        }

        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
