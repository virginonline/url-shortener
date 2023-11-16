package com.virginonline.urlshortener.infrastructure.service;

import com.virginonline.urlshortener.domain.model.LinkInfo;

import java.util.Collection;
import reactor.core.publisher.Mono;

public interface UrlService {
  Mono<LinkInfo> findByCode(String code);

  Mono<LinkInfo> saveUrl(String url);

  Collection<LinkInfo> getAll();
}
