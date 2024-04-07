package com.playtomic.tests.wallet.infrastructure.rest;

import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase;
import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.domain.Wallet;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
@AllArgsConstructor
public class WalletController {

  private FindWalletUseCase findWalletUseCase;
  private ModelMapper modelMapper;

  @GetMapping("/{id}")
  public WalletResponse getWalletById(@PathVariable("id") UUID id) {
    Wallet wallet = findWalletUseCase.findWalletById(FindWalletQuery.builder().id(id).build());

    return modelMapper.map(wallet, WalletResponse.class);
  }
}
