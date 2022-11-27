package com.xffffff.wellfed.common;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xffffff.wellfed.R;

import java.util.List;

public class SortingFragment extends Fragment {

    private List<String> sortOptions;
    private List<String> readableSortOptions;
    private OnSortClick listener;

    public void setOptions(List<String> sortingOptions, List<String> readableSortOptions) {
        this.sortOptions = sortingOptions;
        this.readableSortOptions = readableSortOptions;
    }

    public void setListener(OnSortClick listener) {
        this.listener = listener;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    @Override public void onViewCreated(@NonNull View view,
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
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        listener.onClick(sortOptions.get((int) id));
                    }
                });

        imageFilterButton.setOnClickListener(v -> {
            popupWindow.show();
        });
    }

    public interface OnSortClick {
        public void onClick(String field);
    }
}
