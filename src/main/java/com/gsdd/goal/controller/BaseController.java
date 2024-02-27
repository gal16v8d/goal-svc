package com.gsdd.goal.controller;

import com.gsdd.goal.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BaseController<T, D> {

  Long getId(D model);

  BaseService<T, D> getService();

  @Operation(summary = "Allows to retrieve all")
  @GetMapping
  default ResponseEntity<Collection<D>> getAll() {
    return ResponseEntity.ok(getService().getAll());
  }

  @Operation(summary = "Retrieve a single record by id",
      responses = {
          @ApiResponse(responseCode = "200", description = "Matching data"),
          @ApiResponse(responseCode = "404", description = "Can not find any data by given id")})
  @GetMapping("{id:[0-9]+}")
  default ResponseEntity<D> getById(@PathVariable("id") Long id) {
    return Optional.ofNullable(getService().getById(id))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Store given data",
      responses = {
          @ApiResponse(responseCode = "200", description = "Save success"),
          @ApiResponse(responseCode = "400",
              description = "If some missing data or wrong payload")})
  @PostMapping
  default ResponseEntity<D> save(@Valid @RequestBody D model) {
    return Optional.ofNullable(getService().save(model))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Fully updates matching data",
      responses = {
          @ApiResponse(responseCode = "200", description = "Update success"),
          @ApiResponse(responseCode = "400", description = "If some missing data or wrong payload"),
          @ApiResponse(responseCode = "404", description = "Can not find any data by given id")})
  @PutMapping("{id:[0-9]+}")
  default ResponseEntity<D> update(@PathVariable("id") Long id, @Valid @RequestBody D model) {
    return Optional.ofNullable(getService().update(id, model))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Partial update matching data",
      responses = {
          @ApiResponse(responseCode = "200", description = "Update success"),
          @ApiResponse(responseCode = "400", description = "If some missing data or wrong payload"),
          @ApiResponse(responseCode = "404", description = "Can not find any data by given id")})
  @PatchMapping("{id:[0-9]+}")
  default ResponseEntity<D> patch(@PathVariable("id") Long id, @RequestBody D model) {
    return Optional.ofNullable(getService().patch(id, model))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Delete matching data",
      responses = {
          @ApiResponse(responseCode = "204", description = "Delete success"),
          @ApiResponse(responseCode = "404", description = "Can not find any data by given id")})
  @DeleteMapping("{id:[0-9]+}")
  default ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    return Optional.ofNullable(getService().delete(id))
        .map(result -> ResponseEntity.noContent().build())
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
