package com.playtomic.tests.wallet.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.application.exception.ApplicationNotFoundException;
import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindWalletServiceTest {

  @Mock private WalletRepository walletRepository;

  @InjectMocks private FindWalletService findWalletService;

  @Test
  void givenWalletId_whenFindingWallet_thenWalletExists() {
    UUID id = UUID.randomUUID();
    FindWalletQuery findWalletQuery = FindWalletQuery.builder().id(id).build();
    Wallet expectedWallet = buildWallet(id, BigDecimal.valueOf(12.5));
    when(walletRepository.findWalletById(findWalletQuery)).thenReturn(Optional.of(expectedWallet));

    Wallet actualWallet = findWalletService.findWalletById(findWalletQuery);

    assertEquals(expectedWallet, actualWallet);
  }

  @Test
  void givenNonExistingWallet_whenFindingWallet_thenWalletDoesntExist() {
    UUID id = UUID.randomUUID();
    FindWalletQuery findWalletQuery = FindWalletQuery.builder().id(id).build();
    when(walletRepository.findWalletById(findWalletQuery)).thenReturn(Optional.empty());

    assertThrows(
        ApplicationNotFoundException.class,
        () -> findWalletService.findWalletById(findWalletQuery));
  }

  private Wallet buildWallet(UUID id, BigDecimal balance) {
    return Wallet.builder().id(id).balance(balance).build();
  }
}
