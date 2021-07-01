package com.izhar.admintip.objects;

public class User {
    String uid, name, phone, email, image, account_status, expire_date, password;

    public User() {
    }

    public User(String uid, String name, String phone, String email, String image, String account_status, String expire_date, String password) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.account_status = account_status;
        this.expire_date = expire_date;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getAccount_status() {
        return account_status;
    }

    public String getExpire_date() {
        return expire_date;
    }
}
