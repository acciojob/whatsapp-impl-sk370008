package com.driver;

public class User {
    private String name;
    private String mobile;

    public User(String name,String mobile){
        this.name = name;
        this.mobile = mobile;
    }

    public String getName(){
        return this.name;
    }

    public String getMobile(){return this.mobile;}
}
