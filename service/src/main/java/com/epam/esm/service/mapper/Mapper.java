package com.epam.esm.service.mapper;

/** Entity-to-dto translator interface and vice versa. */
public interface Mapper<T, R> {
  /**
   * Entity-to-dto translate.
   *
   * @param entity entity object
   * @return Dto object
   */
  R mapToDto(T entity, boolean mapNestedEntities);

  /**
   * Dto-to-entity translate.
   *
   * @param dto dto object
   * @return Entity object
   */
  T mapFromDto(R dto);
}
