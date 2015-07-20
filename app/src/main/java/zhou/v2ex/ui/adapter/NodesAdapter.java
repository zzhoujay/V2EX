package zhou.v2ex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zhou.v2ex.R;
import zhou.v2ex.interfaces.ClickCallback;
import zhou.v2ex.interfaces.OnItemClickListener;
import zhou.v2ex.model.Node;

/**
 * Created by 州 on 2015/7/20 0020.
 */
public class NodesAdapter extends RecyclerView.Adapter<NodesAdapter.Holder> {

    private List<Node> nodes;
    private ClickCallback<Node> clickCallback;

    public NodesAdapter(List<Node> nodes) {
        this.nodes = nodes;
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClicked(View view, int position) {
            Node node = nodes.get(position);
            if (clickCallback != null) {
                clickCallback.callback(node);
            }
        }
    };


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_node, null);
        Holder holder = new Holder(view);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Node node = nodes.get(position);

        holder.title.setText(node.title);
        holder.content.setText(node.header);
        holder.num.setText(node.topics + "个主题");
    }

    @Override
    public int getItemCount() {
        return nodes == null ? 0 : nodes.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView title, content, num;

        private View parent;
        private OnItemClickListener onItemClickListener;

        public Holder(View itemView) {
            super(itemView);
            parent = itemView;
            title = (TextView) itemView.findViewById(R.id.item_node_title);
            content = (TextView) itemView.findViewById(R.id.item_node_content);
            num = (TextView) itemView.findViewById(R.id.item_node_num);

            parent.setOnClickListener(new View.OnClickListener() {
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

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
        notifyDataSetChanged();
    }

    public void setClickCallback(ClickCallback<Node> clickCallback) {
        this.clickCallback = clickCallback;
    }
}
