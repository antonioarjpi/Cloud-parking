package com.devsimple.cloudparking.service;

import com.devsimple.cloudparking.entity.Parking;
import com.devsimple.cloudparking.exceptions.ParkingNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private static Map<String, Parking> parkingMap = new HashMap();

    static {
        var id = getUUID();
        Parking parking = new Parking();
        parking.setId(id);
        parking.setModel("Sandero");
        parking.setState("PI");
        parking.setColor("Branco");
        parking.setLicense("MT-1221");
        parkingMap.put(id, parking);

        var id2 = getUUID();
        Parking parking2 = new Parking();
        parking2.setId(id2);
        parking2.setModel("GOL");
        parking2.setState("MA");
        parking2.setColor("PRATA");
        parking2.setLicense("MT-1000");
        parkingMap.put(id2, parking2);
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Parking findById(String id) {
        Parking parking = parkingMap.get(id);
        if (parking == null) {
            throw new ParkingNotFoundException("Parking not found");
        }
        return parking;
    }

    public List<Parking> findAll() {
        return parkingMap.values().stream().collect(Collectors.toList());
    }

    public Parking save(Parking parking) {
        String uuid = getUUID();
        parking.setId(uuid);
        parking.setEntryDate(LocalDateTime.now());
        parkingMap.put(uuid, parking);
        return parking;
    }

    public Parking update(String id, Parking parkingCreate) {
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parkingMap.replace(id, parking);
        return parking;
    }

    public void delete(String id) {
        findById(id);
        parkingMap.remove(id);
    }
}
