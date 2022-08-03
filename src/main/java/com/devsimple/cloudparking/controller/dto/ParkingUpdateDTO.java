package com.devsimple.cloudparking.controller.dto;

public class ParkingUpdateDTO {

    private String color;

    public ParkingUpdateDTO() {
    }

    public ParkingUpdateDTO(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
