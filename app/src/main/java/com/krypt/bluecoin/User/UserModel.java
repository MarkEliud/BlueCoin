package com.krypt.bluecoin.User;

public class UserModel {
    String username;
    String pass;
    String email;
    String phone;

//    public UserModel(String username, String pass, String email, String phone) {
//        this.username = username;
//        this.pass = pass;
//        this.email = email;
//        this.phone = phone;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
