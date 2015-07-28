package zhou.v2ex.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zhou.v2ex.R;
import zhou.v2ex.interfaces.ClickCallback;
import zhou.v2ex.interfaces.OnItemClickListener;
import zhou.v2ex.model.Topic;
import zhou.v2ex.util.TimeUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 * Topic列表Adapter
 */
public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.Holder> {

    private List<Topic> topics;
    private ClickCallback<Topic> clickCallback;

    public TopicsAdapter(@Nullable List<Topic> topics) {
        this.topics = topics;
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClicked(View view, int position) {
            Topic topic = topics.get(position);
            if (clickCallback != null) {
                clickCallback.callback(topic);
            }
        }
    };

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, null);
        Holder holder = new Holder(view);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Topic topic = topics.get(position);

        holder.title.setText(topic.title);
        holder.node.setText(topic.node.title);
        holder.user.setText(topic.member.username);
        holder.time.setText(TimeUtils.friendlyFormat(topic.created * 1000));
        holder.reply.setText(String.format("%d个回复", topic.replies));
        Picasso.with(holder.icon.getContext()).load("http:" + topic.member.avatar_normal)
                .placeholder(R.drawable.default_image).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return topics == null ? 0 : topics.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView title, node, user, time, reply;
        public ImageView icon;

        private View parent;
        private OnItemClickListener onItemClickListener;

        public Holder(View itemView) {
            super(itemView);
            parent = itemView;
            title = (TextView) itemView.findViewById(R.id.item_topic_title);
            node = (TextView) itemView.findViewById(R.id.item_topic_node);
            user = (TextView) itemView.findViewById(R.id.item_topic_user);
            time = (TextView) itemView.findViewById(R.id.item_topic_time);
            icon = (ImageView) itemView.findViewById(R.id.item_topic_icon);
            reply = (TextView) itemView.findViewById(R.id.item_topic_reply);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(parent, getAdapterPosition());
                    }
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public void setClickCallback(ClickCallback<Topic> clickCallback) {
        this.clickCallback = clickCallback;
    }
}
