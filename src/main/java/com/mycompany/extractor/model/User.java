/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.extractor.model;

/**
 *
 * @author user
 */
public class User {

    private long id;
    private String name;
    private String location;

    public User(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
//        String data_format = "{id:%d,name:\"%s\",location:\"%s\"}";
        String data_format = "%d,%s,%s%n";
//        String data_format = "%d~~\"%s\"~~\"%s\"";
        return String.format(data_format, id, name, location);
    }
}
