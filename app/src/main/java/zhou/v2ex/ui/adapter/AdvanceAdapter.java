package zhou.v2ex.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzhoujay on 2015/7/21 0021.
 * 带有头部和尾部的适配于RecyclerView的Adapter
 */
public class AdvanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEAD_START = Integer.MIN_VALUE;
    public static final int FOOT_START = Integer.MIN_VALUE + 100;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter;
    private List<View> headers, footers;

    /**
     * 通过这个Adapter去包含真正显示内容的Adapter来实现添加头部和尾部的目的
     * 注意：被包裹的Adapter的getItemViewType方法返回的值不能大于Integer.MAX_VALUE/2
     *
     * @param innerAdapter 被被包裹的Adapter（用来显示真正的RecyclerView中的数据的）
     */
    public AdvanceAdapter(RecyclerView.Adapter innerAdapter) {
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
            innerAdapter.onBindViewHolder(holder, position - headCount);
        }
    }

    @Override
    public int getItemCount() {
        return headers.size() + footers.size() + innerAdapter.getItemCount();
    }

    /**
     * 获取元素的类型
     * 注意：被包裹的Adapter的这个方法的返回值必须小于Integer.MAX_VALUE/2,否则可能发生溢出
     *
     * @param position 元素的位置
     * @return type
     */
    @Override
    public int getItemViewType(int position) {
        int innerCount = innerAdapter.getItemCount();
        int headCount = headers.size();
        if (headCount > position) {
            return HEAD_START + position;
        } else if (headCount <= position && headCount + innerCount > position) {
            return innerAdapter.getItemViewType(position - headCount) + Integer.MAX_VALUE / 2;//此处可能发生溢出
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
            notifyItemRangeChanged(fromPosition + headCount, toPosition + headCount + itemCount);
        }
    };
}
