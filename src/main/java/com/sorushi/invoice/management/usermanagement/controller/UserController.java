package com.sorushi.invoice.management.usermanagement.controller;

import static com.sorushi.invoice.management.usermanagement.constants.APIEndpoints.API_USER_MANAGEMENT_SERVICE;
import static com.sorushi.invoice.management.usermanagement.constants.APIEndpoints.REGISTER_USER;

import com.sorushi.invoice.management.usermanagement.dto.UserDetails;
import com.sorushi.invoice.management.usermanagement.entity.Users;
import com.sorushi.invoice.management.usermanagement.service.serviceImpl.UserRegistrationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping(API_USER_MANAGEMENT_SERVICE)
public class UserController {

  private final UserRegistrationServiceImpl userRegistrationService;

  public UserController(UserRegistrationServiceImpl userRegistrationService) {
    this.userRegistrationService = userRegistrationService;
  }
  @PostMapping(REGISTER_USER)
  public ResponseEntity<Users> registerUser(
      @RequestBody UserDetails userDetails,
      @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

    if (StringUtils.hasLength(acceptLanguage)) {
      Locale locale = Locale.forLanguageTag(acceptLanguage);
      LocaleContextHolder.setLocale(locale);
    } else {
      LocaleContextHolder.setDefaultLocale(Locale.ENGLISH);
    }

    log.info("User registration starts for user {}", userDetails.getUserName());
    Users users = userRegistrationService.registerUser(userDetails);
    return ResponseEntity.ok(users);
  }

}
