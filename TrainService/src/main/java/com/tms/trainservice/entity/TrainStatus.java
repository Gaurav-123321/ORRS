package com.tms.trainservice.entity;

public enum TrainStatus {
    ON_TIME,
    DELAYED,
    CANCELLED;

    public static TrainStatus getDefault() {
        return ON_TIME;
    }
}

