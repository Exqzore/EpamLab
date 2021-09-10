package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

/** The interface Tag service. */
public interface TagService {
  /**
   * Find count of pages.
   *
   * @param size size of page
   * @return Count of pages
   */
  int findPaginated(int size);

  /**
   * Find count of pages.
   *
   * @param size size of page
   * @return Count of pages
   */
  int findTagsOfCertificatePaginated(int size);

  /**
   * Find all tags.
   *
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of tags
   */
  List<TagDto> findAll(int page, int size, List<String> sortParams);

  /**
   * Find tags by certificate id.
   *
   * @param certificateId certificate id
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of tags
   */
  List<TagDto> findByCertificateId(Long certificateId, int page, int size, List<String> sortParams);

  /**
   * Find by tag id.
   *
   * @param id the tag id
   * @return The founded tag
   */
  TagDto findById(Long id);

  /**
   * Create tag.
   *
   * @param tagDto the tag to be placed in the database
   * @return The created tag
   */
  TagDto create(TagDto tagDto);

  /**
   * Remove tag by id.
   *
   * @param id the tag id
   */
  void removeById(Long id);

  /**
   * Create tagS.
   *
   * @param tags tags
   * @return The created tags
   */
  List<TagDto> createTagsOnlyByNameOrId(List<TagDto> tags);

  /**
   * Checks if was there such a tag.
   *
   * @param name the tag name
   * @return True if tag is exists
   */
  boolean isExists(String name);

  /**
   * Checks if was there such a tag.
   *
   * @param id the tag id
   * @return True if tag is exists
   */
  boolean isExists(Long id);

  /**
   * Find wildly used tag.
   *
   * @return The founded tag
   */
  TagDto findWildlyUsed();
}
