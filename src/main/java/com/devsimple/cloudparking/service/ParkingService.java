package com.devsimple.cloudparking.service;

import com.devsimple.cloudparking.entity.Parking;
import com.devsimple.cloudparking.exceptions.ParkingNotFoundException;
import com.devsimple.cloudparking.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository repository;

    public static final int ONE_HOUR = 60;
    public static final int TWENTY_FOUR_HOUR = 24 * ONE_HOUR;
    public static final double ONE_HOUR_VALUE = 5.0;
    public static final double ADDITIONAL_PER_HOUR_VALUE = 3;
    public static final double DAY_VALUE = 30;

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional(readOnly = true)
    public Parking findById(String id) {
        return repository.findById(id).orElseThrow(() -> new ParkingNotFoundException("Parking not found"));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Parking> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Parking save(Parking parking) {
        String uuid = getUUID();
        parking.setId(uuid);
        parking.setEntryDate(LocalDateTime.now());
        return repository.save(parking);
    }

    @Transactional
    public Parking update(String id, Parking parkingCreate) {
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parking.setModel(parkingCreate.getModel());
        repository.save(parking);
        return parking;
    }

    @Transactional
    public void delete(String id) {
        Parking parking = findById(id);
        repository.delete(parking);
    }

    @Transactional
    public Parking checkOut(String id){
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(getBill(parking.getEntryDate(), parking.getExitDate()));
        return repository.save(parking);
    }

    private Double getBill(LocalDateTime entryDate, LocalDateTime exitDate) {
        long minutes = entryDate.until(exitDate, ChronoUnit.MINUTES);
        Double bill = 0.0;
        if (minutes <= ONE_HOUR) {
            return ONE_HOUR_VALUE;
        }
        if (minutes <= TWENTY_FOUR_HOUR) {
            bill = ONE_HOUR_VALUE;
            int hours = (int) (minutes / ONE_HOUR);
            for (int i = 0; i < hours; i++) {
                bill += ADDITIONAL_PER_HOUR_VALUE;
            }
            return bill;
        }
        int days = (int) (minutes / TWENTY_FOUR_HOUR);
        for (int i = 0; i < days; i++) {
            bill += DAY_VALUE;
        }
        return bill;
    }
}
