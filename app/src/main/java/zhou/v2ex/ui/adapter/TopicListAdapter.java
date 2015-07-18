package zhou.v2ex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zhou.v2ex.R;
import zhou.v2ex.model.Topic;

/**
 * Created by 州 on 2015/7/18 0018.
 */
public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.Holder> {

    private List<Topic> topics;

    public TopicListAdapter(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, null);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Topic topic = topics.get(position);

        holder.title.setText(topic.content);
        holder.node.setText(topic.node.name);
        holder.user.setText(topic.member.username);
        holder.time.setText("" + topic.created);
        Picasso.with(holder.icon.getContext()).load("http:" + topic.member.avatar_normal).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 : topics.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView title, node, user, time;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_topic_icon);
            title = (TextView) itemView.findViewById(R.id.item_topic_title);
            node = (TextView) itemView.findViewById(R.id.item_topic_node);
            user = (TextView) itemView.findViewById(R.id.item_topic_user);
            time = (TextView) itemView.findViewById(R.id.item_topic_time);
        }
    }
}
