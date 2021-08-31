package com.epam.esm.service.impl;

import com.epam.esm.constant.CustomHttpStatus;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.specification.tag.FindAllTagsSpecification;
import com.epam.esm.dao.specification.tag.FindTagsByCertificateIdSpecification;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CreationException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.util.TagFormatInterpreter;
import com.epam.esm.validator.StringIdValidator;
import com.epam.esm.validator.TagDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
  private final String NOT_FOUND_TAG = "Requested tag not found (id = %s)";
  private final String NOT_CREATED_TAG = "Failed to create tag";

  private final TagDao tagDao;
  private final TagFormatInterpreter interpreter;

  @Autowired
  public TagServiceImpl(TagDao tagDao, TagFormatInterpreter interpreter) {
    this.tagDao = tagDao;
    this.interpreter = interpreter;
  }

  @Override
  public List<TagDto> findAll() {
    return tagDao.findBySpecification(new FindAllTagsSpecification()).stream()
        .map(interpreter::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<TagDto> findByCertificateId(long certificateId) {
    return tagDao
        .findBySpecification(new FindTagsByCertificateIdSpecification(certificateId))
        .stream()
        .map(interpreter::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public TagDto findById(String id) {
    List<String> errors = StringIdValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.TAG);
    }
    return tagDao
        .findById(Long.parseLong(id))
        .map(interpreter::toDto)
        .orElseThrow(
            () ->
                new NotFoundException(
                    List.of(NOT_FOUND_TAG.formatted(id)), CustomHttpStatus.NOT_FOUND.TAG));
  }

  @Override
  public TagDto create(TagDto tagDto) {
    List<String> errors = TagDtoValidator.tagDtoValidate(tagDto);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.TAG);
    }
    return tagDao
        .save(interpreter.fromDto(tagDto))
        .map(interpreter::toDto)
        .orElseThrow(() -> new CreationException(NOT_CREATED_TAG, CustomHttpStatus.CONFLICT.TAG));
  }

  @Override
  public void removeById(String id) {
    List<String> errors = StringIdValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.TAG);
    }
    tagDao.removeById(Long.parseLong(id));
  }

  @Override
  public List<TagDto> createTagsOnlyByNameOrId(List<TagDto> tags) {
    List<Tag> createdTags = new ArrayList<>();
    tags.forEach(
        tagDto -> {
          Optional<Tag> tagOptional = Optional.empty();
          if (tagDto.getId() != null && tagDao.isExists(tagDto.getId())) {
            tagDao.setDeleted(tagDto.getId(), false);
            tagOptional = tagDao.findById(tagDto.getId());
          } else if (tagDto.getName() != null) {
            if (tagDao.isExists(tagDto.getName())) {
              tagDao.setDeleted(tagDto.getName(), false);
              tagOptional = tagDao.findByName(tagDto.getName());
            } else {
              tagOptional = tagDao.save(interpreter.fromDto(tagDto));
            }
          }
          tagOptional.ifPresent(createdTags::add);
        });
    return createdTags.stream().map(interpreter::toDto).collect(Collectors.toList());
  }

  @Override
  public boolean isExists(String name) {
    return tagDao.isExists(name);
  }

  @Override
  public boolean isExists(long id) {
    return tagDao.isExists(id);
  }
}
