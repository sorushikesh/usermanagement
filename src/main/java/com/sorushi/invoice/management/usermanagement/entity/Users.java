package com.sorushi.invoice.management.usermanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "emailId")})
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String emailId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean isActive;

  @Column(nullable = false)
  private String role;
}
