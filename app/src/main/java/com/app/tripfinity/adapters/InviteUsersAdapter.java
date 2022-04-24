package com.app.tripfinity.adapters;

import static com.app.tripfinity.utils.HelperClass.displayRoundImageFromUrl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.listeners.RemoveInviteClickListener;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewholders.InviteUserViewHolder;

import java.util.ArrayList;
import java.util.List;

public class InviteUsersAdapter extends RecyclerView.Adapter<InviteUserViewHolder> {

    List<User> users;
    RemoveInviteClickListener listener;
    Context context;

    public InviteUsersAdapter(ArrayList<User> users, RemoveInviteClickListener listener, Context context){
        this.users = users;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public InviteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_user_list_view, parent, false);
        return new InviteUserViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteUserViewHolder holder, int position) {
        User user = users.get(position);
        if(user.getName() != null && !user.getName().equals("")){
            holder.user.setText(user.getName());
            holder.userEmail.setText(user.getEmail());
        }
        else {
            holder.user.setText(user.getEmail());
        }
        displayRoundImageFromUrl(context, user.getUserPhotoUrl(), holder.imageView, R.drawable.placeholderprofileimage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
