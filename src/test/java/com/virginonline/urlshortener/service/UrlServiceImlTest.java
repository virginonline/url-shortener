package com.virginonline.urlshortener.service;


import com.virginonline.urlshortener.AbstractIntegrationTest;
import com.virginonline.urlshortener.infrastructure.service.impl.UrlServiceIml;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

class UrlServiceImlTest extends AbstractIntegrationTest {
  private final String URL = "https://example.com";

  @Autowired private UrlServiceIml urlService;

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
  }
}
