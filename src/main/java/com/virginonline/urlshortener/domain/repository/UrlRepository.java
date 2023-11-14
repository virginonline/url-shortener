package com.virginonline.urlshortener.domain.repository;

import com.virginonline.urlshortener.domain.model.Token;
import java.util.UUID;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

public interface UrlRepository extends ReactiveCassandraRepository<Token, UUID> {

  @Query(allowFiltering = true)
  Mono<Token> findByCode(String code);
}
