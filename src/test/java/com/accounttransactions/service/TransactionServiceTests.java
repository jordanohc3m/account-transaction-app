package com.accounttransactions.service;

import com.accounttransactions.BaseTests;
import com.accounttransactions.dto.TransactionDTO;
import com.accounttransactions.entity.Account;
import com.accounttransactions.entity.Transaction;
import com.accounttransactions.exception.AccountNotFoundException;
import com.accounttransactions.exception.GenericValidateRuntimeException;
import com.accounttransactions.exception.InvalidOperationTypeException;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("unit-tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTests extends BaseTests {

    @Autowired
    private ITransactionService service;

    private Account account;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
    }

    @BeforeAll
    void prepare() {
        account = accountService.create(new Account(UUID.randomUUID().toString()));
    }

    private Transaction createRandomTransaction(Long operationTypeId) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationTypeService.findByOperationTypeId(Long.valueOf(operationTypeId)).get());
        transaction.setAumount(new Random().nextDouble());
        return service.create(transaction);
    }

    @Test
    void createTransactionSuccess() {
        Transaction transaction = createRandomTransaction(Long.valueOf(1));
        Assertions.assertNotNull(transaction.getAumount());
        Assertions.assertNotNull(transaction.getEventDate());
    }

    @MethodSource("providerTransactionAumount")
    @ParameterizedTest(name = "Expected: {1} for aumount: {0}")
    void shouldReturnTheCorrectAumont(Transaction input, boolean expected) {
        // given
        Transaction transaction = input;
        // when
        boolean output = transaction.getAumount() < 0;
        // then
        Assertions.assertEquals(expected, output);
    }

    private Stream<Arguments> providerTransactionAumount() {
        return Stream.of(
                Arguments.of(createRandomTransaction(Long.valueOf(1)), true),
                Arguments.of(createRandomTransaction(Long.valueOf(2)), true),
                Arguments.of(createRandomTransaction(Long.valueOf(3)), true),
                Arguments.of(createRandomTransaction(Long.valueOf(4)), false));
    }

    @Test
    void createTransactionInvalidOperation() {
        TransactionDTO transactionDTO = createRandomTransactionDto(Long.valueOf(5), new Random().nextDouble());
        Transaction transaction = transactionDTO.toEntity();
        Assertions.assertThrows(InvalidOperationTypeException.class, () -> service.create(transaction),"Invalid Operation Type");
    }

    @Test
    void createTransactionInvalidAccount() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(new Random().nextLong());
        transactionDTO.setOperationTypeId(Long.valueOf(4));
        transactionDTO.setAumount(new Random().nextDouble());
        Transaction transaction = transactionDTO.toEntity();
        Assertions.assertThrows(AccountNotFoundException.class, () -> service.create(transaction),"Account not found");
    }

    @Test
    void createTransactionInvalidValue() {
        TransactionDTO transactionDTO = createRandomTransactionDto(Long.valueOf(4), Double.valueOf(0));
        Transaction transaction = transactionDTO.toEntity();
        Assertions.assertThrows(GenericValidateRuntimeException.class, () -> service.create(transaction),"Value must be different of zero");
    }
}
