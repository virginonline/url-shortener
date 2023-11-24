package com.virginonline.urlshortener.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.virginonline.urlshortener.domain.model.LinkInfo;
import com.virginonline.urlshortener.infrastructure.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UrlControllerTest {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UrlService urlService;
  private final String URL = "https://example.com";

  @ServiceConnection @Container
  static CassandraContainer<?> cassandraContainer =
      new CassandraContainer<>("cassandra:5.0").withInitScript("scripts/init-keyspace.cql");

  @Test
  void connectionEstablished() {
    assertThat(cassandraContainer.isCreated()).isTrue();
    assertThat(cassandraContainer.isRunning()).isTrue();
  }

  @Test
  void code() {
    var code = urlService.saveUrl(URL).block().getCode();
    webTestClient
        .get()
        .uri(String.format("api/v1/%s", code))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(LinkInfo.class);
  }

  @Test
  void createLink() {
    webTestClient
        .post()
        .uri(String.format("api/v1/create?source=%s", URL))
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(LinkInfo.class);
  }

  @Test
  void fetchAllTokens() {
    webTestClient.get().uri("api/v1/").exchange().expectStatus().isOk().expectBody();
  }
}
