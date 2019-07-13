package com.example.bobol.practice;

import java.io.Serializable;

/**
 * Created by bobol on 2018-01-19.
 */
//Serializable은 intent하기 위한 패키징??정도로 이해
public class FriendsItems  implements Serializable {


    public  String lastName;
    public  String firstName;
    public  String mobile;
    public  String emailAdd;
    public  int _id;
    public  String domain;
    public  String address;
   // int no;


    public FriendsItems(int _id, String lastName, String firstName, String mobile,String emailAdd,String domain,String address) {
        this.domain= domain;
        this._id= _id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.mobile = mobile;
        this.emailAdd= emailAdd;
        this.address = address;
    }


    public String getAddress(){return address;}

    public void setAddress(String address){this.address=address;}

    public String getDomain(){return domain;}

    public void setDomain(String domain){this.domain=domain;}

    public int get_id(){return _id;}

    public void set_id(int position){this._id=_id;}

    public String getEmailAdd(){return emailAdd;}

    public void setEmailAdd(String emailAdd){this.emailAdd=emailAdd;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String full_name(){return lastName+firstName;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }





}
