package com.playtomic.tests.wallet.infrasctructure.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.infrastructure.configuration.MapperConfiguration;
import com.playtomic.tests.wallet.infrastructure.jpa.WalletDbRepository;
import com.playtomic.tests.wallet.infrastructure.jpa.WalletEntity;
import com.playtomic.tests.wallet.infrastructure.jpa.WalletJpaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class WalletDbRepositoryTest {

  @Mock private WalletJpaRepository walletJpaRepository;
  @Spy private final ModelMapper modelMapper = new MapperConfiguration().modelMapper();
  @InjectMocks private WalletDbRepository walletDbRepository;

  @Test
  void givenWalletId_whenFindingWallet_thenWalletIsReturned() {
    UUID id = UUID.randomUUID();
    BigDecimal balance = BigDecimal.valueOf(12.5);

    FindWalletQuery findWalletQuery = FindWalletQuery.builder().id(id).build();
    WalletEntity walletEntity = WalletEntity.builder().id(id).balance(balance).build();
    when(walletJpaRepository.findById(id)).thenReturn(Optional.of(walletEntity));

    Optional<Wallet> walletOpt = walletDbRepository.findWalletById(findWalletQuery);

    assertTrue(walletOpt.isPresent());
    assertEquals(walletEntity.getId(), walletOpt.get().getId());
    assertEquals(walletEntity.getBalance(), walletOpt.get().getBalance());
  }
}
