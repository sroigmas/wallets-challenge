package com.playtomic.tests.wallet.infrastructure.configuration;

import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.application.service.AddMoneyService;
import com.playtomic.tests.wallet.application.service.FindWalletService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletConfiguration {

  @Bean
  public FindWalletService findWalletService(final WalletRepository walletRepository) {
    return new FindWalletService(walletRepository);
  }

  @Bean
  public AddMoneyService addMoneyService() {
    return new AddMoneyService();
  }
}
