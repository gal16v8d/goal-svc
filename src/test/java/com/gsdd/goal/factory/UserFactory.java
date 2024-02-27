package com.gsdd.goal.factory;

import com.gsdd.goal.model.UserModel;
import com.gsdd.goal.persistence.entities.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserFactory {
  public static final Long ID = 1L;
  public static final String USER_NAME = "Aaa";

  public static User getUser() {
    return User.builder().userId(ID).name(USER_NAME).build();
  }

  public static UserModel getUserModel() {
    return UserModel.builder().userId(ID).name(USER_NAME).build();
  }
}
