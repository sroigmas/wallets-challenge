package com.playtomic.tests.wallet.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase.AddMoneyCommand;
import com.playtomic.tests.wallet.application.port.output.PaymentRepository;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddMoneyServiceTest {

  @Mock private PaymentRepository paymentRepository;

  @Mock private WalletRepository walletRepository;

  @InjectMocks private AddMoneyService addMoneyService;

  @Test
  void givenMoney_whenAddingMoneyToWallet_thenAddsMoney() {
    UUID id = UUID.randomUUID();
    BigDecimal balance = BigDecimal.valueOf(100);
    BigDecimal amount = BigDecimal.valueOf(12.5);

    AddMoneyCommand addMoneyCommand = AddMoneyCommand.builder().id(id).amount(amount).build();

    Optional<Wallet> walletOpt = Optional.of(Wallet.builder().id(id).balance(balance).build());
    Mockito.when(walletRepository.findWalletById(id)).thenReturn(walletOpt);

    Wallet wallet = addMoneyService.addMoney(addMoneyCommand);

    assertNotNull(wallet);
    assertEquals(id, wallet.getId());
    assertEquals(balance.add(amount), wallet.getBalance());
    // TODO: improve test
  }
}
