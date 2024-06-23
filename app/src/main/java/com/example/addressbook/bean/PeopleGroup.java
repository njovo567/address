package com.example.addressbook.bean;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeopleGroup extends LitePalSupport implements Serializable {
    @Column(unique = true)
    private long id;
    private String name;
    private List<People> peopleList;


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

    public List<People> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<People> peopleList) {
       this.peopleList = peopleList;
    }
}