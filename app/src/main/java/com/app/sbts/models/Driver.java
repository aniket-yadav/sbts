package com.app.sbts.models;

public class Driver {
    String Full_Name;
    String Email;
    String Mobile_No1;

    public Driver(String full_Name, String email, String mobile_No1, String bus_No) {
        Full_Name = full_Name;
        Email = email;
        Mobile_No1 = mobile_No1;
        Bus_No = bus_No;
    }

    public Driver() {
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile_No1() {
        return Mobile_No1;
    }

    public void setMobile_No1(String mobile_No1) {
        Mobile_No1 = mobile_No1;
    }

    public String getBus_No() {
        return Bus_No;
    }

    public void setBus_No(String bus_No) {
        Bus_No = bus_No;
    }

    String Bus_No;
}
