package com.epam.esm.dao;

import com.epam.esm.dao.specification.Specification;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/** The interface Tag service. */
public interface TagDao {
  /**
   * Find tags by specification.
   *
   * @return A list of tags
   */
  List<Tag> findBySpecification(Specification specification);

  /**
   * Find by tag id.
   *
   * @param id the tag id
   * @return The founded tag
   */
  Optional<Tag> findById(long id);

  /**
   * Find by tag name.
   *
   * @param name the tag name
   * @return The founded tag
   */
  Optional<Tag> findByName(String name);

  /**
   * Create tag.
   *
   * @param tag the tag to be placed in the database
   * @return The created tag
   */
  Optional<Tag> save(Tag tag);

  /**
   * Remove tag by id.
   *
   * @param id the tag id
   */
  void removeById(long id);

  /**
   * Remove tag by name.
   *
   * @param name the tag name
   */
  void removeByName(String name);

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
   * Checks if a tag exists.
   *
   * @param name the tag name
   * @return True if tag is exists
   */
  boolean isExists(String name);

  /**
   * Checks if a tag exists.
   *
   * @param id the tag id
   * @return True if tag is exists
   */
  boolean isExists(long id);
}
