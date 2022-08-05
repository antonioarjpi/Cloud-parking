package com.devsimple.cloudparking.controller;

import com.devsimple.cloudparking.controller.dto.ParkingCreateDTO;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingControllerTest extends AbstractContainer {

    @LocalServerPort
    private int randomPort;

    @BeforeEach
    public void setUpTest(){
        RestAssured.port = randomPort;
    }

    @Test
    void whenfindAllCheckResult() {
        RestAssured.given()
                .when()
                .get("/parkings")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void whenSaveThenCheckIsCreated() {
        ParkingCreateDTO createDTO = new ParkingCreateDTO();
        createDTO.setColor("Branco");
        createDTO.setLicense("License");
        createDTO.setModel("Renault");
        createDTO.setState("PI");

        RestAssured.given()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createDTO)
                .post("/parkings")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("color", Matchers.equalTo("Branco"))
                .body("model", Matchers.equalTo("Renault"))
                .body("state", Matchers.equalTo("PI"))
                .body("license", Matchers.equalTo("License"));
    }

}