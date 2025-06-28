package com.sorushi.invoice.management.usermanagement.service.serviceImpl;

import static com.sorushi.invoice.management.usermanagement.exception.ErrorCodes.USERNAME_ALREADY_EXIST;
import static com.sorushi.invoice.management.usermanagement.exception.ErrorCodes.USER_EMAIL_ALREADY_EXIST;

import com.sorushi.invoice.management.usermanagement.configuration.MessageSourceConfig;
import com.sorushi.invoice.management.usermanagement.dto.UserDetails;
import com.sorushi.invoice.management.usermanagement.entity.Users;
import com.sorushi.invoice.management.usermanagement.exception.UserManagementServiceException;
import com.sorushi.invoice.management.usermanagement.repository.UserRepository;
import com.sorushi.invoice.management.usermanagement.service.UserRegistrationService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

  private final MessageSource messageSource;

  private final UserRepository userRepository;

  public UserRegistrationServiceImpl(
      @Qualifier(MessageSourceConfig.MESSAGE_SOURCE) MessageSource messageSource,
      UserRepository userRepository) {
    this.messageSource = messageSource;
    this.userRepository = userRepository;
  }

  @Override
  public Users registerUser(UserDetails userDetails) throws UserManagementServiceException {

    log.info("Checking if user already exist or not");
    Optional<Users> userByUserName = getUserByUserName(userDetails.getUserName());
    if (userByUserName.isPresent()) {
      log.error("User already exist in system for userName {}", userDetails.getUserName());
      throw new UserManagementServiceException(
          HttpStatus.BAD_REQUEST, USERNAME_ALREADY_EXIST, null, messageSource);
    }

    Optional<Users> userByEmail = getUserByEmailId(userDetails.getEmailId());
    if (userByEmail.isPresent()) {
      log.error("User already exist in system for email {}", userDetails.getEmailId());
      throw new UserManagementServiceException(
          HttpStatus.BAD_REQUEST, USER_EMAIL_ALREADY_EXIST, null, messageSource);
    }

    Users users =
        Users.builder()
            .userName(userDetails.getUserName())
            .emailId(userDetails.getEmailId())
            .password(userDetails.getPassword())
            .role("DEFAULT")
            .isActive(false)
            .build();
    return userRepository.save(users);
  }

  @Override
  public Optional<Users> getUserByUserName(String userName) {
    log.info("Fetching user for user {}", userName);
    Users users = userRepository.findByUserName(userName);

    if (users == null) {
      log.debug("User not found for userName {}", userName);
      return Optional.empty();
    }
    log.info("User found for userName {}", userName);
    return Optional.of(users);
  }

  @Override
  public Optional<Users> getUserByEmailId(String emailId) {
    log.info("Fetching user for email {}", emailId);
    Users users = userRepository.findByEmailId(emailId);

    if (users == null) {
      log.debug("User not found for email {}", emailId);
      return Optional.empty();
    }
    log.info("User found for email {}", emailId);
    return Optional.of(users);
  }
}
