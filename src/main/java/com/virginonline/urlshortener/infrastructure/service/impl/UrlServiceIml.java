package com.virginonline.urlshortener.infrastructure.service.impl;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.virginonline.urlshortener.domain.model.LinkInfo;
import com.virginonline.urlshortener.domain.repository.UrlRepository;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import com.virginonline.urlshortener.util.KeyGenerator;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlServiceIml implements UrlService {

  private final UrlRepository urlRepository;
  private final KeyGenerator keyGenerator;

  private final ReactiveRedisTemplate<String, LinkInfo> redisTemplate;

  @Override
  public Mono<LinkInfo> findByCode(String code) {
    return redisTemplate
        .opsForValue()
        .get(code).switchIfEmpty(Mono.defer(() -> urlRepository.findByCode(code)
            .flatMap(linkInfo -> redisTemplate.opsForValue().set(code, linkInfo).thenReturn(linkInfo))));
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
  public Flux<LinkInfo> getAll() {
    log.info("Fetching all urls");
    return urlRepository.findAll();
  }
}
