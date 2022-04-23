package com.app.tripfinity.adapters;

import static com.app.tripfinity.utils.Constants.MESSAGE_RECEIVED_VIEW;
import static com.app.tripfinity.utils.Constants.MESSAGE_SENT_VIEW;
import static com.app.tripfinity.utils.HelperClass.displayRoundImageFromUrl;
import static com.app.tripfinity.utils.HelperClass.getCurrentUser;
import static com.app.tripfinity.utils.HelperClass.getParsedTimestamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Message;

import java.util.List;

// inspired by tutorial: https://sendbird.com/developer/tutorials/android-chat-tutorial-building-a-messaging-ui
public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Message> mMessageList;

    public MessageListAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        if (message.getSenderEmail().equals(getCurrentUser().getEmail())) {
            // If the current user is the sender of the message
            return MESSAGE_SENT_VIEW;
        } else {
            // If some other user sent the message
            return MESSAGE_RECEIVED_VIEW;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == MESSAGE_SENT_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_bubble_current_user, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == MESSAGE_RECEIVED_VIEW) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_bubble_other_user, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case MESSAGE_SENT_VIEW:
                ((SentMessageHolder) holder).bind(message);
                break;
            case MESSAGE_RECEIVED_VIEW:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());
            timeText.setText(getParsedTimestamp(message.getTimestamp()));
            nameText.setText(message.getSenderName());
            displayRoundImageFromUrl(mContext, message.getSenderPhotoUrl(), profileImage, R.drawable.placeholderprofileimage);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());
            timeText.setText(getParsedTimestamp(message.getTimestamp()));
        }
    }
}