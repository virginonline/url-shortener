package com.virginonline.urlshortener.infrastructure.api;

import com.virginonline.urlshortener.domain.model.LinkInfo;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class UrlController {
  private final UrlService urlService;

  @GetMapping("/{code}")
  public Mono<ResponseEntity<LinkInfo>> code(@PathVariable String code) {
    return urlService
        .findByCode(code)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<LinkInfo>> createLink(@RequestParam String source) {
    return urlService
        .saveUrl(source)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping
  public Collection<LinkInfo> fetchAllTokens() {
    return urlService.getAll();
  }
}
