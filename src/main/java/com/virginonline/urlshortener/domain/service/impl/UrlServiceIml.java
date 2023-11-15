package com.virginonline.urlshortener.domain.service.impl;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.virginonline.urlshortener.domain.model.Token;
import com.virginonline.urlshortener.domain.repository.UrlRepository;
import com.virginonline.urlshortener.domain.service.UrlService;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlServiceIml implements UrlService {

  private final UrlRepository urlRepository;

  @Override
  public Mono<Token> findByCode(String code) {
    log.info("searching link by {}", code);
    return urlRepository.findByCode(code);
  }

  @Override
  public Mono<Token> saveUrl(String url) {
    log.info("save url {}", url);
    UUID id = Uuids.timeBased();
    Token token =
        Token.builder()
            .id(id)
            .source(url)
            .code(id.toString().substring(0, 8)) // tmp code
            .createdDate(Instant.now())
            .isActive(true)
            .build();
    return urlRepository.save(token);
  }

  @Override
  public Collection<Token> getAll() {
    log.info("Fetching all urls");
    return urlRepository.findAll().toStream().collect(Collectors.toList());
  }
}
