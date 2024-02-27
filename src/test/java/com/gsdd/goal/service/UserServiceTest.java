package com.gsdd.goal.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.spy;

import com.gsdd.goal.converter.UserMapper;
import com.gsdd.goal.factory.GoalFactory;
import com.gsdd.goal.factory.UserFactory;
import com.gsdd.goal.model.UserModel;
import com.gsdd.goal.persistence.entities.User;
import com.gsdd.goal.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  private static final UserModel MODEL = UserFactory.getUserModel();
  private static final User ENTITY = UserFactory.getUser();
  private UserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;

  @BeforeEach
  void setUp() {
    userService = spy(new UserService(userRepository, userMapper));
  }

  @Test
  void testSortArg() {
    Assertions.assertEquals("userId", userService.getSortArg());
  }

  @Test
  void testReplaceId() {
    var userNew = UserFactory.getUser();
    var userOld = UserFactory.getUser();
    userOld.setUserId(0L);
    var result = userService.replaceId(userNew, userOld);
    Assertions.assertEquals(0L, result.getUserId());
  }

  @Test
  void testGetRepo() {
    Assertions.assertEquals(userRepository, userService.getRepo());
  }

  @Test
  void testGetConverter() {
    Assertions.assertEquals(userMapper, userService.getConverter());
  }

  @Test
  void testGetAll() {
    willReturn(List.of(ENTITY)).given(userRepository).findAll(any(Sort.class));
    willReturn(MODEL).given(userMapper).convertToDomain(any());
    var result = userService.getAll();
    Assertions.assertNotNull(result);
    Assertions.assertFalse(result.isEmpty());
  }

  @Test
  void testGetById() {
    willReturn(Optional.of(ENTITY)).given(userRepository).findById(anyLong());
    willReturn(MODEL).given(userMapper).convertToDomain(any());
    var result = userService.getById(GoalFactory.ID);
    Assertions.assertNotNull(result);
  }

  @Test
  void testGetByIdNotFound() {
    willReturn(Optional.empty()).given(userRepository).findById(anyLong());
    var result = userService.getById(GoalFactory.ID);
    Assertions.assertNull(result);
  }

  @Test
  void testSave() {
    willDoNothing().given(userService).validateData(any());
    willReturn(ENTITY).given(userMapper).convertToEntity(any());
    willReturn(ENTITY).given(userRepository).saveAndFlush(any());
    willReturn(MODEL).given(userMapper).convertToDomain(any());
    var result = userService.save(MODEL);
    Assertions.assertNotNull(result);
  }

  @Test
  void testUpdate() {
    willDoNothing().given(userService).validateData(any());
    willReturn(Optional.of(ENTITY)).given(userRepository).findById(any());
    willReturn(ENTITY).given(userMapper).convertToEntity(any());
    willReturn(ENTITY).given(userRepository).saveAndFlush(any());
    willReturn(MODEL).given(userMapper).convertToDomain(any());
    var result = userService.update(GoalFactory.ID, MODEL);
    Assertions.assertNotNull(result);
  }

  @Test
  void testUpdateNotFound() {
    willDoNothing().given(userService).validateData(any());
    willReturn(Optional.empty()).given(userRepository).findById(any());
    var result = userService.update(GoalFactory.ID, MODEL);
    Assertions.assertNull(result);
  }

  @Test
  void testPatch() {
    willReturn(Optional.of(ENTITY)).given(userRepository).findById(any());
    willReturn(ENTITY).given(userMapper).mapToEntity(any(), any());
    willReturn(ENTITY).given(userService).validateEntity(any());
    willReturn(ENTITY).given(userRepository).saveAndFlush(any());
    willReturn(MODEL).given(userMapper).convertToDomain(any());
    var result = userService.patch(UserFactory.ID, MODEL);
    Assertions.assertNotNull(result);
  }

  @Test
  void testPatchNotFound() {
    willReturn(Optional.empty()).given(userRepository).findById(any());
    var result = userService.patch(GoalFactory.ID, MODEL);
    Assertions.assertNull(result);
  }

  @Test
  void testDelete() {
    willReturn(Optional.of(ENTITY)).given(userRepository).findById(any());
    willDoNothing().given(userRepository).delete(any());
    var result = userService.delete(UserFactory.ID);
    Assertions.assertEquals(UserFactory.ID, result);
  }

  @Test
  void testDeleteNotFound() {
    willReturn(Optional.empty()).given(userRepository).findById(any());
    var result = userService.delete(UserFactory.ID);
    Assertions.assertNull(result);
  }
}
