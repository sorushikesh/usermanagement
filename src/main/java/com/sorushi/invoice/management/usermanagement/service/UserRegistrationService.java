package com.sorushi.invoice.management.usermanagement.service;

import com.sorushi.invoice.management.usermanagement.dto.UserDetails;
import com.sorushi.invoice.management.usermanagement.entity.Users;

import java.util.Optional;

public interface UserRegistrationService {

  Users registerUser(UserDetails userDetails);

  Optional<Users> getUserByUserName(String userName);

  Optional<Users> getUserByEmailId(String emailId);
}
