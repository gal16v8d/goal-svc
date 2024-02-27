package com.gsdd.goal.service;

import com.gsdd.goal.converter.GenericConverter;
import com.gsdd.goal.converter.UserMapper;
import com.gsdd.goal.model.UserModel;
import com.gsdd.goal.persistence.entities.User;
import com.gsdd.goal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements BaseService<User, UserModel> {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public String getSortArg() {
    return "userId";
  }

  @Override
  public User replaceId(User entityNew, User entityOrig) {
    entityNew.setUserId(entityOrig.getUserId());
    return entityNew;
  }

  @Override
  public JpaRepository<User, Long> getRepo() {
    return userRepository;
  }

  @Override
  public GenericConverter<User, UserModel> getConverter() {
    return userMapper;
  }

  @Override
  public void validateData(UserModel userInput) {
    // No additional validation here
  }

  @Override
  public User validateEntity(User mergedEntity) {
    // No additional validation here
    return mergedEntity;
  }
}
