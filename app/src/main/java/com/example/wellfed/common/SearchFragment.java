package com.example.wellfed.common;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wellfed.R;
import com.google.android.material.textfield.TextInputEditText;

public class SearchFragment extends Fragment {

    private OnTextChange onTextChange;

    public interface OnTextChange{
        void onTextChange(String newText);
    }

    public void setOnTextChange(OnTextChange listener){
        onTextChange = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Search bar
        TextInputEditText searchBar = view.findViewById(R.id.ingredient_storage_search);
        // Clear search bar
        ImageView crossIcon = view.findViewById(R.id.clear_search);
        crossIcon.setOnClickListener(v -> searchBar.setText(""));
        // On search bar text change show "Functionality not implemented yet"
        // message
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    crossIcon.setVisibility(View.VISIBLE);
                } else {
                    crossIcon.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextChange.onTextChange(s.toString());
            }
        });
    }
}

