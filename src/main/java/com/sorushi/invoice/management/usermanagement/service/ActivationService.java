package com.sorushi.invoice.management.usermanagement.service;

public interface ActivationService {

  void generateAndSendOtp(String email);

  boolean confirmOtp(String email, String otpInput);
}
