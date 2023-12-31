package com.virginonline.urlshortener.infrastructure.api;

import com.virginonline.urlshortener.domain.model.LinkInfo;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The `UrlController` class handles URL-related operations.
 */
@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class UrlController {
  private final UrlService urlService;

  @GetMapping("/{code}")
  public Mono<ResponseEntity<LinkInfo>> getLinkByCode(@PathVariable String code) {
    if (code == null || code.isEmpty()) {
      return Mono.just(ResponseEntity.badRequest().build());
    }
    return urlService
        .findByCode(code)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ResponseEntity<LinkInfo>> createLink(@RequestParam String sourceUrl) {
    return urlService
        .saveUrl(sourceUrl)
        .map(link -> ResponseEntity.status(HttpStatus.CREATED).body(link))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping
  public Flux<LinkInfo> getAllLinks() {
    return urlService.getAll();
  }
}
