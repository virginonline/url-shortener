package com.virginonline.urlshortener.infrastructure.service;

import com.virginonline.urlshortener.domain.model.LinkInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UrlService {
  Mono<LinkInfo> findByCode(String code);

  Mono<LinkInfo> saveUrl(String url);

  Flux<LinkInfo> getAll();
}
