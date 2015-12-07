package com.cmu.android.juicychatcomp.DB;

/**
 * message when user chat
 * Created by qiuzhexin on 12/6/15.
 */
public class Message {
    public Group group;
    public String message;
    public String owner;
    public long createTime;

    public Message() {
    }

    public Message(Group group, String message, String owner, long createTime) {
        this.group = group;
        this.message = message;
        this.owner = owner;
        this.createTime = createTime;
    }
}
