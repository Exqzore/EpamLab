package com.epam.esm.service;

/** The general interface service for all objects. */
public interface GeneralService<T> {
  /**
   * Find count of pages.
   *
   * @param size size of page
   * @return Count of pages
   */
  int getCountOfPages(int size);

  /**
   * Find object by id.
   *
   * @param id object id
   * @return The founded object
   */
  T findById(Long id);
}
