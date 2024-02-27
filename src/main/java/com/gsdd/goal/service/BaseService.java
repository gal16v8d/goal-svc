package com.gsdd.goal.service;

import com.gsdd.goal.converter.GenericConverter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseService<T, D> {

  String getSortArg();

  /**
   * Keep old entity id for update operation.
   *
   * @param entityNew, build according new payload
   * @param entityOrig, database mapped entity
   * @return entityNew fields but with entityOrig id.
   */
  T replaceId(T entityNew, T entityOrig);

  JpaRepository<T, Long> getRepo();

  GenericConverter<T, D> getConverter();

  void validateData(D userInput);

  T validateEntity(T mergedEntity);

  default List<D> getAll() {
    return getRepo().findAll(Sort.by(getSortArg()))
        .stream()
        .map(getConverter()::convertToDomain)
        .toList();
  }

  default D getById(Long id) {
    return getRepo().findById(id).map(getConverter()::convertToDomain).orElse(null);
  }

  default D save(D model) {
    validateData(model);
    return Optional.ofNullable(model)
        .map(getConverter()::convertToEntity)
        .map(getRepo()::saveAndFlush)
        .map(getConverter()::convertToDomain)
        .orElse(null);
  }

  default D update(Long id, D model) {
    validateData(model);
    return getRepo().findById(id).map((T dbEntity) -> {
      T ent = getConverter().convertToEntity(model);
      return Optional.ofNullable(ent).map((T e) -> {
        e = replaceId(e, dbEntity);
        return getRepo().saveAndFlush(e);
      }).orElse(null);
    }).map(getConverter()::convertToDomain).orElse(null);
  }

  default D patch(Long id, D model) {
    return getRepo().findById(id)
        .map(dbEntity -> getConverter().mapToEntity(model, dbEntity))
        .map(this::validateEntity)
        .map((T e) -> getRepo().saveAndFlush(e))
        .map(getConverter()::convertToDomain)
        .orElse(null);
  }

  default Long delete(Long id) {
    return getRepo().findById(id).map((T entity) -> {
      getRepo().delete(entity);
      return id;
    }).orElse(null);
  }
}
