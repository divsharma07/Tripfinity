package com.app.tripfinity.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.listeners.RemoveInviteClickListener;

public class InviteUserViewHolder extends RecyclerView.ViewHolder {
    public TextView user;
    public ImageButton delete;
    public InviteUserViewHolder(@NonNull View itemView, RemoveInviteClickListener listener) {
        super(itemView);
        user = itemView.findViewById(R.id.user_list_item);
        delete = itemView.findViewById(R.id.remove_user);
        delete.setOnClickListener(v-> {
            if(listener != null){
                int position = getLayoutPosition();
                if (position != RecyclerView.NO_POSITION) {

                    listener.onRemoveClicked(position);
                }
            }
        });
    }
}
