package zhou.v2ex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 州 on 2015/7/21 0021.
 */
public class AdapterWithHeadAndFoot extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEAD_START = Integer.MIN_VALUE;
    public static final int FOOT_START = Integer.MIN_VALUE + 100;

    private RecyclerView.Adapter innerAdapter;
    private List<View> headers, footers;

    public AdapterWithHeadAndFoot(RecyclerView.Adapter innerAdapter) {
        headers = new ArrayList<>();
        footers = new ArrayList<>();
        setInnerAdapter(innerAdapter);
    }

    public void addHeader(View view) {
        headers.add(view);
    }

    public void addFooter(View view) {
        footers.add(view);
    }

    public void removeHeader(View view) {
        headers.remove(view);
    }

    public void removeFooter(View view) {
        footers.remove(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int headCount = headers.size();
        if (viewType < HEAD_START + headCount) {
            return new Holder(headers.get(viewType - HEAD_START));
        } else if (viewType >= FOOT_START && viewType < Integer.MAX_VALUE / 2) {
            return new Holder(footers.get(viewType - FOOT_START));
        } else {
            return innerAdapter.onCreateViewHolder(parent, viewType - Integer.MAX_VALUE / 2);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headCount = headers.size();
        if (position >= headCount && position < headCount + innerAdapter.getItemCount()) {
            innerAdapter.onBindViewHolder(holder, position-headCount);
        }
    }

    @Override
    public int getItemCount() {
        return headers.size() + footers.size() + innerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        int innerCount = innerAdapter.getItemCount();
        int headCount = headers.size();
        if (headCount > position) {
            return HEAD_START + position;
        } else if (headCount <= position && headCount + innerCount > position) {
            return innerAdapter.getItemViewType(position - headCount) + Integer.MAX_VALUE / 2;
        } else {
            return FOOT_START + position - headCount - innerCount;
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }

    public void setInnerAdapter(RecyclerView.Adapter adapter) {
        if (innerAdapter != null) {
            notifyItemRangeRemoved(headers.size(), innerAdapter.getItemCount());
            innerAdapter.unregisterAdapterDataObserver(dataObserver);
        }
        this.innerAdapter = adapter;
        innerAdapter.registerAdapterDataObserver(dataObserver);
        notifyItemRangeInserted(headers.size(), innerAdapter.getItemCount());
    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + headers.size(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + headers.size(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + headers.size(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            int headCount = headers.size();
            // TODO: No notifyItemRangeMoved method?
            notifyItemRangeChanged(fromPosition + headCount, toPosition + headCount + itemCount);
        }
    };
}
