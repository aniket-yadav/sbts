package com.app.sbts.models;

public class Parent {
    String Full_Name;
    String Email;
    String Mobile_No1;

    public String getHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(String hasPaid) {
        this.hasPaid = hasPaid;
    }

    String hasPaid;

    public Parent() {
    }

    public Parent(String full_Name, String email, String mobile_No1, String hasPaid, String student_Name) {
        Full_Name = full_Name;
        Email = email;
        Mobile_No1 = mobile_No1;
        this.hasPaid = hasPaid;
        Student_Name = student_Name;
    }

    String Student_Name;

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

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }
}
