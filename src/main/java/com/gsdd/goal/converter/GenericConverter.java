package com.gsdd.goal.converter;

public interface GenericConverter<T, D> {

  D convertToDomain(T entity);

  T convertToEntity(D model);

  T mapToEntity(D model, T oldEntity);
}
