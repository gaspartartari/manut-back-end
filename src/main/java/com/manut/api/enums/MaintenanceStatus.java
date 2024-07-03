package com.manut.api.enums;

public enum MaintenanceStatus {
    ON_TIME("em dia", "green"),
    ATTENTION("atenção", "yellow"),
    IN_PROGRESS("em progresso", "blue"),
    LATE("atrasada", "red");

    private final String label;
    private final String color;

    MaintenanceStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
