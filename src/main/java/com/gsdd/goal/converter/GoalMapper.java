package com.gsdd.goal.converter;

import com.gsdd.goal.model.GoalModel;
import com.gsdd.goal.persistence.entities.Goal;
import com.gsdd.goal.persistence.entities.User;
import java.util.Optional;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GoalMapper extends GenericConverter<Goal, GoalModel> {

  @Mapping(target = "userId", source = "user.userId")
  GoalModel convertToDomain(Goal goal);

  @Mapping(target = "user", source = "userId", qualifiedByName = "getUserFromLong")
  Goal convertToEntity(GoalModel model);

  @Named("getUserFromLong")
  default User getUserFromLong(Long userId) {
    return User.builder().userId(userId).build();
  }

  default Goal mapToEntity(GoalModel model, Goal oldEntity) {
    var newEntity = Goal.builder().goalId(oldEntity.getGoalId()).build();
    var modelOp = Optional.ofNullable(model);
    newEntity.setLife(modelOp.map(GoalModel::isLife).orElseGet(oldEntity::isLife));
    newEntity.setForYear(modelOp.map(GoalModel::getForYear).orElseGet(oldEntity::getForYear));
    newEntity.setReady(modelOp.map(GoalModel::isReady).orElseGet(oldEntity::isReady));
    newEntity.setReadyDate(modelOp.map(GoalModel::getReadyDate).orElseGet(oldEntity::getReadyDate));
    newEntity.setEstimateReadyDate(
        modelOp.map(GoalModel::getEstimateReadyDate).orElseGet(oldEntity::getEstimateReadyDate));
    return newEntity;
  }
}
