package com.sorushi.invoice.management.usermanagement.controller;

import static com.sorushi.invoice.management.usermanagement.constants.APIEndpoints.*;
import static com.sorushi.invoice.management.usermanagement.constants.Messages.*;

import com.sorushi.invoice.management.usermanagement.dto.ResponseBody;
import com.sorushi.invoice.management.usermanagement.dto.UserDetails;
import com.sorushi.invoice.management.usermanagement.service.serviceImpl.ActivationServiceImpl;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ACTIVATE_USER)
public class ActivationController {

  private final ActivationServiceImpl activationService;

  public ActivationController(ActivationServiceImpl activationService) {
    this.activationService = activationService;
  }

  @PostMapping(value = REQUEST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseBody> requestOtp(
      @RequestBody UserDetails userDetails, @RequestHeader("Accept") String acceptHeader) {
    log.info("User activation process start for user {}", userDetails.getEmailId());
    log.info("Header {}", acceptHeader);
    activationService.generateAndSendOtp(userDetails.getEmailId());
    return ResponseEntity.status(HttpStatus.OK)
        .body(ResponseBody.builder().status(SUCCESS).messages(List.of(OTP_SEND_SUCCESS)).build());
  }

  @PostMapping(CONFIRM)
  public ResponseEntity<ResponseBody> confirmOtp(@RequestBody Map<String, String> request) {
    log.info("Validating OTP for user {}", request.get(EMAIL));
    boolean success = activationService.confirmOtp(request.get(EMAIL), request.get(OTP));
    return success
        ? ResponseEntity.ok(
            ResponseBody.builder().status(SUCCESS).messages(List.of(USER_ACTIVATED)).build())
        : ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseBody.builder().status(FAILED).messages(List.of(INVALID_OTP)).build());
  }
}
