package com.virginonline.urlshortener.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.virginonline.urlshortener.infrastructure.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
class UrlServiceImlTest {
  @ServiceConnection @Container
  static CassandraContainer<?> cassandraContainer =
      new CassandraContainer<>("cassandra:5.0").withInitScript("scripts/init-keyspace.cql");

  @Autowired
  private UrlService urlService;

  @Test
  void connectionEstablished() {
    assertThat(cassandraContainer.isCreated()).isTrue();
    assertThat(cassandraContainer.isRunning()).isTrue();
  }

  @Test
  void findByCode() {
    String url = "https://example.com";
    var result = urlService.saveUrl(url);
    StepVerifier.create(result).expectNextCount(1).expectComplete().verify();
    String savedCode = result.block().getCode();

    var foundLinkMono = urlService.findByCode(savedCode);

    StepVerifier.create(foundLinkMono).expectNextCount(1).expectComplete().verify();
  }

  @Test
  void saveUrl() {
    String url = "https://example.com";
    var result = urlService.saveUrl(url);
    StepVerifier.create(result).expectNextCount(1).expectComplete().verify();
    String savedCode = result.block().getCode();

    var foundLinkMono = urlService.findByCode(savedCode);

    StepVerifier.create(foundLinkMono).expectNextCount(1).expectComplete().verify();
  }

  @Test
  void getAll() {
    String url = "https://example.com";
    var result = urlService.saveUrl(url);

    StepVerifier.create(result).expectNextCount(1).expectComplete().verify();

    var links = urlService.getAll();
    StepVerifier.create(links).expectNextCount(1).expectComplete().verify();
  }



}
