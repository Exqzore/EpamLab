package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

/** The interface Tag service. */
public interface TagService extends GeneralService<TagDto>, Creatable<TagDto>, Deletable {
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
