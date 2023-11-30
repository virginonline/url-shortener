package com.virginonline.urlshortener;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  private static final DockerImageName CASSANDRA_IMAGE = DockerImageName.parse("cassandra:5.0");
  private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7.2-rc-alpine");

  public static GenericContainer<?> redisContainer =
      new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379).withReuse(true);

  public static CassandraContainer<?> cassandraContainer =
      new CassandraContainer<>(CASSANDRA_IMAGE)
          .withInitScript("scripts/init-keyspace.cql")
          .withReuse(true)
          .withExposedPorts(9042);

  @DynamicPropertySource
  static void registerProps(DynamicPropertyRegistry registry) {
    registry.add("spring.cassandra.keyspace-name", () -> "links");
    registry.add("spring.cassandra.port", cassandraContainer::getExposedPorts);
    registry.add(
        "spring.cassandra.contact-points",
        () -> cassandraContainer.getHost() + ":" + cassandraContainer.getMappedPort(9042));
    registry.add("spring.cassandra.local-datacenter", cassandraContainer::getLocalDatacenter);
    registry.add("spring.data.redis.host", redisContainer::getHost);
    registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
  }

  static {
    log.info("containers started");
    Startables.deepStart(Stream.of(redisContainer, cassandraContainer)).join();
  }

  @Test
  void containersIsRunning() {
    assertThat(redisContainer.isRunning() && cassandraContainer.isRunning()).isTrue();
  }
}
