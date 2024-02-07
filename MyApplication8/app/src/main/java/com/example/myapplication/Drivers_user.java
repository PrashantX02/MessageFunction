package com.example.myapplication;
public class Drivers_user {
    String imageUri,mail,name,passcode,userId,status;
    public Drivers_user() {
        // Initialize default values or leave it empty if not required
    }

    public Drivers_user(String id, String n, String m, String p, String imageUri, String s){
        this.userId = id;
        this.name = n;
        this.mail = m;
        this.passcode = p;
        this.imageUri = imageUri;
        this.status = s;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String uri) {
        this.imageUri = uri;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
