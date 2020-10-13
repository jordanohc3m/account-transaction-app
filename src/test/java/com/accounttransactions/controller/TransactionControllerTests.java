package com.accounttransactions.controller;

import com.accounttransactions.BaseTests;
import com.accounttransactions.dto.TransactionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Tag("integration-tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTests extends BaseTests {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransaction() throws JsonProcessingException {

        final TransactionDTO transaction = createRandomTransactionDto(Long.valueOf(1), Double.valueOf(1000));

        RestAssured.given()
                .body(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, Matchers.anything("/transactions"))
                .contentType(ContentType.JSON)
                .body("aumount", Matchers.notNullValue())
                .body("account_id", Matchers.notNullValue())
                .body("operation_type_id", Matchers.notNullValue());
    }

    @Test
    void shouldErrorCreateInvalidOperation() throws JsonProcessingException {

        final TransactionDTO transaction = createRandomTransactionDto(Long.valueOf(5), new Random().nextDouble());

        RestAssured.given()
                .body(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .header(HttpHeaders.LOCATION, Matchers.anything("/transactions"))
                .contentType(ContentType.JSON)
                .body("status", Matchers.comparesEqualTo("BAD_REQUEST"))
                .body("message", Matchers.comparesEqualTo("Invalid Operation Type"));
    }

    @Test
    void shouldErrorCreateInvalidAccount() throws JsonProcessingException {

        TransactionDTO transaction = createRandomTransactionDto(Long.valueOf(1), new Random().nextDouble());
        transaction.setAccountId(new Random().nextLong());

        RestAssured.given()
                .body(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .header(HttpHeaders.LOCATION, Matchers.anything("/transactions"))
                .contentType(ContentType.JSON)
                .body("status", Matchers.comparesEqualTo("NOT_FOUND"))
                .body("message", Matchers.comparesEqualTo("Account not found"));
    }

    @Test
    void shouldErrorCreateInvalidAumount() throws JsonProcessingException {

        TransactionDTO transaction = createRandomTransactionDto(Long.valueOf(1), Double.valueOf(0));

        RestAssured.given()
                .body(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/transactions")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .header(HttpHeaders.LOCATION, Matchers.anything("/transactions"))
                .contentType(ContentType.JSON)
                .body("status", Matchers.comparesEqualTo("BAD_REQUEST"))
                .body("message", Matchers.comparesEqualTo("Value must be different of zero"));
    }
}
