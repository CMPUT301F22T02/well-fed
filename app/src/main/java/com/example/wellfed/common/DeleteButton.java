package com.example.wellfed.common;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public class DeleteButton implements View.OnClickListener {
    private final DeleteDialog deleteDialog;

    public DeleteButton(Context context, Button button, String title,
                        OnDeleteListener onDeleteListener) {
        button.setOnClickListener(this);
        this.deleteDialog = new DeleteDialog(context, onDeleteListener, title);
    }

    @Override
    public void onClick(View v) {
        this.deleteDialog.show();
    }
}
