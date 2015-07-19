package zhou.v2ex.model;

import java.io.Serializable;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class Node implements Serializable{
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
}
