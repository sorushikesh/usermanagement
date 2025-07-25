package com.sorushi.invoice.management.usermanagement.repository;

import com.sorushi.invoice.management.usermanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

  Users findByEmailId(String emailId);
}
