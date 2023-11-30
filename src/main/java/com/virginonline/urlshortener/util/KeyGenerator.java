package com.virginonline.urlshortener.util;

import com.virginonline.urlshortener.domain.repository.UrlRepository;
import java.util.Random;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * The {@code KeyGenerator} class is responsible for generating unique short links asynchronously
 * using characters from a predefined dictionary.
 *
 * <p>It relies on dependency injection to access the {@link UrlRepository} for checking the
 * existence of generated codes. The link length is configurable through the application properties.
 *
 * <p>The generation process is deferred until subscription, ensuring that a new code is generated
 * for each subscription. If a generated code already exists in the repository, the generation
 * process is retried recursively until a unique code is found.
 *
 * <p>The plan will be to rewrite this algorithm and put it in a separate KGS Service
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class KeyGenerator {

  private final UrlRepository urlRepository;
  private final Random random = new Random();

  @Value("${app.url-length}")
  private int codeLength;

  /**
   * Generates a unique short link asynchronously.
   *
   * @return a {@link reactor.core.publisher.Mono} emitting the generated short link
   */
  public Mono<String> generateLink() {
    log.info("Generating short links...");
    return Mono.defer(
        () -> {
          char[] generatedCode = new char[codeLength];

          for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(CharactersDictionary.CHARACTERS.length - 1);
            generatedCode[i] = CharactersDictionary.CHARACTERS[randomIndex];
          }

          String code = new String(generatedCode);

          return urlRepository
              .existsByCode(code)
              .flatMap(codeExists -> codeExists ? generateLink() : Mono.just(code));
        });
  }
}
