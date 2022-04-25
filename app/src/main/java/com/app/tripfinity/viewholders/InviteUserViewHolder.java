package com.app.tripfinity.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;

public class InviteUserViewHolder extends RecyclerView.ViewHolder {
    public TextView user;
    public TextView userEmail;
    public ImageView imageView;

    public InviteUserViewHolder(@NonNull View itemView) {
        super(itemView);
        user = itemView.findViewById(R.id.user_list_item);
        userEmail = itemView.findViewById(R.id.user_list_email);
        imageView = itemView.findViewById(R.id.image_profile);
    }
}
