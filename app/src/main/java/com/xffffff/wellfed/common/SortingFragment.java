package com.xffffff.wellfed.common;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xffffff.wellfed.R;

import java.util.List;

/**
 * The SortingFragment class is a fragment that displays a list of
 * sorting options for the user to select from.
 */
public class SortingFragment extends Fragment {
    /**
     * The list of sorting options
     */
    private List<String> sortOptions;
    /**
     * the onSortClick listener for the fragment
     */
    private OnSortClick listener;
    /**
     * The list of readable sorting options
     */
    private List<String> readableSortOptions;
    
    /**
     * setOptions sets the list of sorting options
     *
     * @param sortingOptions the list of sorting options
     * @param readableSortOptions the list of readable sorting options
     */
    public void setOptions(List<String> sortingOptions, List<String> readableSortOptions) {
        this.sortOptions = sortingOptions;
        this.readableSortOptions = readableSortOptions;
    }

    /**
     * setListener sets the onSortClick listener for the fragment
     *
     * @param listener the onSortClick listener
     */
    public void setListener(OnSortClick listener) {
        this.listener = listener;
    }

    /**
     * onCreateView inflates the view
     *
     * @param inflater           the inflater
     * @param container          the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    /**
     * onViewCreated sets up the view
     *
     * @param view               the view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button imageFilterButton = view.findViewById(R.id.image_filter_button);
        ListPopupWindow popupWindow =
            new ListPopupWindow(requireContext(), null,
                androidx.appcompat.R.attr.listPopupWindowStyle);
        popupWindow.setAnchorView(imageFilterButton);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter(requireContext(),
                R.layout.list_popup_window_item, readableSortOptions);
        popupWindow.setWidth(400);
        popupWindow.setAdapter(sortAdapter);

        popupWindow.setOnItemClickListener(
            (parent, view1, position, id) -> listener.onClick(sortOptions.get((int) id)));

        imageFilterButton.setOnClickListener(v -> popupWindow.show());
    }

    /**
     * OnSortClick is the interface for the onSortClick listener
     */
    public interface OnSortClick {
        void onClick(String field);
    }
}
