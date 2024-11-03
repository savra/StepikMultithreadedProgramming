package com.hvdbs.savra.solution.Chapter4.Unit2.enums;

public enum DressingRoomState {
    EMPTY("пустая"),
    OCCUPIED_BY_MAN("мужская"),
    OCCUPIED_BY_WOMAN("женская");

    DressingRoomState(String title) {
        this.title = title;
    }

    private final String title;

    public String getTitle() {
        return title;
    }
}
