package com.urlshortner.url_shortner_service.repository;

import com.urlshortner.url_shortner_service.entity.ShortenedUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrlEntity, UUID> {
    ShortenedUrlEntity findByShortcode(String shortcode);
}
