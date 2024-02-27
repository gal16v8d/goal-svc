package com.gsdd.goal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
public class UserModel {

  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
  private Long userId;
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "jss")
  @NotEmpty(message = "name should not be empty")
  private String name;
}
