package com.sahil.affordmedurlshortener.repository;

import com.sahil.affordmedurlshortener.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {
    Url findByShortcode(String shortcode);
    boolean existsByShortcode(String shortcode);
}
