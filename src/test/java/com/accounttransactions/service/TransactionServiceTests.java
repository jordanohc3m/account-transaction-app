package com.accounttransactions.service;

import com.accounttransactions.BaseTests;
import com.accounttransactions.entity.Account;
import com.accounttransactions.entity.Transaction;
import com.accounttransactions.exception.AccountNotFoundException;
import com.accounttransactions.exception.GenericValidateRuntimeException;
import com.accounttransactions.exception.InvalidOperationTypeException;
import com.accounttransactions.service.impl.OperationTypeService;
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

    @Autowired
    protected OperationTypeService operationTypeService;

    private Account validAccount;

    private final Account invalidAccount = Account.builder().id(Long.valueOf(-1)).documentNumber(UUID.randomUUID().toString()).build();

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
    }

    @BeforeAll
    void prepare() {
        validAccount = accountService.create(Account.builder().documentNumber(UUID.randomUUID().toString()).build());
    }

    private Transaction createRandomTransaction(Long operationTypeId, Account account) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperationType(operationTypeService.findByOperationTypeId(Long.valueOf(operationTypeId)).get());
        transaction.setAumount(new Random().nextDouble());
        return service.create(transaction);
    }

    @Test
    void createTransactionSuccess() {
        Transaction transaction = createRandomTransaction(Long.valueOf(1), this.validAccount);
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
                Arguments.of(createRandomTransaction(Long.valueOf(1), validAccount), true),
                Arguments.of(createRandomTransaction(Long.valueOf(2), validAccount), true),
                Arguments.of(createRandomTransaction(Long.valueOf(3), validAccount), true),
                Arguments.of(createRandomTransaction(Long.valueOf(4), validAccount), false));
    }

    @MethodSource("providerTransactionException")
    @ParameterizedTest(name = "Expected: {1} for : {0}")
    void shouldReturnTransactionException(RuntimeException input, String expectedMessage, String expectedClass) {
        // given
        RuntimeException runtimeException = input;
        // when
        String output = runtimeException.getMessage();
        String outputClass = runtimeException.getClass().getSimpleName();

        // then
        Assertions.assertEquals(expectedMessage, output);
        Assertions.assertEquals(expectedClass, outputClass);

    }

    private Stream<Arguments> providerTransactionException() {
        return Stream.of(
                Arguments.of(createTransactionInvalidThrow(), "Invalid Operation Type", InvalidOperationTypeException.class.getSimpleName()),
                Arguments.of(createTransactionInvalidAccount(), "Account not found", AccountNotFoundException.class.getSimpleName()),
                Arguments.of(createTransactionInvalidValue(), "Value must be different of zero", GenericValidateRuntimeException.class.getSimpleName())
        );
    }

    private InvalidOperationTypeException createTransactionInvalidThrow() {
        try {
            service.create(createRandomTransactionDto(Long.valueOf(5), new Random().nextDouble()).toEntity());
        } catch (InvalidOperationTypeException ex) {
            return ex;
        }
        return null;
    }

    private AccountNotFoundException createTransactionInvalidAccount() {
        try {
            createRandomTransaction(Long.valueOf(4), invalidAccount);
        } catch (AccountNotFoundException ex) {
            return ex;
        }
        return null;
    }

    private GenericValidateRuntimeException createTransactionInvalidValue() {
        try {
            service.create(createRandomTransactionDto(Long.valueOf(4), Double.valueOf(0)).toEntity());
        } catch (GenericValidateRuntimeException ex) {
            return ex;
        }
        return null;
    }
}
