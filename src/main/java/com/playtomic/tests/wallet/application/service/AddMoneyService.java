package com.playtomic.tests.wallet.application.service;

import com.playtomic.tests.wallet.application.exception.ApplicationNotFoundException;
import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase;
import com.playtomic.tests.wallet.application.port.output.PaymentRepository;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Wallet;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddMoneyService implements AddMoneyUseCase {

  private final PaymentRepository paymentRepository;

  private final WalletRepository walletRepository;

  @Override
  public Wallet addMoney(AddMoneyCommand addMoneyCommand) {
    Wallet wallet =
        walletRepository
            .findWalletById(addMoneyCommand.getId())
            .orElseThrow(
                () ->
                    new ApplicationNotFoundException(
                        String.format(
                            "Wallet with id=%s could not be found.", addMoneyCommand.getId())));

    paymentRepository.charge(addMoneyCommand.getCreditCardNumber(), addMoneyCommand.getAmount());

    Wallet newWallet = wallet.addMoney(addMoneyCommand.getAmount());

    // TODO: replace by Kafka event which will persist in DB
    walletRepository.updateWallet(newWallet);

    return newWallet;
  }
}
