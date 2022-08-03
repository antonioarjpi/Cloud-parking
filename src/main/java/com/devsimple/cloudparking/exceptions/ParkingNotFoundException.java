package com.devsimple.cloudparking.exceptions;

public class ParkingNotFoundException extends RuntimeException {
    public ParkingNotFoundException(String msg) {
        super(msg);
    }
}
