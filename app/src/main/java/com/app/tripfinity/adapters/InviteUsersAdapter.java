package com.app.tripfinity.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.listeners.RemoveInviteClickListener;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewholders.InviteUserViewHolder;

import java.util.List;

public class InviteUsersAdapter extends RecyclerView.Adapter<InviteUserViewHolder> {

    List<User> users;
    RemoveInviteClickListener listener;

    public InviteUsersAdapter(List<User> users, RemoveInviteClickListener listener){
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InviteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_user_list_view, parent, false);
        return new InviteUserViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteUserViewHolder holder, int position) {
        holder.user.setText(users.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
