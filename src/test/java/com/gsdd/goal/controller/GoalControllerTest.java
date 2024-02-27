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
import com.gsdd.goal.factory.GoalFactory;
import com.gsdd.goal.service.GoalService;
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
class GoalControllerTest {
  private static final String PATH = "/api/goals";
  private static final String PATH_ID = PATH + "/1";
  private static final String DESCRIPTION_NODE = "$.description";
  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private GoalService goalService;

  @Test
  void testGetAll() throws Exception {
    willReturn(List.of(GoalFactory.getGoal())).given(goalService).getAll();
    mvc.perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void testGetById() throws Exception {
    willReturn(GoalFactory.getGoal()).given(goalService).getById(anyLong());
    mvc.perform(get(PATH_ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath(DESCRIPTION_NODE).value(GoalFactory.DESCRIPTION));
  }

  @Test
  void testSave() throws Exception {
    var model = GoalFactory.getGoalModel();
    willReturn(model).given(goalService).save(any());
    mvc.perform(
        post(PATH).contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath(DESCRIPTION_NODE).value(GoalFactory.DESCRIPTION));
  }

  @Test
  void testSaveNotValid() throws Exception {
    var model = GoalFactory.getGoalModel();
    model.setDescription("");
    willReturn(model).given(goalService).save(any());
    mvc.perform(
        post(PATH).contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(jsonPath("$.detail").isNotEmpty());
  }

  @Test
  void testUpdate() throws Exception {
    var model = GoalFactory.getGoalModel();
    willReturn(model).given(goalService).update(anyLong(), any());
    mvc.perform(
        put(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(DESCRIPTION_NODE).value(GoalFactory.DESCRIPTION));
  }

  @Test
  void testUpdateNotFound() throws Exception {
    var model = GoalFactory.getGoalModel();
    willReturn(null).given(goalService).update(anyLong(), any());
    mvc.perform(
        put(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testPatch() throws Exception {
    var model = GoalFactory.getGoalModel();
    willReturn(model).given(goalService).patch(anyLong(), any());
    mvc.perform(
        patch(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(DESCRIPTION_NODE).value(GoalFactory.DESCRIPTION));
  }

  @Test
  void testPatchNotFound() throws Exception {
    var model = GoalFactory.getGoalModel();
    willReturn(null).given(goalService).patch(anyLong(), any());
    mvc.perform(
        patch(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(model)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDelete() throws Exception {
    willReturn(GoalFactory.ID).given(goalService).delete(anyLong());
    mvc.perform(delete(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteNotFound() throws Exception {
    willReturn(null).given(goalService).delete(anyLong());
    mvc.perform(delete(PATH_ID).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }
}
