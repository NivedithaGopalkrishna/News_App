package com.example.newsapp;

import java.io.Serializable;

public class Source implements Serializable {
    String name;
    String id;

    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
