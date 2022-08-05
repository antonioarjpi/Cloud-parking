package com.devsimple.cloudparking.service;

import com.devsimple.cloudparking.entity.Parking;
import com.devsimple.cloudparking.exceptions.ParkingNotFoundException;
import com.devsimple.cloudparking.repository.ParkingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Parking Service")
public class ParkingServiceTest {

    @SpyBean
    private ParkingService service;

    @MockBean
    private ParkingRepository repository;

    public static Parking parkingCreate() {
        Parking parking = new Parking("ID",
                "License MT-01",
                "SP",
                "BMW 320i GP",
                "Azul",
                LocalDateTime.of(2022, 8, 5, 12, 20),
                null,
                null);
        return parking;
    }

    @BeforeEach
    void setUp() {
        List<Parking> parkings = new ArrayList<>(List.of(parkingCreate()));

        BDDMockito.when(repository.findAll(ArgumentMatchers.any(Example.class)))
                .thenReturn(parkings);
        BDDMockito.when(repository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(parkingCreate()));
        BDDMockito.when(repository.save(ArgumentMatchers.any(Parking.class)))
                .thenReturn(parkingCreate());
    }

    @Test
    void findAll_ShouldReturnListOfParking_WhenSuccessful() {
        List<Parking> parkings = service.findAll();

        assertThat(parkings).isNotNull();
    }

    @Test
    void findById_ReturnParkingById_WhenSuccessful() {
        String id = parkingCreate().getId();
        Parking parking = service.findById(id);

        assertThat(parking).isNotNull();
    }

    @Test
    void findById_ReturnNotFoundId_WhenSFailure() {
        String id = parkingCreate().getId();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        catchThrowableOfType(() -> service.findById(id), ParkingNotFoundException.class);
    }

    @Test
    void save_CreateParking_WhenSuccessful() {
        Parking parkingCreate = service.save(parkingCreate());

        assertThat(parkingCreate).isNotNull();
        assertThat(parkingCreate.getId()).isEqualTo(parkingCreate().getId());
        assertThat(parkingCreate.getModel()).isEqualTo(parkingCreate.getModel());
    }

    @Test
    void update_ShouldUpdateParking_WhenSuccessful(){
        Parking parking = parkingCreate();
        Parking save = repository.save(parking);
        save.setColor("Branco");
        save.setModel("Novo modelo");
        Parking update = service.update(save.getId(), save);

        assertThat(update).isNotNull();
        assertThat(update.getColor()).isEqualTo(save.getColor());
        assertThat(update.getModel()).isEqualTo(save.getModel());
    }

    @Test
    void delete_ErrorDeleteNotFoundParking_WhenFailure() {
        Parking parking = parkingCreate();

        catchThrowableOfType(() -> service.delete(parking.getId()), NullPointerException.class);

        Mockito.verify(repository, Mockito.never()).delete(parking);
    }

    @Test
    void checkOut_getBillValueForParking_WhenSuccessful() {
        Parking parking = parkingCreate();
        parking.setBill(10.0);
        parking.setExitDate(LocalDateTime.now());
        BDDMockito.when(repository.save(ArgumentMatchers.any(Parking.class)))
                .thenReturn(parking);
        Parking save = service.checkOut(parking.getId());

        assertThat(save.getExitDate()).isNotNull();
        assertThat(save.getBill()).isNotNull();
    }

}
