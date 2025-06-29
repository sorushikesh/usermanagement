package com.sorushi.invoice.management.usermanagement.service;

import com.sorushi.invoice.management.usermanagement.dto.UserDetails;
import com.sorushi.invoice.management.usermanagement.entity.Users;
import java.util.Optional;

public interface UserService {

  Users registerUser(UserDetails userDetails);

  Users updateUser(Users users);

  Optional<Users> getUserByEmailId(String emailId);
}
