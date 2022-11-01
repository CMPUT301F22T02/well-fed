package com.example.wellfed.common;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
    private OnAdapterDataChangedListener listener;

    public interface OnAdapterDataChangedListener {
        void onAdapterDataChanged();
    }

    public AdapterDataObserver(OnAdapterDataChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        listener.onAdapterDataChanged();
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount,
                                             @Nullable Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        listener.onAdapterDataChanged();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }

    @Override public void onItemRangeMoved(int fromPosition, int toPosition,
                                           int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        listener.onAdapterDataChanged();
    }

    @Override public void onItemRangeRemoved(int positionStart,
                                             int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }
}
