package com.app.tripfinity.model;

import androidx.annotation.Keep;

import java.io.Serializable;

public class Topic implements Serializable {
    private String topicName;
    private String topicType;

    public Topic(String topicName, String topicType) {
        this.topicName = topicName;
        this.topicType = topicType;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicType() {
        return topicType;
    }
}
