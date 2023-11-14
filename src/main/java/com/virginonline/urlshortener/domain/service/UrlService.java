package com.virginonline.urlshortener.domain.service;

import com.virginonline.urlshortener.domain.model.Token;

import java.util.Collection;
import reactor.core.publisher.Mono;

public interface UrlService {
  Mono<Token> findByCode(String code);

  Mono<Token> saveUrl(String url);

  Collection<Token> getAll();
}
