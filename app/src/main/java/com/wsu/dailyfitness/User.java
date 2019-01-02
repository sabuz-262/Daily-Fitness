package com.wsu.dailyfitness;

public class User {


    private String fullName;
    private String userName;
    private String email;
    private String password;

    public User(String fullname, String username, String email, String password){
        this.fullName = fullname;
        this.userName = username;
        this.email = email;
        this.password = password;
    }

    public String getFullName(){
        return this.fullName;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }

}
