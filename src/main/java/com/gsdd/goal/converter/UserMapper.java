package com.gsdd.goal.converter;

import com.gsdd.goal.model.UserModel;
import com.gsdd.goal.persistence.entities.User;
import java.util.Optional;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends GenericConverter<User, UserModel> {

  UserModel convertToDomain(User user);

  User convertToEntity(UserModel model);

  default User mapToEntity(UserModel model, User oldEntity) {
    var newEntity = User.builder().userId(oldEntity.getUserId()).build();
    var modelOp = Optional.ofNullable(model);
    newEntity.setName(modelOp.map(UserModel::getName).orElseGet(oldEntity::getName));
    return newEntity;
  }
}
