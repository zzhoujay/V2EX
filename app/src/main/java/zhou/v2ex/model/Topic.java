package zhou.v2ex.model;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Topic implements Serializable {
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
}
