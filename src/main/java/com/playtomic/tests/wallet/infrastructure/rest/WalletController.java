package com.playtomic.tests.wallet.infrastructure.rest;

import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase;
import com.playtomic.tests.wallet.application.port.input.AddMoneyUseCase.AddMoneyCommand;
import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase;
import com.playtomic.tests.wallet.application.port.input.FindWalletUseCase.FindWalletQuery;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.infrastructure.exception.InfrastructureValidationException;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
@AllArgsConstructor
public class WalletController {

  private FindWalletUseCase findWalletUseCase;
  private AddMoneyUseCase addMoneyUseCase;
  private ModelMapper modelMapper;

  @GetMapping("/{id}")
  public WalletResponse getWalletById(@PathVariable("id") UUID id) {
    Wallet wallet = findWalletUseCase.findWalletById(FindWalletQuery.builder().id(id).build());

    return modelMapper.map(wallet, WalletResponse.class);
  }

  @PutMapping("/{id}/charge")
  @ResponseStatus(HttpStatus.CREATED)
  public WalletResponse addMoney(
      @PathVariable("id") UUID id, @Valid @RequestBody PaymentRequest paymentRequest) {
    BigDecimal amount = validatePaymentAmount(paymentRequest.getAmount());

    AddMoneyCommand addMoneyCommand =
        AddMoneyCommand.builder()
            .id(id)
            .amount(amount)
            .creditCardNumber(paymentRequest.getCreditCardNumber())
            .build();

    Wallet wallet = addMoneyUseCase.addMoney(addMoneyCommand);

    return modelMapper.map(wallet, WalletResponse.class);
  }

  private BigDecimal validatePaymentAmount(String amount) {
    try {
      return new BigDecimal(amount);
    } catch (NumberFormatException e) {
      throw new InfrastructureValidationException("Field 'amount' is not a valid number.");
    }
  }
}
