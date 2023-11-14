package com.virginonline.urlshortener.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@Table("tokens")
@ToString
public class Token {
  @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
  private UUID id;

  private String source;

  private String code;

  @CreatedDate private Instant createdDate;
  private Boolean isActive;
}
