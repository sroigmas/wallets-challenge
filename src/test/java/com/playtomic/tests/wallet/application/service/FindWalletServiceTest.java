package com.playtomic.tests.wallet.application.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindWalletServiceTest {

  @InjectMocks private FindWalletService findWalletService;

  @Test
  void givenWalletId_whenFindingWallet_thenWalletExists() {
    FindWalletQuery findWalletQuery = FindWalletQuery.builder().id(UUID.randomUUID()).build();

    Optional<Wallet> wallet = findWalletService.findWalletById(findWalletQuery);

    assertTrue(wallet.isPresent());
  }
}
