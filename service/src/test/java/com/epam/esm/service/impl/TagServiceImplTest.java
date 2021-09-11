package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.PaginateValidator;
import com.epam.esm.service.validator.TagDtoValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TagServiceImplTest {
  private TagDao tagDao;
  private Mapper<Tag, TagDto> mapper;
  private IdValidator idValidator;
  private PaginateValidator paginateValidator;
  private TagDtoValidator tagDtoValidator;

  private TagService tagService;

  @BeforeEach
  public void init() {
    tagDao = Mockito.mock(TagDao.class);
    mapper = Mockito.mock(TagMapper.class);
    idValidator = Mockito.mock(IdValidator.class);
    paginateValidator = Mockito.mock(PaginateValidator.class);
    tagDtoValidator = Mockito.mock(TagDtoValidator.class);

    tagService =
        new TagServiceImpl(tagDao, mapper, idValidator, paginateValidator, tagDtoValidator);
  }

  @Test
  void getCountOfPages() {
    // given
    int size = 80;
    int expected = 2;

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findCountOfRecords()).thenReturn(122);
    int actual = tagService.getCountOfPages(size);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(tagDao, Mockito.times(1)).findCountOfRecords();
  }

  @Test
  void findAll() {
    // given
    List<String> sortParams = new ArrayList<>();
    int page = 1;
    int size = 50;
    List<TagDto> expected = new ArrayList<>();

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(paginateValidator.pageValidate(page)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findAll(page, size, sortParams)).thenReturn(Collections.emptyList());
    List<TagDto> actual = tagService.findAll(page, size, sortParams);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(paginateValidator, Mockito.times(1)).pageValidate(page);
    Mockito.verify(tagDao, Mockito.times(1)).findAll(page, size, sortParams);
  }

  @Test
  void findByCertificateId() {
    // given
    long id = 1;
    List<String> sortParams = new ArrayList<>();
    int page = 1;
    int size = 50;
    List<TagDto> expected = new ArrayList<>();

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(paginateValidator.pageValidate(page)).thenReturn(Collections.emptyList());
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findByCertificateId(id, page, size, sortParams))
        .thenReturn(Collections.emptyList());
    List<TagDto> actual = tagService.findByCertificateId(id, page, size, sortParams);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(paginateValidator, Mockito.times(1)).pageValidate(page);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(tagDao, Mockito.times(1)).findByCertificateId(id, page, size, sortParams);
  }

  @Test
  void findById() {
    // given
    long id = 1;
    Tag tag = new Tag(id, "", false);
    TagDto tagDto = new TagDto(id, "");
    TagDto expected = new TagDto(id, "");

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findById(id)).thenReturn(tag);
    Mockito.when(mapper.mapToDto(tag, true)).thenReturn(tagDto);
    TagDto actual = tagService.findById(id);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(tagDao, Mockito.times(1)).findById(id);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(tag, true);
  }

  @Test
  void create() {
    // given
    long id = 1;
    Tag tag = new Tag(id, "", false);
    TagDto tagDto = new TagDto(id, "");
    TagDto expected = new TagDto(id, "");

    // when
    Mockito.when(tagDtoValidator.tagValidate(tagDto)).thenReturn(Collections.emptyList());
    Mockito.when(mapper.mapFromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(tag);
    Mockito.when(mapper.mapToDto(tag, false)).thenReturn(tagDto);
    TagDto actual = tagService.create(tagDto);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(tagDtoValidator, Mockito.times(1)).tagValidate(tagDto);
    Mockito.verify(mapper, Mockito.times(1)).mapFromDto(tagDto);
    Mockito.verify(tagDao, Mockito.times(1)).save(tag);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(tag, false);
  }

  @Test
  void removeById() throws NoSuchResultException {
    // given
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Tag tag = new Tag(id, "", false);

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findById(id)).thenReturn(tag);
    tagService.removeById(id);

    // then
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(tagDao, Mockito.times(1)).findById(id);
    Mockito.verify(tagDao, Mockito.times(1)).removeById(id);
  }

  @Test
  void findWildlyUsed() {
    // given
    long id = 1;
    Tag tag = new Tag(id, "", false);
    TagDto tagDto = new TagDto(id, "");
    TagDto expected = new TagDto(id, "");

    // when
    Mockito.when(tagDao.findWildlyUsed()).thenReturn(tag);
    Mockito.when(mapper.mapToDto(tag, true)).thenReturn(tagDto);
    TagDto actual = tagService.findWildlyUsed();

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(tagDao, Mockito.times(1)).findWildlyUsed();
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(tag, true);
  }

  @Test
  void isExistsById() {
    // given
    long id = 1;
    Tag tag = new Tag(id, "", false);

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findById(id)).thenReturn(tag);
    Mockito.when(tagDao.isDeletedExists(id)).thenReturn(false);
    boolean actual = tagService.isExists(id);

    // then
    Assertions.assertTrue(actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(tagDao, Mockito.times(1)).findById(id);
  }

  @Test
  void isExistsByIdDeleted() {
    // given
    long id = 1;

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.findById(id)).thenReturn(null);
    Mockito.when(tagDao.isDeletedExists(id)).thenReturn(true);
    boolean actual = tagService.isExists(id);

    // then
    Assertions.assertTrue(actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(tagDao, Mockito.times(1)).findById(id);
    Mockito.verify(tagDao, Mockito.times(1)).isDeletedExists(id);
  }

  @Test
  void sExistsByName() {
    // given
    String name = "name";

    // when
    Mockito.when(tagDao.findByName(name)).thenReturn(null);
    Mockito.when(tagDao.isDeletedExists(name)).thenReturn(true);
    boolean actual = tagService.isExists(name);

    // then
    Assertions.assertTrue(actual);
    Mockito.verify(tagDao, Mockito.times(1)).findByName(name);
    Mockito.verify(tagDao, Mockito.times(1)).isDeletedExists(name);
  }

  @Test
  void sExistsByNameDeleted() {
    // given
    String name = "name";
    Tag tag = new Tag(1L, name, false);

    // when
    Mockito.when(tagDao.findByName(name)).thenReturn(tag);
    Mockito.when(tagDao.isDeletedExists(name)).thenReturn(false);
    boolean actual = tagService.isExists(name);

    // then
    Assertions.assertTrue(actual);
    Mockito.verify(tagDao, Mockito.times(1)).findByName(name);
  }
}
