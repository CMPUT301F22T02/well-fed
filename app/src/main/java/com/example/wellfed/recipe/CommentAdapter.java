package com.example.wellfed.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Viewholder> {
    List<String> comments;

    CommentAdapter(List<String> comments){
        this.comments = comments;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView comment;
        public Button deleteBtn;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            comment = itemView.findViewById(R.id.comment_textView);
            deleteBtn = itemView.findViewById(R.id.comment_delete_btn);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recipeView = layoutInflater.inflate(R.layout.recipe_comments_edit, parent, false);
        Viewholder viewHolder = new Viewholder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Viewholder holder, int position) {
        TextView comment = holder.comment;
        Button deleteBtn = holder.deleteBtn;

        comment.setText(comments.get(holder.getAdapterPosition()));
        deleteBtn.setOnClickListener(view -> {
            comments.remove(holder.getAdapterPosition());
            this.notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
