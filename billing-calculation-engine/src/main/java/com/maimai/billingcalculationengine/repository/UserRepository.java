package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndActiveTrue(String email);
    Optional<User> findByEmailAndActiveFalse(String email);
}
