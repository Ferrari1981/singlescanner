package com.sous.scanner.MODEL;

import java.util.LinkedHashMap;

public class SubClassPojo {
    String address;
    String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap getLinkedHashMap() {
        return linkedHashMap;
    }

    public void setLinkedHashMap(LinkedHashMap linkedHashMap) {
        this.linkedHashMap = linkedHashMap;
    }

    LinkedHashMap<String,Object> linkedHashMap=new LinkedHashMap();

    public SubClassPojo(String address, String name, LinkedHashMap linkedHashMap) {
        this.address = address;
        this.name = name;
        this.linkedHashMap = linkedHashMap;
    }
}
