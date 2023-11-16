package com.virginonline.urlshortener.infrastructure.service.impl;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.virginonline.urlshortener.domain.model.LinkInfo;
import com.virginonline.urlshortener.domain.repository.UrlRepository;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import com.virginonline.urlshortener.util.KeyGenerator;
import java.time.Instant;
import java.time.LocalDateTime;
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
  private final KeyGenerator keyGenerator;

  @Override
  public Mono<LinkInfo> findByCode(String code) {
    log.info("searching link by {}", code);
    return urlRepository.findByCode(code);
  }

  @Override
  public Mono<LinkInfo> saveUrl(String url) {
    log.info("save url {}", url);
    return keyGenerator
        .generateLink()
        .flatMap(
            key -> {
              UUID id = Uuids.timeBased();
              LinkInfo linkInfo =
                  LinkInfo.builder()
                      .id(id)
                      .source(url)
                      .createdDate(LocalDateTime.now())
                      .code(key)
                      .build();
              return Mono.just(linkInfo);
            })
        .flatMap(urlRepository::save);
  }

  @Override
  public Collection<LinkInfo> getAll() {
    log.info("Fetching all urls");
    return urlRepository.findAll().toStream().collect(Collectors.toList());
  }
}
