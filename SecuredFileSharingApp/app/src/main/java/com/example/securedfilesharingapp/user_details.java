package com.example.securedfilesharingapp;

public class user_details {
    public String fname,lname, email, pass;
    public  String  phone, address, gender;

    public user_details(){

    }
    public user_details(String fname,String lname,String  email,String  pass,String  phone,String  address,String  gender){
        this.fname=fname;
        this.lname=lname;
        this.email=email;
        this.pass=pass;

        this.phone=phone;
        this.address=address;
        this.gender=gender;


    }

}
