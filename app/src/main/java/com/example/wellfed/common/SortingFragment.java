package com.example.wellfed.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;

import com.example.wellfed.R;

import java.util.List;

public class SortingFragment extends Fragment {

    private List<String> sortOptions;
    private OnSortClick listener;

    public void setOptions(List<String> sortingOptions) {
        this.sortOptions = sortingOptions;
    }

    public void setListener(OnSortClick listener){
        this.listener = listener;
    }

    public interface OnSortClick{
        public void onClick(String field);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageFilterButton imageFilterButton = view.findViewById(R.id.image_filter_button);
        ListPopupWindow popupWindow = new ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle);
        popupWindow.setAnchorView(imageFilterButton);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter(requireContext(), R.layout.list_popup_window_item, sortOptions);
        popupWindow.setWidth(400);
        popupWindow.setAdapter(sortAdapter);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onClick(sortOptions.get((int) id));
            }
        });

        imageFilterButton.setOnClickListener(v->{popupWindow.show();});
    }
}
