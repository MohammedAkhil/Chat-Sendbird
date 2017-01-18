package com.loany.gmx.sendbirdloany;

/**
 * Created by Windows on 1/17/2017.
 */

public class Message {

    String _id;
    String _message;

    public Message(){}

    public Message(String id, String message) {
        this._id = id;
        this._message = message;
    }

    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getMessage(){
        return this._message;
    }

    // setting name
    public void set_Message(String message){
        this._message = message;
    }



}
