package com.gsdd.goal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.extern.jackson.Jacksonized;

@Generated
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
public class GoalModel {

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
  private Long goalId;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
  private boolean life;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2024")
  @PositiveOrZero(message = "year should be positive")
  private Integer forYear;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "false")
  private boolean ready;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2024-10-31T23:59:59.999Z")
  @FutureOrPresent
  private ZonedDateTime readyDate;

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2024-10-31T23:59:59.999Z")
  @FutureOrPresent
  private ZonedDateTime estimateReadyDate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Study Python")
  @NotEmpty(message = "description should be provided")
  private String description;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
  @PositiveOrZero(message = "userId should be positive")
  @NotNull(message = "userId should be provided")
  private Long userId;
}
