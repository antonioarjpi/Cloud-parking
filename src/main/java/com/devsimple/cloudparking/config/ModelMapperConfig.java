package com.devsimple.cloudparking.config;

import com.devsimple.cloudparking.controller.dto.ParkingDTO;
import com.devsimple.cloudparking.entity.Parking;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelMapperConfig {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public ParkingDTO parkingDTO(Parking parking){
        return MODEL_MAPPER.map(parking, ParkingDTO.class);
    }

    public List<ParkingDTO> parkingDTOlist(List<Parking> parkings){
        return parkings.stream().map(this::parkingDTO).collect(Collectors.toList());
    }
}
