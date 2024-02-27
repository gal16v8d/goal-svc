package com.gsdd.goal.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.spy;

import com.gsdd.goal.converter.GoalMapper;
import com.gsdd.goal.exception.BadDataException;
import com.gsdd.goal.factory.GoalFactory;
import com.gsdd.goal.factory.UserFactory;
import com.gsdd.goal.model.GoalModel;
import com.gsdd.goal.model.UserModel;
import com.gsdd.goal.persistence.entities.Goal;
import com.gsdd.goal.persistence.entities.User;
import com.gsdd.goal.repository.GoalRepository;
import java.util.List;
import java.util.Optional;

import com.gsdd.goal.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {
  private static final GoalModel MODEL = GoalFactory.getGoalModel();
  private static final Goal ENTITY = GoalFactory.getGoal();
  private static final UserModel USER_MODEL = UserFactory.getUserModel();
  private static final User USER_ENTITY = UserFactory.getUser();
  private GoalService goalService;
  @Mock
  private GoalRepository goalRepository;
  @Mock
  private GoalMapper goalMapper;
  @Mock
  private UserService userService;

  @BeforeEach
  void setUp() {
    goalService = spy(new GoalService(goalRepository, goalMapper, userService));
  }

  @Test
  void testSortArg() {
    Assertions.assertEquals("goalId", goalService.getSortArg());
  }

  @Test
  void testReplaceId() {
    var goalNew = GoalFactory.getGoal();
    var goalOld = GoalFactory.getGoal();
    goalOld.setGoalId(0L);
    var result = goalService.replaceId(goalNew, goalOld);
    Assertions.assertEquals(0L, result.getGoalId());
  }

  @Test
  void testGetRepo() {
    Assertions.assertEquals(goalRepository, goalService.getRepo());
  }

  @Test
  void testGetConverter() {
    Assertions.assertEquals(goalMapper, goalService.getConverter());
  }

  @Test
  void testValidateDataNoUser() {
    willReturn(null).given(userService).getById(anyLong());
    Assertions.assertThrows(BadDataException.class, () -> goalService.validateData(MODEL));
  }

  @Test
  void testValidateDataLifeFalseAndNotYear() {
    willReturn(USER_MODEL).given(userService).getById(anyLong());
    var model = GoalFactory.getGoalModel();
    model.setLife(false);
    model.setForYear(null);
    Assertions.assertThrows(BadDataException.class, () -> goalService.validateData(model));
  }

  @Test
  void testValidateDataLifeAndYear() {
    willReturn(USER_MODEL).given(userService).getById(anyLong());
    var model = GoalFactory.getGoalModel();
    model.setLife(true);
    model.setForYear(2024);
    Assertions.assertThrows(BadDataException.class, () -> goalService.validateData(model));
  }

  @Test
  void testValidateEntity() {
    Assertions.assertEquals(ENTITY, goalService.validateEntity(ENTITY));
  }

  @Test
  void testValidateEntityLifeFalseAndNotYear() {
    var entity = GoalFactory.getGoal();
    entity.setLife(false);
    entity.setForYear(null);
    Assertions.assertThrows(BadDataException.class, () -> goalService.validateEntity(entity));
  }

  @Test
  void testValidateEntityLifeAndYear() {
    var entity = GoalFactory.getGoal();
    entity.setLife(true);
    entity.setForYear(2024);
    Assertions.assertThrows(BadDataException.class, () -> goalService.validateEntity(entity));
  }

  @Test
  void testGetAll() {
    willReturn(List.of(ENTITY)).given(goalRepository).findAll(any(Sort.class));
    willReturn(MODEL).given(goalMapper).convertToDomain(any());
    var result = goalService.getAll();
    Assertions.assertNotNull(result);
    Assertions.assertFalse(result.isEmpty());
  }

  @Test
  void testGetById() {
    willReturn(Optional.of(ENTITY)).given(goalRepository).findById(anyLong());
    willReturn(MODEL).given(goalMapper).convertToDomain(any());
    var result = goalService.getById(GoalFactory.ID);
    Assertions.assertNotNull(result);
  }

  @Test
  void testGetByIdNotFound() {
    willReturn(Optional.empty()).given(goalRepository).findById(anyLong());
    var result = goalService.getById(GoalFactory.ID);
    Assertions.assertNull(result);
  }

  @Test
  void testSave(@Mock UserRepository userRepository) {
    willDoNothing().given(goalService).validateData(any());
    willReturn(userRepository).given(userService).getRepo();
    willReturn(Optional.of(USER_ENTITY)).given(userRepository).findById(any());
    willReturn(ENTITY).given(goalMapper).convertToEntity(any());
    willReturn(ENTITY).given(goalRepository).saveAndFlush(any());
    willReturn(MODEL).given(goalMapper).convertToDomain(any());
    var result = goalService.save(MODEL);
    Assertions.assertNotNull(result);
  }

  @Test
  void testUpdate() {
    willDoNothing().given(goalService).validateData(any());
    willReturn(Optional.of(ENTITY)).given(goalRepository).findById(any());
    willReturn(ENTITY).given(goalMapper).convertToEntity(any());
    willReturn(ENTITY).given(goalRepository).saveAndFlush(any());
    willReturn(MODEL).given(goalMapper).convertToDomain(any());
    var result = goalService.update(GoalFactory.ID, MODEL);
    Assertions.assertNotNull(result);
  }

  @Test
  void testUpdateNotFound() {
    willDoNothing().given(goalService).validateData(any());
    willReturn(Optional.empty()).given(goalRepository).findById(any());
    var result = goalService.update(GoalFactory.ID, GoalFactory.getGoalModel());
    Assertions.assertNull(result);
  }

  @Test
  void testPatch() {
    willReturn(Optional.of(ENTITY)).given(goalRepository).findById(any());
    willReturn(ENTITY).given(goalMapper).mapToEntity(any(), any());
    willReturn(ENTITY).given(goalService).validateEntity(any());
    willReturn(ENTITY).given(goalRepository).saveAndFlush(any());
    willReturn(MODEL).given(goalMapper).convertToDomain(any());
    var result = goalService.patch(GoalFactory.ID, MODEL);
    Assertions.assertNotNull(result);
  }

  @Test
  void testPatchNotFound() {
    willReturn(Optional.empty()).given(goalRepository).findById(any());
    var result = goalService.patch(GoalFactory.ID, GoalFactory.getGoalModel());
    Assertions.assertNull(result);
  }

  @Test
  void testDelete() {
    willReturn(Optional.of(ENTITY)).given(goalRepository).findById(any());
    willDoNothing().given(goalRepository).delete(any());
    var result = goalService.delete(GoalFactory.ID);
    Assertions.assertEquals(GoalFactory.ID, result);
  }

  @Test
  void testDeleteNotFound() {
    willReturn(Optional.empty()).given(goalRepository).findById(any());
    var result = goalService.delete(GoalFactory.ID);
    Assertions.assertNull(result);
  }
}
