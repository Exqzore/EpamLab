package com.epam.esm.service;

/** Interface with the ability to delete an object by id. */
public interface Deletable {
  /**
   * Remove object by id.
   *
   * @param id the object id
   */
  void removeById(Long id);
}
