package com.izhar.admintip.objects;

public class Request {
    String id, phone, photo, time;

    public Request() {
    }

    public Request(String id, String phone, String photo, String time) {
        this.id = id;
        this.phone = phone;
        this.photo = photo;
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public String getTime() {
        return time;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}
