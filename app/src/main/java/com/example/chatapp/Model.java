package com.example.chatapp;

public class Model {

    String name,username;
    String imgurl;
    String Uid;


    public Model() {

    }

    public Model( String name, String username,String imgurl,String uid) {
        this.name = name;
        this.username = username;
        this.imgurl = imgurl;
        Uid = uid;


    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
