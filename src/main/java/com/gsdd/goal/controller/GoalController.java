package com.gsdd.goal.controller;

import com.gsdd.goal.model.GoalModel;
import com.gsdd.goal.persistence.entities.Goal;
import com.gsdd.goal.service.BaseService;
import com.gsdd.goal.service.GoalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Goal CRUD operations")
@RequiredArgsConstructor
@RefreshScope
@RestController
@RequestMapping("api/goals")
public class GoalController implements BaseController<Goal, GoalModel> {

  private final GoalService goalService;

  @Override
  public Long getId(GoalModel model) {
    return model.getGoalId();
  }

  @Override
  public BaseService<Goal, GoalModel> getService() {
    return goalService;
  }
}
