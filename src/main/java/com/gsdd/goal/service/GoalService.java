package com.gsdd.goal.service;

import com.gsdd.goal.converter.GenericConverter;
import com.gsdd.goal.converter.GoalMapper;
import com.gsdd.goal.exception.BadDataException;
import com.gsdd.goal.model.GoalModel;
import com.gsdd.goal.persistence.entities.Goal;
import com.gsdd.goal.repository.GoalRepository;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalService implements BaseService<Goal, GoalModel> {

  public static final String YOU_SHOULD_PROVIDE_YEAR_FOR_THIS_GOAL =
      "You should provide year for this goal";
  public static final String YOU_SHOULDN_T_PROVIDE_YEAR_FOR_A_LIFE_GOAL =
      "You shouldn't provide year for a life goal";
  private final GoalRepository goalRepository;
  private final GoalMapper goalMapper;
  private final UserService userService;

  @Override
  public String getSortArg() {
    return "goalId";
  }

  @Override
  public Goal replaceId(Goal entityNew, Goal entityOrig) {
    entityNew.setGoalId(entityOrig.getGoalId());
    return entityNew;
  }

  @Override
  public JpaRepository<Goal, Long> getRepo() {
    return goalRepository;
  }

  @Override
  public GenericConverter<Goal, GoalModel> getConverter() {
    return goalMapper;
  }

  @Override
  public void validateData(GoalModel userInput) {
    var user = userService.getById(userInput.getUserId());
    if (Objects.isNull(user)) {
      throw new BadDataException(
          "User with %s id doesn't exist, please create it".formatted(userInput.getUserId()));
    }
    if (!userInput.isLife() && Objects.isNull(userInput.getForYear())) {
      throw new BadDataException(YOU_SHOULD_PROVIDE_YEAR_FOR_THIS_GOAL);
    }
    if (userInput.isLife() && Objects.nonNull(userInput.getForYear())) {
      throw new BadDataException(YOU_SHOULDN_T_PROVIDE_YEAR_FOR_A_LIFE_GOAL);
    }
  }

  @Override
  public Goal validateEntity(Goal mergedEntity) {
    if (!mergedEntity.isLife() && Objects.isNull(mergedEntity.getForYear())) {
      throw new BadDataException(YOU_SHOULD_PROVIDE_YEAR_FOR_THIS_GOAL);
    }
    if (mergedEntity.isLife() && Objects.nonNull(mergedEntity.getForYear())) {
      throw new BadDataException(YOU_SHOULDN_T_PROVIDE_YEAR_FOR_A_LIFE_GOAL);
    }
    return mergedEntity;
  }

  @Override
  public GoalModel save(GoalModel model) {
    validateData(model);
    var user = userService.getRepo().findById(model.getUserId());
    var goalEntity = Optional.of(model)
            .map(getConverter()::convertToEntity).orElse(null);
    user.ifPresent(goalEntity::setUser);
    return Optional.of(goalEntity)
            .map(getRepo()::saveAndFlush)
            .map(getConverter()::convertToDomain)
            .orElse(null);
  }
}
