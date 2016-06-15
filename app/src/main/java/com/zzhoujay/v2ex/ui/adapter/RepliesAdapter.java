package com.zzhoujay.v2ex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.interfaces.OnItemClickListener;
import com.zzhoujay.v2ex.model.Replies;
import com.zzhoujay.v2ex.util.ContentUtils;
import com.zzhoujay.v2ex.util.TimeUtils;

/**
 * Created by 州 on 2015/7/20 0020.
 * 回复列表Adapter
 */
public class RepliesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Replies> replies;
    private OnItemClickListener iconClickCallback;
    private OnItemClickListener itemClickCallback;

    public RepliesAdapter(List<Replies> replies) {
        this.replies = replies;
    }


    private OnItemClickListener onIconClickListener = new OnItemClickListener() {
        @Override
        public void onItemClicked(View view, int position) {
            if (iconClickCallback != null) {
                iconClickCallback.onItemClicked(view, position);
            }
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClicked(View view, int position) {
            if (itemClickCallback != null) {
                itemClickCallback.onItemClicked(view, position);
            }
        }
    };

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_replies, null);
        Holder holder = new Holder(view);
        holder.setIconClickListener(onIconClickListener);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h instanceof Holder) {
            Holder holder= (Holder) h;
            Replies reply = replies.get(position);
            holder.user.setText(reply.member.username);
            holder.time.setText(TimeUtils.friendlyFormat(reply.created * 1000));
            holder.floor.setText(String.format("%d楼", (position + 1)));
            RichText.from(ContentUtils.formatContent(reply.content_rendered)).into(holder.content);
            Glide
                    .with(holder.icon.getContext())
                    .load("http:" + reply.member.avatar_normal)
                    .placeholder(R.drawable.default_image)
                    .crossFade()
                    .centerCrop()
                    .into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return replies == null ? 0 : replies.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView user, time, floor;
        public TextView content;

        private OnItemClickListener iconClickListener;
        private OnItemClickListener onItemClickListener;

        public Holder(final View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_replies_icon);
            user = (TextView) itemView.findViewById(R.id.item_replies_user);
            time = (TextView) itemView.findViewById(R.id.item_replies_time);
            floor = (TextView) itemView.findViewById(R.id.item_replies_floor);
            content = (TextView) itemView.findViewById(R.id.item_replies_content);

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iconClickListener != null) {
                        iconClickListener.onItemClicked(icon, getLayoutPosition());
                        getAdapterPosition();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(itemView, getAdapterPosition());
                    }
                }
            });
        }

        public void setIconClickListener(OnItemClickListener iconClickListener) {
            this.iconClickListener = iconClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    public Replies getItem(int position) {
        return replies == null ? null : replies.get(position);
    }

    public void setReplies(List<Replies> replies) {
        this.replies = replies;
        notifyDataSetChanged();
    }

    public void setIconClickCallback(OnItemClickListener iconClickCallback) {
        this.iconClickCallback = iconClickCallback;
    }

    public void setItemClickCallback(OnItemClickListener itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }
}
