package com.example.myapplication;

public class MsgModel {
    String msg,userUid;
    long timeStamp;

    public MsgModel(){}

    public MsgModel(String msg,long timeStamp,String userUid){
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.userUid = userUid;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setTimeStamp(long timeStamp){
        this.timeStamp = timeStamp;
    }
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setUserUid(String userUid){
        this.userUid = userUid;
    }
    public String getUserUid(){
        return userUid;
    }
}
