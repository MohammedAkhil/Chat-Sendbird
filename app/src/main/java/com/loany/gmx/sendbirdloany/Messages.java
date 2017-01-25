package com.loany.gmx.sendbirdloany;

import com.orm.SugarRecord;

/**
 * Created by Windows on 1/25/2017.
 */

public class Messages extends SugarRecord {

    String senderId;
    String receiverId;
    String message;


    public Messages(){

    }

    public Messages(String senderId, String receiverId, String message) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }


    public String getMessage(){
        return message;
    }
    public String getSenderId(){
        return senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }

}
