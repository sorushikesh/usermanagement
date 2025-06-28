package com.sorushi.invoice.management.usermanagement.exception;

import static com.sorushi.invoice.management.usermanagement.constants.Constants.ERRORS;

import java.util.LinkedList;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings("unused")
@RestControllerAdvice
public class UserManagementServiceExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  public UserManagementServiceExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(UserManagementServiceException.class)
  public ResponseEntity<ProblemDetail> handleUserManagementServiceException(
      UserManagementServiceException ex) {
    ProblemDetail problemDetail = ex.getBody();
    appendErrorCode(problemDetail, ex.getDetailMessageCode(), ex.getDetailMessageArguments());
    return ResponseEntity.status(ex.getStatusCode()).body(problemDetail);
  }

  private void appendErrorCode(
      ProblemDetail problemDetail, String errorCode, Object[] errorArguments) {
    if (problemDetail == null || errorCode == null) return;

    String errorMessage =
        messageSource.getMessage(errorCode, errorArguments, LocaleContextHolder.getLocale());
    ErrorDetail newError = new ErrorDetail(errorCode, errorMessage);
    List<ErrorDetail> existingErrors = new LinkedList<>();

    Object errorsObj = null;
    if (problemDetail.getProperties() != null) {
      errorsObj = problemDetail.getProperties().get(ERRORS);
    }

    if (errorsObj instanceof List<?> list) {
      for (Object obj : list) {
        if (obj instanceof ErrorDetail ed) {
          existingErrors.add(ed);
        }
      }
    }

    existingErrors.addFirst(newError);
    problemDetail.setProperty(ERRORS, existingErrors);
  }
}
