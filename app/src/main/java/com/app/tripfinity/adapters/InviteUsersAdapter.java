package com.app.tripfinity.adapters;

import static com.app.tripfinity.utils.HelperClass.displayRoundImageFromUrl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewholders.InviteUserViewHolder;

import java.util.ArrayList;
import java.util.List;

public class InviteUsersAdapter extends RecyclerView.Adapter<InviteUserViewHolder> {

    List<User> users;
    Context context;

    public InviteUsersAdapter(ArrayList<User> users, Context context){
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public InviteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_user_list_view, parent, false);
        return new InviteUserViewHolder(view);
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
