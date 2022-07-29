package com.id.ace.models;

public class MQQueue {
    QueueType type = QueueType.LOCAL;
    String name = "";

    public QueueType getType() {
        return type;
    }

    public void setType(QueueType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersistent() {
        return persistent;
    }

    public void setPersistent(String persistent) {
        this.persistent = persistent;
    }

    public String getBackoutqueue() {
        return backoutqueue;
    }

    public void setBackoutqueue(String backoutqueue) {
        this.backoutqueue = backoutqueue;
    }

    String target = "";
    String description = "";
    String persistent = "";
    String backoutqueue = "";

    public enum QueueType {LOCAL, ALIAS, REMOTE, MODEL};


}
