package com.virginonline.urlshortener.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@Table("link_info")
@ToString
public class LinkInfo {
  @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
  private UUID id;

  private String source;

  private String code;

  @Column("created_date")
  @JsonProperty("created_date")
  private LocalDateTime createdDate;
}
