package com.hvdbs.savra.solution.Chapter4.Unit2.enums;

public enum Sex {
    MALE("мужчина"),
    FEMALE("женщина");

    Sex(String title) {
        this.title = title;
    }

    private final String title;

    public String getTitle() {
        return title;
    }
}
