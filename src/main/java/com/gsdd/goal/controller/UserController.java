package com.gsdd.goal.controller;

import com.gsdd.goal.model.UserModel;
import com.gsdd.goal.persistence.entities.User;
import com.gsdd.goal.service.BaseService;
import com.gsdd.goal.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User CRUD operations")
@RequiredArgsConstructor
@RefreshScope
@RestController
@RequestMapping("api/users")
public class UserController implements BaseController<User, UserModel> {

  private final UserService userService;

  @Override
  public Long getId(UserModel model) {
    return model.getUserId();
  }

  @Override
  public BaseService<User, UserModel> getService() {
    return userService;
  }
}
