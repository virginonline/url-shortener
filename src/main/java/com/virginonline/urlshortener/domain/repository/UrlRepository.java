package com.virginonline.urlshortener.domain.repository;

import com.virginonline.urlshortener.domain.model.LinkInfo;
import java.util.UUID;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

public interface UrlRepository extends ReactiveCassandraRepository<LinkInfo, UUID> {

  @Query(allowFiltering = true)
  Mono<LinkInfo> findByCode(String code);

  @Query(allowFiltering = true)
  Mono<Boolean> existsByCode(String code);
}
