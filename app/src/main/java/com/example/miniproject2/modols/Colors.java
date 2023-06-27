package com.example.miniproject2.modols;



public class Colors {
    private int id;
    private String name;
    private String code;

    public Colors(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Colors(String name,String code) {
        this.name = name;
        this.code = code;
    }
    @Override
    public String toString() {
        return String.format("name %s code %s",name,code);
    }

}

