package com.maimai.billingcalculationengine.repository;

import com.maimai.billingcalculationengine.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAll();

    Optional<Client> findByClientId(String clientId);
}
