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

package com.xffffff.wellfed.common;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The AdapterDataObserver class extends RecyclerView.AdapterDataObserver to
 * provide a listener for changes in RecyclerView.Adapter data.
 */
public class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
    /**
     * Holds the listener for changes in the RecyclerView.Adapter data
     */
    private final OnAdapterDataChangedListener listener;

    /**
     * The OnDataChangedListener interface requires a handler for changes in
     * RecyclerView.Adapter data.
     */
    public interface OnAdapterDataChangedListener {
        /**
         * Handler for changes in RecyclerView.Adapter data.
         */
        void onAdapterDataChanged();
    }

    /**
     * Constructs AdapterDataObserver
     *
     * @param listener the OnAdapterDataChangedListener
     */
    public AdapterDataObserver(OnAdapterDataChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Notifies the listener that the RecyclerView.Adapter data has changed
     */
    @Override public void onChanged() {
        super.onChanged();
        listener.onAdapterDataChanged();
    }

    /**
     * Notifies the listener that the RecyclerView.Adapter data has changed
     *
     * @param positionStart the start position
     * @param itemCount    the item count
     */
    @Override public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }

    /**
     * Notifies the listener that the RecyclerView.Adapter data has changed
     *
     * @param positionStart the start position
     * @param itemCount    the item count
     * @param payload      the payload
     */
    @Override public void onItemRangeChanged(int positionStart, int itemCount,
                                             @Nullable Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        listener.onAdapterDataChanged();
    }

    /**
     * Notifies the listener that the RecyclerView.Adapter data has changed
     *
     * @param positionStart the start position
     * @param itemCount the item count
     */
    @Override public void onItemRangeInserted(int positionStart,
                                              int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }

    /**
     * Notifies the listener that the RecyclerView.Adapter data has changed
     *
     * @param fromPosition the from position
     * @param toPosition   the to position
     * @param itemCount    the item count
     */
    @Override public void onItemRangeMoved(int fromPosition, int toPosition,
                                           int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        listener.onAdapterDataChanged();
    }

    /**
     * Notifies the listener that the RecyclerView.Adapter data has changed
     *
     * @param positionStart the start position
     * @param itemCount     the item count
     */
    @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        listener.onAdapterDataChanged();
    }
}
