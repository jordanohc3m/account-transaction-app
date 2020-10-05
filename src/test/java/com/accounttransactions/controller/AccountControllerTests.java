package com.accounttransactions.controller;

import com.accounttransactions.DatabaseInitializer;
import com.accounttransactions.dto.AccountDTO;
import com.accounttransactions.entity.Account;
import com.accounttransactions.repository.AccountRepository;
import com.accounttransactions.service.impl.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;


@Tag("integration-tests")
@ContextConfiguration(initializers = DatabaseInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AccountDTO accountDTO = new AccountDTO(UUID.randomUUID().toString());

    @PostConstruct
    public void init() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void shouldReturnAccounts() {
        RestAssured.given()
                .when()
                .get("/accounts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", Matchers.emptyIterable());
    }

    @Test
    void shouldReturnOneAccount() {
        Account newAccount = service.create(accountDTO.toEntity());

        RestAssured.given()
                .when()
                .get("/accounts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", Matchers.hasSize(1))
                .body("[0].id", Matchers.equalTo(newAccount.getId().intValue()))
                .body("[0].document_number", Matchers.equalTo(newAccount.getDocumentNumber()));
    }

    @Test
    void shouldReturnOneAccountUsingPagination() {

        Account account01 = service.create(accountDTO.toEntity());
        Account account02 = service.create(new Account(UUID.randomUUID().toString()));

        service.create(new Account(UUID.randomUUID().toString()));
        service.create(new Account(UUID.randomUUID().toString()));

        RestAssured.given()
                .queryParam("size", 2)
                .when()
                .get("/accounts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", Matchers.hasSize(2))
                .body("id", Matchers.hasItems(account01.getId().intValue(),
                        account02.getId().intValue()));
    }

    @Test
    void shouldReturnAAccount() {

        Account newAccount = service.create(accountDTO.toEntity());

        RestAssured.given()
                .pathParam("id", newAccount.getId())
                .when()
                .get("/accounts/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(newAccount.getId().intValue()));
    }

    @Test
    void shouldReturnNotFound() {

        RestAssured.given()
                .when()
                .get("/accounts/-1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    void shouldCreateAccount() throws JsonProcessingException {
        long id = RestAssured.given()
                .body(objectMapper.writeValueAsString(accountDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, Matchers.anything("/accounts"))
                .contentType(ContentType.JSON)
                .body("id", Matchers.notNullValue())
                .body("document_number", Matchers.notNullValue())
                .and()
                .extract()
                .body()
                .jsonPath()
                .getLong("id");

        Optional<Account> account = repository.findById(id);
        Assertions.assertTrue(account.isPresent());
    }

    @Test
    void shouldErrorCreateAccount() throws JsonProcessingException {
        RestAssured.given()
                .body(objectMapper.writeValueAsString(new Account(null).toDto()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/accounts")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .header(HttpHeaders.LOCATION, Matchers.anything("/accounts"))
                .contentType(ContentType.JSON)
                .body("status", Matchers.comparesEqualTo("BAD_REQUEST"))
                .body("message", Matchers.comparesEqualTo("Fill document_number field"));
    }
}
