package com.playtomic.tests.wallet.application.port.output;

import com.playtomic.tests.wallet.service.Payment;
import java.math.BigDecimal;

public interface PaymentRepository {

  Payment charge(String creditCardNumber, BigDecimal amount);

  void refund(String paymentId);
}
