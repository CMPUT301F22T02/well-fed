package com.xffffff.wellfed.common;

/**
 * This interface is used to handle the result of DB operation
 */
public interface OnCompleteListener<T> {
    /**
     * Called when a DB operation is completed
     *
     * @param item    the object
     * @param success true if the operation was successful,
     *                false otherwise.
     */
    void onComplete(T item, Boolean success);
}