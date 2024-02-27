package com.gsdd.goal.persistence.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractBaseEntity {
  @Version
  private Long version;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  @PrePersist
  public void onCreate() {
    if (createdAt == null) {
      createdAt = ZonedDateTime.now(ZoneOffset.UTC);
    }
  }

  @PreUpdate
  public void onUpdate() {
    updatedAt = ZonedDateTime.now(ZoneOffset.UTC);
  }
}
