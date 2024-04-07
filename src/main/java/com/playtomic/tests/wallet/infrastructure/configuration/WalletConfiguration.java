package com.playtomic.tests.wallet.infrastructure.configuration;

import com.playtomic.tests.wallet.application.service.FindWalletService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletConfiguration {

  @Bean
  public FindWalletService findWalletService() {
    return new FindWalletService();
  }
}
