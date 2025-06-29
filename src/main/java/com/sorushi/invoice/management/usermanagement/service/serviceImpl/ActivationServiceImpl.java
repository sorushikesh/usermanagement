package com.sorushi.invoice.management.usermanagement.service.serviceImpl;

import static com.sorushi.invoice.management.usermanagement.exception.ErrorCodes.FAILED_TO_SEND_MAIL;

import com.sorushi.invoice.management.usermanagement.configuration.MessageSourceConfig;
import com.sorushi.invoice.management.usermanagement.entity.OtpToken;
import com.sorushi.invoice.management.usermanagement.entity.Users;
import com.sorushi.invoice.management.usermanagement.exception.UserManagementServiceException;
import com.sorushi.invoice.management.usermanagement.repository.OtpTokenRepository;
import com.sorushi.invoice.management.usermanagement.service.ActivationService;
import com.sorushi.invoice.management.usermanagement.utils.MailerUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActivationServiceImpl implements ActivationService {

  private final MessageSource messageSource;
  private final UserServiceImpl userService;
  private final OtpTokenRepository otpRepo;
  private final JavaMailSender mailSender;

  Random random = new Random();

  public ActivationServiceImpl(
      @Qualifier(MessageSourceConfig.MESSAGE_SOURCE) MessageSource messageSource,
      UserServiceImpl userService,
      OtpTokenRepository otpRepo,
      JavaMailSender mailSender) {
    this.messageSource = messageSource;
    this.userService = userService;
    this.otpRepo = otpRepo;
    this.mailSender = mailSender;
  }

  @Override
  public void generateAndSendOtp(String email) {
    log.info("Generating OTP for user {}", email);
    String otp = String.valueOf(this.random.nextInt(900000) + 100000);
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

    OtpToken token = OtpToken.builder().otp(otp).email(email).expiryTime(expiry).build();
    otpRepo.save(token);

    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      helper.setTo(email);
      helper.setSubject("Your OTP Code - Sorushi Invoice System");
      helper.setText(MailerUtil.buildOtpHtmlTemplate(otp), true);

      mailSender.send(mimeMessage);
      log.info("OTP sent over email {} for user activation", email);
    } catch (MessagingException e) {
      log.error("Failed to send OTP email to {} due to {}", email, e.getMessage());
      throw new UserManagementServiceException(
          HttpStatus.BAD_REQUEST, FAILED_TO_SEND_MAIL, null, messageSource);
    }
  }

  @Override
  public boolean confirmOtp(String email, String otpInput) {
    Optional<OtpToken> tokenOpt = otpRepo.findTopByEmailOrderByExpiryTimeDesc(email);

    if (tokenOpt.isPresent()) {
      log.info("OTP found");
      OtpToken token = tokenOpt.get();

      if (token.getOtp().equals(otpInput) && token.getExpiryTime().isAfter(LocalDateTime.now())) {
        Optional<Users> userOpt = userService.getUserByEmailId(email);

        if (userOpt.isPresent()) {
          Users user = userOpt.get();
          user.setActive(true);
          userService.updateUser(user);
          log.info("User activated successfully");
        }

        log.info("OTP validated successfully");
        return true;
      } else {
        log.warn("Invalid OTP or OTP expired");
      }
    } else {
      log.warn("No OTP found for email: {}", email);
    }

    return false;
  }
}
