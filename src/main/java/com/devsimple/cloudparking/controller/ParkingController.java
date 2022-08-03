package com.devsimple.cloudparking.controller;

import com.devsimple.cloudparking.config.ModelMapperConfig;
import com.devsimple.cloudparking.controller.dto.ParkingCreateDTO;
import com.devsimple.cloudparking.controller.dto.ParkingDTO;
import com.devsimple.cloudparking.controller.dto.ParkingUpdateDTO;
import com.devsimple.cloudparking.entity.Parking;
import com.devsimple.cloudparking.service.ParkingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parkings")
@Api(tags = "Parking Controller")
public class ParkingController {

    private ParkingService service;
    private ModelMapperConfig modelMapper;

    public ParkingController(ParkingService service, ModelMapperConfig modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @ApiOperation("Find All parkings")
    @GetMapping
    public ResponseEntity<List<ParkingDTO>> findAll() {
        List<Parking> parkingList = service.findAll();
        List<ParkingDTO> result = modelMapper.parkingDTOlist(parkingList);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("Find one parking")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingDTO> finById(@PathVariable String id) {
        Parking parking = service.findById(id);
        ParkingDTO result = modelMapper.parkingDTO(parking);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("Create Parking")
    @PostMapping
    public ResponseEntity<ParkingDTO> save(@RequestBody ParkingCreateDTO parkingDTO) {
        var parking = modelMapper.toParkingCreate(parkingDTO);
        var save = service.save(parking);
        var result = modelMapper.parkingDTO(save);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @ApiOperation("Create Parking")
    @PutMapping("/{id}")
    public ResponseEntity<ParkingDTO> update(@PathVariable String id, @RequestBody ParkingUpdateDTO parkingDTO) {
        var parking = modelMapper.toParkingUpdate(parkingDTO);
        var save = service.update(id, parking);
        var result = modelMapper.parkingDTO(save);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("Delete Parking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
