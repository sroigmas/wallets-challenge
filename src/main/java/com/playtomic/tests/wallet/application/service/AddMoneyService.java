package com.playtomic.tests.wallet.application.service;

import com.playtomic.tests.wallet.application.exception.ApplicationNotFoundException;
import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase;
import com.playtomic.tests.wallet.application.port.output.PaymentRepository;
import com.playtomic.tests.wallet.application.port.output.WalletRepository;
import com.playtomic.tests.wallet.domain.Payment;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.infrastructure.exception.InfrastructureExternalException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class AddMoneyService implements AddMoneyUseCase {

  private final PaymentRepository paymentRepository;

  private final WalletRepository walletRepository;

  @Override
  public Wallet addMoney(AddMoneyCommand addMoneyCommand) {
    UUID walletId = addMoneyCommand.getId();

    Wallet wallet =
        walletRepository
            .findWalletById(walletId)
            .orElseThrow(
                () ->
                    new ApplicationNotFoundException(
                        String.format("Wallet with id=%s could not be found.", walletId)));

    Payment payment =
        paymentRepository.charge(
            addMoneyCommand.getCreditCardNumber(), addMoneyCommand.getAmount());
    log.info(
        String.format(
            "Payment with id=%s charged for wallet with id=%s", payment.getId(), walletId));

    Wallet newWallet = wallet.addMoney(addMoneyCommand.getAmount());

    try {
      newWallet = walletRepository.updateWallet(newWallet);
      log.info(String.format("Wallet updated with id=%s", walletId));
    } catch (Exception e) {
      log.warn(
          String.format(
              "Refunding payment with id=%s due to an error while updating wallet with id=%s",
              payment.getId(), walletId));
      paymentRepository.refund(payment.getId());
      throw new InfrastructureExternalException(
          String.format("External error while updating wallet with id=%s.", walletId));
    }

    return newWallet;
  }
}
