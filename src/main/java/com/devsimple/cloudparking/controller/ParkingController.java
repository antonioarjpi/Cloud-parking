package com.devsimple.cloudparking.controller;

import com.devsimple.cloudparking.config.ModelMapperConfig;
import com.devsimple.cloudparking.controller.dto.ParkingDTO;
import com.devsimple.cloudparking.entity.Parking;
import com.devsimple.cloudparking.service.ParkingService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parkings")
public class ParkingController {

    private ParkingService service;
    private ModelMapperConfig modelMapper;

    public ParkingController(ParkingService service, ModelMapperConfig modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ParkingDTO> findAll(){
        List<Parking> parkingList = service.findAll();
        List<ParkingDTO> result = modelMapper.parkingDTOlist(parkingList);
        return result;
    }

}
