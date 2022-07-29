package com.id.ace.models;

public class MQSubscription {
    String name = "";
    String topicstring = "";
    String destination = "";
    String description = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopicstring() {
        return topicstring;
    }

    public void setTopicstring(String topicstring) {
        this.topicstring = topicstring;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
