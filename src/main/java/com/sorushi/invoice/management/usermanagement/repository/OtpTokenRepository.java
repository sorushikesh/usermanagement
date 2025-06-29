package com.sorushi.invoice.management.usermanagement.repository;

import com.sorushi.invoice.management.usermanagement.entity.OtpToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
  Optional<OtpToken> findTopByEmailOrderByExpiryTimeDesc(String email);

  void deleteByExpiryTimeBefore(LocalDateTime time);
}
