package com.app.tripfinity.model;

import com.app.tripfinity.repository.MessagingRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.type.DateTime;

public class Message {
    private String content;
    private String senderEmail;
    private String senderName;
    private String senderPhotoUrl;
    private Timestamp timestamp;

    public Message(String content, String senderEmail, String senderName, String senderPhotoUrl, Timestamp timestamp) {
        this.content = content;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.senderPhotoUrl = senderPhotoUrl;
        this.timestamp = timestamp;
    }

    public Message() {}

    public String getSenderEmail() {
        return senderEmail;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getSenderPhotoUrl() {
        return senderPhotoUrl;
    }

    public String getSenderName() {
        return senderName;
    }
}
