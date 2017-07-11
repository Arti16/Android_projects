package com.example.shivmn.firebaselogin;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by SHIVM@n on 7/8/2017.
 */


@IgnoreExtraProperties
public class messageModel {

    public String username;
    public String message;
    public long messageTime;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public messageModel() {
    }

    public messageModel(String username, String message) {
        this.username = username;
        this.message = message;
        messageTime = new Date().getTime();
    }
    public String getMessageText() {
        return message;
    }

    public void setMessageText(String message) {
        this.message = message;
    }

    public String getMessageUser() {
        return username;
    }

    public void setMessageUser(String username) {
        this.username = username;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

