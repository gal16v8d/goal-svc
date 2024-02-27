package com.gsdd.goal.factory;

import com.gsdd.goal.model.GoalModel;
import com.gsdd.goal.persistence.entities.Goal;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GoalFactory {
  public static final Long ID = 1L;
  public static final Integer YEAR = 2024;
  public static final String DESCRIPTION = "My goal";

  public static Goal getGoal() {
    return Goal.builder()
        .goalId(ID)
        .forYear(YEAR)
        .ready(false)
        .description(DESCRIPTION)
        .life(false)
        .user(UserFactory.getUser())
        .build();
  }

  public static GoalModel getGoalModel() {
    return GoalModel.builder()
        .goalId(ID)
        .forYear(YEAR)
        .ready(false)
        .description(DESCRIPTION)
        .life(false)
        .userId(ID)
        .build();
  }
}
