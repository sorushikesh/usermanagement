package com.sorushi.invoice.management.usermanagement.exception;

import static com.sorushi.invoice.management.usermanagement.constants.Constants.COLON;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Slf4j
public class ExceptionUtil {

  private ExceptionUtil() {}

  public static ProblemDetail buildProblemDetail(
      HttpStatus httpStatus,
      String errorCode,
      Object[] errorMessageArguments,
      MessageSource messageSource) {

    if (httpStatus == null) {
      log.warn("HttpStatus is null");
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    if (messageSource == null) {
      log.warn("Message source is null");
      return ProblemDetail.forStatusAndDetail(httpStatus, "Message source is null");
    }

    if (errorCode == null) {
      log.warn("Error code is null");
      return ProblemDetail.forStatusAndDetail(httpStatus, "Error code is null");
    }

    String errorMessage;
    try {
      errorMessage =
          messageSource.getMessage(
              errorCode, errorMessageArguments, LocaleContextHolder.getLocale());
    } catch (Exception e) {
      log.error("Unable to fetch message for errorCode: {}", errorCode, e);
      errorMessage = "Message not found for errorCode: " + errorCode;
    }

    return ProblemDetail.forStatusAndDetail(httpStatus, errorCode + COLON + errorMessage);
  }
}
