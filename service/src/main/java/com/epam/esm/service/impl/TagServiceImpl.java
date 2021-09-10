package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.CreationException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.PaginateValidator;
import com.epam.esm.service.validator.TagDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
  private final TagDao tagDao;
  private final Mapper<Tag, TagDto> mapper;
  private final IdValidator idValidator;
  private final PaginateValidator paginateValidator;
  private final TagDtoValidator tagDtoValidator;

  @Autowired
  public TagServiceImpl(
      TagDao tagDao,
      Mapper<Tag, TagDto> mapper,
      IdValidator idValidator,
      PaginateValidator paginateValidator,
      TagDtoValidator tagDtoValidator) {
    this.tagDao = tagDao;
    this.mapper = mapper;
    this.idValidator = idValidator;
    this.paginateValidator = paginateValidator;
    this.tagDtoValidator = tagDtoValidator;
  }

  @Override
  @Transactional
  public int findPaginated(int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    int countOfRecords = tagDao.findCountOfRecords();
    int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
    return countPages == 0 ? 1 : countPages;
  }

  @Override
  @Transactional
  public int findTagsOfCertificatePaginated(int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    int countOfRecords = tagDao.findCountOfRecords();
    int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
    return countPages == 0 ? 1 : countPages;
  }

  @Override
  public List<TagDto> findAll(int page, int size, List<String> sortParams) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(paginateValidator.pageValidate(page));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return tagDao.findAll(page, size, sortParams).stream()
        .map(tag -> mapper.mapToDto(tag, false))
        .collect(Collectors.toList());
  }

  @Override
  public List<TagDto> findByCertificateId(
      Long certificateId, int size, int page, List<String> sortParams) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(paginateValidator.pageValidate(page));
    errors.addAll(idValidator.idValidate(certificateId));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return tagDao.findByCertificateId(certificateId, size, page, sortParams).stream()
        .map(tag -> mapper.mapToDto(tag, false))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public TagDto findById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Tag tag = tagDao.findById(id);
    if (tag == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.TAG.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NOT_FOUND).formatted(id)));
    }
    return mapper.mapToDto(tag, true);
  }

  @Override
  @Transactional
  public TagDto create(TagDto tagDto) {
    List<ErrorMessageDto> errors = tagDtoValidator.tagDtoValidate(tagDto);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Tag tag = tagDao.save(mapper.mapFromDto(tagDto));
    if (tag == null) {
      throw new CreationException(
          new ErrorMessageDto(
              CustomHttpStatus.Conflict.TAG.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NOT_CREATE)));
    }
    return mapper.mapToDto(tag, false);
  }

  @Override
  @Transactional
  public void removeById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Tag tag = tagDao.findById(id);
    if (tag == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.TAG.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NOT_FOUND).formatted(id)));
    }
    try {
      tagDao.removeById(id);
    } catch (NoSuchResultException exception) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.TAG.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NOT_FOUND).formatted(id)));
    }
  }

  @Override
  @Transactional
  public List<TagDto> createTagsOnlyByNameOrId(List<TagDto> tags) {
    List<ErrorMessageDto> errors = tagDtoValidator.tagsDtoValidate(tags);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Set<TagDto> tagDtoSet = new HashSet<>(tags);
    List<TagDto> createdTags = new ArrayList<>();
    tagDtoSet.forEach(
        tag -> {
          Tag createdTag = null;
          if (tag.getId() != null && isExists(tag.getId())) {
            tagDao.setDeleted(tag.getId(), false);
            createdTag = tagDao.findById(tag.getId());
          } else if (tag.getName() != null) {
            if (isExists(tag.getName())) {
              tagDao.setDeleted(tag.getName(), false);
              createdTag = tagDao.findByName(tag.getName());
            } else {
              createdTag = tagDao.save(mapper.mapFromDto(tag));
            }
          }
          if (createdTag != null) {
            createdTags.add(mapper.mapToDto(createdTag, false));
          }
        });
    return createdTags;
  }

  @Override
  @Transactional
  public TagDto findWildlyUsed() {
    Tag tag = tagDao.findWildlyUsed();
    if (tag == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.TAG.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NOT_FOUND_WITHOUT_ID)));
    }
    return mapper.mapToDto(tag, true);
  }

  @Override
  @Transactional
  public boolean isExists(String name) {
    return tagDao.findByName(name) != null || tagDao.isDeletedExists(name);
  }

  @Override
  @Transactional
  public boolean isExists(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return tagDao.findById(id) != null || tagDao.isDeletedExists(id);
  }
}
