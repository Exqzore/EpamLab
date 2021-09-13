package com.epam.esm.dao;

import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;

import java.util.List;

/** The interface Tag service. */
public interface TagDao {
  /**
   * Find all tags.
   *
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of tags
   */
  List<Tag> findAll(int page, int size, List<String> sortParams);

  /**
   * Find tags by certificate id.
   *
   * @param certificateId certificate id
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of tags
   */
  List<Tag> findByCertificateId(long certificateId, int page, int size, List<String> sortParams);

  /**
   * Find by tag id.
   *
   * @param id the tag id
   * @return The founded tag
   */
  Tag findById(long id);

  /**
   * Find by tag name.
   *
   * @param name the tag name
   * @return The founded tag
   */
  Tag findByName(String name);

  /**
   * Create tag.
   *
   * @param tag the tag to be placed in the database
   * @return The created tag
   */
  Tag save(Tag tag);

  /**
   * Remove tag by id.
   *
   * @param id the tag id
   */
  void removeById(long id) throws NoSuchResultException;

  /**
   * Remove tag by name.
   *
   * @param name the tag name
   */
  void removeByName(String name) throws NoSuchResultException;

  /**
   * Sets the tag to a delete marker.
   *
   * @param name the tag name
   * @param isDeleted delete marker
   */
  void setDeleted(String name, boolean isDeleted);

  /**
   * Sets the tag to a delete marker.
   *
   * @param id the tag id
   * @param isDeleted delete marker
   */
  void setDeleted(long id, boolean isDeleted);

  /**
   * Checks if a deleted tag exists.
   *
   * @param name the tag name
   * @return True deleted if tag is exists
   */
  boolean isDeletedExists(String name);

  /**
   * Checks if a deleted tag exists.
   *
   * @param id the tag id
   * @return True if deleted tag is exists
   */
  boolean isDeletedExists(long id);

  /**
   * Find wildly used tag.
   *
   * @return The founded tag
   */
  Tag findWildlyUsed();

  /**
   * Find count of tags on db.
   *
   * @return Count of tags
   */
  int findCountOfRecords();
}
