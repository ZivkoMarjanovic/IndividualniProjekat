package com.tus.individual.project.model;

public enum StatusEnum {
    PLACED("PLACED"),
    UPDATED("UPDATED"),
    PAID("PAID"),
    SERVED("SERVED"),
    COLLECTED("COLLECTED");
    private final String status;


    StatusEnum(String status) {
        this.status = status;
    }
}
