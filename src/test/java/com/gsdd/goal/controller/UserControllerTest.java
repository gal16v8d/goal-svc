package com.gsdd.goal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsdd.goal.factory.UserFactory;
import com.gsdd.goal.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
  private static final String PATH = "/api/users";
  private static final String PATH_ID = PATH + "/1";
  private static final String NAME_NODE = "$.name";
  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private UserService userService;

  @Test
  void testGetAll() throws Exception {
    willReturn(List.of(UserFactory.getUser())).given(userService).getAll();
    mvc.perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void testGetById() throws Exception {
    willReturn(UserFactory.getUser()).given(userService).getById(anyLong());
    mvc.perform(get(PATH_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath(NAME_NODE).value(UserFactory.USER_NAME));
  }

  @Test
  void testSave() throws Exception {
    var model = UserFactory.getUserModel();
    willReturn(model).given(userService).save(any());
    mvc.perform(
        post(PATH).contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath(NAME_NODE).value(UserFactory.USER_NAME));
  }

  @Test
  void testSaveNotValid() throws Exception {
    var model = UserFactory.getUserModel();
    model.setName("");
    willReturn(model).given(userService).save(any());
    mvc.perform(
        post(PATH).contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(jsonPath("$.detail").isNotEmpty());
  }

  @Test
  void testUpdate() throws Exception {
    var model = UserFactory.getUserModel();
    willReturn(model).given(userService).update(anyLong(), any());
    mvc.perform(
        put(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(NAME_NODE).value(UserFactory.USER_NAME));
  }

  @Test
  void testUpdateNotFound() throws Exception {
    var model = UserFactory.getUserModel();
    willReturn(null).given(userService).update(anyLong(), any());
    mvc.perform(
        put(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testPatch() throws Exception {
    var model = UserFactory.getUserModel();
    willReturn(model).given(userService).patch(anyLong(), any());
    mvc.perform(
        patch(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(NAME_NODE).value(UserFactory.USER_NAME));
  }

  @Test
  void testPatchNotFound() throws Exception {
    var model = UserFactory.getUserModel();
    willReturn(null).given(userService).patch(anyLong(), any());
    mvc.perform(
        patch(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDelete() throws Exception {
    willReturn(UserFactory.ID).given(userService).delete(anyLong());
    mvc.perform(delete(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteNotFound() throws Exception {
    willReturn(null).given(userService).delete(anyLong());
    mvc.perform(delete(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }
}
