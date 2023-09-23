package com.krypt.bluecoin.User;

public  class UserModel {
    String dCreated;
    public String username;
    public String password;
    public String email;
    public String phone;
    public String fname;
    public String sname;
    public String status;
    public static  String userID;
    public String gender;

    public UserModel()
    {

    }

    public UserModel(String dCreated, String username, String password, String email, String phone, String fname, String sname, String status, String userID, String gender) {
        this.dCreated = dCreated;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.fname = fname;
        this.sname = sname;
        this.status = status;
        UserModel.userID = userID;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getdCreated() {
        return dCreated;
    }

    public void setdCreated(String dCreated) {
        this.dCreated = dCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
