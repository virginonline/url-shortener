package com.virginonline.urlshortener.service;

import com.virginonline.urlshortener.AbstractTest;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@Testcontainers
class UrlServiceImlTest extends AbstractTest {
  private final String URL = "https://example.com";

  @Autowired private UrlService urlService;

  @Test
  void findByCode() {
    var result = urlService.saveUrl(URL);
    StepVerifier.create(result).expectNextCount(1).expectComplete().verify();
    String savedCode = result.block().getCode();

    var foundLinkMono = urlService.findByCode(savedCode);

    StepVerifier.create(foundLinkMono).expectNextCount(1).expectComplete().verify();
  }

  @Test
  void saveUrl() {
    var result = urlService.saveUrl(URL);
    StepVerifier.create(result).expectNextCount(1).expectComplete().verify();
    String savedCode = result.block().getCode();

    var foundLinkMono = urlService.findByCode(savedCode);

    StepVerifier.create(foundLinkMono).expectNextCount(1).expectComplete().verify();
  }

  @Test
  void getAll() {

    var result = urlService.saveUrl(URL);

    StepVerifier.create(result).expectNextCount(1).expectComplete().verify();

    var links = urlService.getAll();
    StepVerifier.create(links).expectNextCount(1).expectComplete().verify();
  }
}
