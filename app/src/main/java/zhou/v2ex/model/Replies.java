package zhou.v2ex.model;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Replies implements Serializable{
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
}
