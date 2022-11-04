/*
 * AdapterDataObserver
 *
 * Version: v1.0.0
 *
 * Date: 2022-11-03
 *
 * Copyright notice:
 * This file is part of well-fed.
 *
 * well-fed is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * well-fed is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with well-fed. If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.wellfed.common;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
    private final OnAdapterDataChangedListener listener;

    public interface OnAdapterDataChangedListener {
        void onAdapterDataChanged();
    }

    public AdapterDataObserver(OnAdapterDataChangedListener listener) {
        this.listener = listener;
    }

    @Override public void onChanged() {
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

    @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }
}
