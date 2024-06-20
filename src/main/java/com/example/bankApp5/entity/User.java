package com.example.bankApp5.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        private Long id;
        private String firstName;
        private String lastName;
        private String otherName;
        private String gender;
        private String address;
        private  String stateOfOrigin;
        private String accountNumber;
        private BigDecimal accountBalance;
        private String email;
        private String phoneNumber;
        private String alternativePhoneNumber;
        private String status;
        private LocalDate date;
        @CreationTimestamp
        private LocalDate createdAt;
        @UpdateTimestamp
        private LocalDateTime modifiedAt;
        private String password;

        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
        private Set<Role> roles;

    }


