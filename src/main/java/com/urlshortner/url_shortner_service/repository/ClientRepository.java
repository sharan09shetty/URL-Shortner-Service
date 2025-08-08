package com.urlshortner.url_shortner_service.repository;

import com.urlshortner.url_shortner_service.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
    ClientEntity findByClientIdentifier(String clientIdentifier);
}
