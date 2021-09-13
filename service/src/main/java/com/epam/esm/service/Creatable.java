package com.epam.esm.service;

/** Interface with the ability to create an object. */
public interface Creatable<T> {
  /**
   * Create object.
   *
   * @param dto the dto object to be placed in the database
   * @return The created object
   */
  T create(T dto);
}
