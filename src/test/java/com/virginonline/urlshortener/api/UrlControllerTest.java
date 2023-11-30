package com.virginonline.urlshortener.api;

import com.virginonline.urlshortener.AbstractIntegrationTest;
import com.virginonline.urlshortener.domain.model.LinkInfo;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@Slf4j
class UrlControllerTest extends AbstractIntegrationTest {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UrlService urlService;
  private final String URL = "https://example.com";

  @Test
  void code() {
    var code = urlService.saveUrl(URL).block();
    log.info("code created: {}", code);
    webTestClient
        .get()
        .uri(String.format("api/v1/%s", code.getCode()))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(LinkInfo.class);
  }

  @Test
  void createLink() {
    webTestClient
        .post()
        .uri(String.format("api/v1/create?sourceUrl=%s", URL))
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(LinkInfo.class);
  }

  @Test
  void fetchAllTokens() {
    webTestClient.get().uri("api/v1/").exchange().expectStatus().isOk().expectBody();
  }
}
