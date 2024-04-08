package com.playtomic.tests.wallet.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase.AddMoneyCommand;
import com.playtomic.tests.wallet.domain.Wallet;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddMoneyServiceTest {

  @InjectMocks private AddMoneyService addMoneyService;

  @Test
  void givenMoney_whenAddingMoneyToWallet_thenAddsMoney() {
    AddMoneyCommand addMoneyCommand =
        AddMoneyCommand.builder().id(UUID.randomUUID()).amount(BigDecimal.valueOf(12.5)).build();

    Wallet wallet = addMoneyService.addMoney(addMoneyCommand);

    assertNotNull(wallet);
  }
}
