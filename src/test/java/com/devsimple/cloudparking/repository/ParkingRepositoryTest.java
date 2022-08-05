package com.devsimple.cloudparking.repository;

import com.devsimple.cloudparking.entity.Parking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Parking Repository")
@ActiveProfiles("test")
public class ParkingRepositoryTest {

    @Autowired
    private ParkingRepository repository;

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Parking parkingCreate() {
        Parking parking = new Parking(getUUID(),
                "License MT-01",
                "SP",
                "BMW 320i GP",
                "Azul",
                LocalDateTime.of(2022, 8, 5, 12, 20),
                LocalDateTime.of(2022, 8, 5, 14, 00),
                20.0);
        return parking;
    }

    @Test
    void findById_ShouldReturnParkingWhenSuccess() {
        Parking parking = parkingCreate();
        Parking save = repository.save(parking);
        Optional<Parking> byId = repository.findById(save.getId());

        assertThat(byId).isNotNull();
        assertThat(byId.get().getState()).isEqualTo("SP");
    }

    @Test
    void findById_NotShouldReturnNothing_WhenSuccess() {
        Optional<Parking> byId = repository.findById("Not found");

        assertThat(byId).isEmpty();
    }

    @Test
    void save_ShouldCreateParking_WhenSuccess() {
        Parking parking = parkingCreate();
        Parking save = repository.save(parking);

        assertThat(save).isNotNull();
        assertThat(save.getId()).isNotNull();
        assertThat(save.getId()).isEqualTo(parking.getId());
        assertThat(save.getModel()).isEqualTo(parking.getModel());
        assertThat(save.getLicense()).isEqualTo(parking.getLicense());
        assertThat(save.getState()).isEqualTo(parking.getState());
        assertThat(save.getEntryDate()).isEqualTo(parking.getEntryDate());
        assertThat(save.getExitDate()).isEqualTo(parking.getExitDate());
    }

    @Test
    void update_ShouldReturnParkingUpdate_WhenSuccessful(){
        Parking parking = parkingCreate();
        Parking parkingUpdate = repository.save(parking);

        parkingUpdate.setModel("Modelo");
        parkingUpdate.setColor("Branco");
        parkingUpdate.setLicense("Ops");
        parkingUpdate.setExitDate(LocalDateTime.now());
        parkingUpdate.setEntryDate(LocalDateTime.now());
        parkingUpdate.setBill(10.0);
        parkingUpdate.setState("PI");
        Parking update = repository.save(parkingUpdate);

        assertThat(update).isEqualTo(parkingUpdate);
        assertThat(update.getModel()).isEqualTo(parkingUpdate.getModel());
        assertThat(update.getState()).isEqualTo(parkingUpdate.getState());
        assertThat(update.getId()).isEqualTo(parkingUpdate.getId());
        assertThat(update.getLicense()).isEqualTo(parkingUpdate.getLicense());
        assertThat(update.getColor()).isEqualTo(parkingUpdate.getColor());
    }

    @Test
    void delete_ShouldDeleteParking_WhenSuccessful(){
        Parking parking = parkingCreate();
        Parking save = repository.save(parking);
        repository.delete(save);
        Optional<Parking> byId = repository.findById(save.getId());

        assertThat(byId).isEmpty();
    }

}