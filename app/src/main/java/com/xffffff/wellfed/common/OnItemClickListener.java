package com.xffffff.wellfed.common;

/**
 * OnItemClickListener interface. The listener for an item click in the
 * RecyclerView.
 *
 * @param <Item> The type of item in the ArrayList.
 */
public interface OnItemClickListener<Item> {
    /**
     * Called on item click
     * @param item the item that was clicked
     */
    void onItemClick(Item item);
}
