package com.atguigu.jdbc;

import java.sql.Date;

public class Customer {

    private int id;
    private String name;
    private String email;
    private java.util.Date birth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public java.util.Date getBirth() {
        return birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Customer(int id, String name, String email, Date birth) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public Customer() {

    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email=" + email
                + ", birth=" + birth + "]";
    }

}
