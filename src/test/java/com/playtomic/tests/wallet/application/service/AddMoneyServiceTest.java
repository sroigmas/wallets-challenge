package com.playtomic.tests.wallet.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.application.exception.ApplicationNotFoundException;
import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase.AddMoneyCommand;
import com.playtomic.tests.wallet.application.port.output.PaymentRepository;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Payment;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.infrastructure.exception.InfrastructureExternalException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    String creditCardNumber = "4242 4242 4242 4242";

    AddMoneyCommand addMoneyCommand =
        AddMoneyCommand.builder().id(id).amount(amount).creditCardNumber(creditCardNumber).build();

    Optional<Wallet> walletOpt = Optional.of(Wallet.builder().id(id).balance(balance).build());
    when(walletRepository.findWalletById(id)).thenReturn(walletOpt);

    Payment payment = Payment.builder().id(UUID.randomUUID().toString()).build();
    when(paymentRepository.charge(creditCardNumber, amount)).thenReturn(payment);

    ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
    when(walletRepository.updateWallet(walletCaptor.capture())).thenReturn(walletOpt.get());

    Wallet wallet = addMoneyService.addMoney(addMoneyCommand);

    assertNotNull(wallet);
    assertEquals(id, wallet.getId());
    assertEquals(balance.add(amount), walletCaptor.getValue().getBalance());
    verify(walletRepository).findWalletById(id);
    verify(paymentRepository).charge(creditCardNumber, amount);
    verify(walletRepository).updateWallet(any(Wallet.class));
  }

  @Test
  void givenNonExistingWallet_whenAddingMoneyToWallet_thenReturnsAnException() {
    UUID id = UUID.randomUUID();
    BigDecimal amount = BigDecimal.valueOf(12.5);
    String creditCardNumber = "4242 4242 4242 4242";

    AddMoneyCommand addMoneyCommand =
        AddMoneyCommand.builder().id(id).amount(amount).creditCardNumber(creditCardNumber).build();

    when(walletRepository.findWalletById(id)).thenReturn(Optional.empty());

    assertThrows(
        ApplicationNotFoundException.class, () -> addMoneyService.addMoney(addMoneyCommand));
    verify(walletRepository).findWalletById(id);
    verify(paymentRepository, never()).charge(creditCardNumber, amount);
    verify(walletRepository, never()).updateWallet(any(Wallet.class));
  }

  @Test
  void givenDatabaseError_whenAddingMoneyToWallet_thenReturnsAnException() {
    UUID id = UUID.randomUUID();
    BigDecimal balance = BigDecimal.valueOf(100);
    BigDecimal amount = BigDecimal.valueOf(12.5);
    String creditCardNumber = "4242 4242 4242 4242";

    AddMoneyCommand addMoneyCommand =
        AddMoneyCommand.builder().id(id).amount(amount).creditCardNumber(creditCardNumber).build();

    Optional<Wallet> walletOpt = Optional.of(Wallet.builder().id(id).balance(balance).build());
    when(walletRepository.findWalletById(id)).thenReturn(walletOpt);

    Payment payment = Payment.builder().id(UUID.randomUUID().toString()).build();
    when(paymentRepository.charge(creditCardNumber, amount)).thenReturn(payment);
    when(walletRepository.updateWallet(any(Wallet.class))).thenThrow(RuntimeException.class);

    assertThrows(
        InfrastructureExternalException.class, () -> addMoneyService.addMoney(addMoneyCommand));
    verify(walletRepository).findWalletById(id);
    verify(paymentRepository).charge(creditCardNumber, amount);
    verify(walletRepository).updateWallet(any(Wallet.class));
    verify(paymentRepository).refund(payment.getId());
  }
}
