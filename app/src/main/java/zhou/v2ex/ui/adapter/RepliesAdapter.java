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
import zhou.v2ex.model.Replies;
import zhou.v2ex.ui.widget.RichText;
import zhou.v2ex.util.TimeUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.Holder> {

    private List<Replies> replies;

    public RepliesAdapter(List<Replies> replies) {
        this.replies = replies;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_replies, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Replies reply = replies.get(position);

        holder.user.setText(reply.member.username);
        holder.time.setText(TimeUtils.friendlyFormat(reply.created * 1000));
        holder.floor.setText(String.format("%d楼", (position + 1)));
        holder.content.setRichText(reply.content_rendered);
        Picasso.with(holder.icon.getContext()).load("http:" + reply.member.avatar_normal)
                .placeholder(R.drawable.default_image).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return replies == null ? 0 : replies.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView user, time, floor;
        public RichText content;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_replies_icon);
            user = (TextView) itemView.findViewById(R.id.item_replies_user);
            time = (TextView) itemView.findViewById(R.id.item_replies_time);
            floor = (TextView) itemView.findViewById(R.id.item_replies_floor);
            content = (RichText) itemView.findViewById(R.id.item_replies_content);
        }
    }

    public void setReplies(List<Replies> replies) {
        this.replies = replies;
        notifyDataSetChanged();
    }
}
