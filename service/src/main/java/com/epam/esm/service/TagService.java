package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

/**
 * The interface Tag service.
 */
public interface TagService {
    /**
     * Find all tags.
     *
     * @return A list of tags
     */
    List<TagDto> findAll();

    /**
     * Find tags by certificate id.
     *
     * @return A list of tags
     */
    List<TagDto> findByCertificateId(long certificateId);

    /**
     * Find by tag id.
     *
     * @param id the tag id
     * @return The founded tag
     */
    TagDto findById(String id);

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
    void removeById(String id);

    /**
     * Create tagS.
     *
     * @param tags tags
     * @return The created tags
     */
    List<TagDto> createTagsOnlyByNameOrId(List<TagDto> tags);

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
