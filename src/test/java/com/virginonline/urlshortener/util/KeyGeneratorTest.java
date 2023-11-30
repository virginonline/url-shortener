package com.virginonline.urlshortener.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.virginonline.urlshortener.AbstractIntegrationTest;
import com.virginonline.urlshortener.domain.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

class KeyGeneratorTest extends AbstractIntegrationTest {

  @Mock private UrlRepository urlRepository;

  @InjectMocks private KeyGenerator keyGenerator;

  @Test
  public void generateLink() {

    when(urlRepository.existsByCode(anyString())).thenReturn(Mono.just(false));

    Mono<String> result = keyGenerator.generateLink();
    assertNotNull(result);
    assertEquals(result.block().length(), keyGenerator.getCodeLength());
    verify(urlRepository, atLeastOnce()).existsByCode(anyString());
  }
}
