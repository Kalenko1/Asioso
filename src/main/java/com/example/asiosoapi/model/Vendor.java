package com.example.asiosoapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vendor {

    private Long id;
    private String name;

    @JsonProperty("self_link")
    private String selfLink;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }
}