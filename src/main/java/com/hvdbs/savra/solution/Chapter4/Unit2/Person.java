package com.hvdbs.savra.solution.Chapter4.Unit2;

import com.hvdbs.savra.solution.Chapter4.Unit2.enums.Sex;

public class Person {
    private final Sex sex;
    private final String name;

    public Person(Sex sex, String name) {
        this.sex = sex;
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }
}
