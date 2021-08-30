package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.specification.tag.FindAllTagsSpecification;
import com.epam.esm.dao.specification.tag.FindTagsByCertificateIdSpecification;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CreationException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.impl.util.TagFormatInterpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TagServiceImplTest {
  @InjectMocks private TagServiceImpl tagService;
  @Mock private TagDao tagDao;
  @Mock private TagFormatInterpreter interpreter;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void findAll() {
    // given
    FindAllTagsSpecification findAllTagsSpecification =
            mock(FindAllTagsSpecification.class);
    List<TagDto> expected = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();

    // when
    Mockito.when(tagDao.findBySpecification(findAllTagsSpecification)).thenReturn(tags);
    List<TagDto> actual = tagService.findAll();

    // then
    assertEquals(expected, actual);
    verify(tagDao, times(1)).findBySpecification(findAllTagsSpecification);
  }

  @Test
  void findByCertificateId() {
    // given
    long certificateId = 1;
    FindTagsByCertificateIdSpecification findTagsByCertificateIdSpecification =
        mock(FindTagsByCertificateIdSpecification.class);
    List<Tag> tags = Collections.emptyList();
    List<TagDto> tagDtoList = Collections.emptyList();

    // when
    Mockito.when(tagDao.findBySpecification(findTagsByCertificateIdSpecification)).thenReturn(tags);
    List<TagDto> actual = tagService.findByCertificateId(certificateId);

    // then
    assertEquals(tagDtoList, actual);
    verify(tagDao, times(1))
        .findBySpecification(findTagsByCertificateIdSpecification);
  }

  @Test
  void findByIdPositive() {
    // given
    long id = 10;
    String name = "Tag";
    TagDto expected = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    // when
    Mockito.when(tagDao.findById(id)).thenReturn(Optional.of(tag));
    Mockito.when(interpreter.toDto(tag)).thenReturn(expected);
    TagDto actual = tagService.findById(String.valueOf(id));

    // then
    assertEquals(expected, actual);
    verify(interpreter, times(1)).toDto(tag);
    verify(tagDao, times(1)).findById(id);
  }

  @Test
  void findByIdNegative() {
    // given
    long id = 10;

    // when
    Mockito.when(tagDao.findById(id)).thenReturn(Optional.empty());

    // then
    assertThrows(NotFoundException.class, () -> tagService.findById(String.valueOf(id)));
    verify(tagDao, times(1)).findById(id);
  }

  @Test
  void createPositive() {
    // given
    long id = 1;
    String name = "Tag";
    TagDto expectedTagDto = new TagDto(id, name);
    Tag expectedTag = new Tag(id, name);
    TagDto tagDto = new TagDto();
    tagDto.setName(name);
    Tag tag = new Tag();
    tag.setName(name);

    // when
    Mockito.when(interpreter.fromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(Optional.of(expectedTag));
    Mockito.when(interpreter.toDto(expectedTag)).thenReturn(expectedTagDto);
    TagDto actual = tagService.create(tagDto);

    // then
    assertEquals(expectedTagDto, actual);
    verify(interpreter, times(1)).fromDto(tagDto);
    verify(tagDao, times(1)).save(tag);
    verify(interpreter, times(1)).toDto(expectedTag);
  }

  @Test
  void createNegative() {
    // given
    String name = "Tag";
    TagDto tagDto = new TagDto();
    tagDto.setName(name);
    Tag tag = new Tag();
    tag.setName(name);

    // when
    Mockito.when(interpreter.fromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(Optional.empty());

    // then
    assertThrows(CreationException.class, () -> tagService.create(tagDto));
    verify(interpreter, times(1)).fromDto(tagDto);
    verify(tagDao, times(1)).save(tag);
  }

  @Test
  void removeById() {
    // given
    long id = 1;

    // when
    tagService.removeById(String.valueOf(id));

    // then
    verify(tagDao, times(1)).removeById(id);
  }

  @Test
  void isExistByName() {
    // given
    String name = Mockito.anyString();

    // when
    tagService.isExists(name);

    // then
    verify(tagDao, times(1)).isExists(name);
  }

  @Test
  void isExistById() {
    // given
    long id = Mockito.anyLong();

    // when
    tagService.isExists(id);

    // then
    verify(tagDao, times(1)).isExists(id);
  }

  @Test
  void createTagsOnlyByNameOrId1() {
    // given
    long id = 1;
    String name = "Tag";
    TagDto tagDto = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    // when
    Mockito.when(tagDao.isExists(tagDto.getId())).thenReturn(true);
    Mockito.when(tagDao.findById(tagDto.getId())).thenReturn(Optional.of(tag));
    tagService.createTagsOnlyByNameOrId(List.of(tagDto));

    // then
    verify(tagDao, times(1)).isExists(tagDto.getId());
    verify(tagDao, times(1)).setDeleted(tagDto.getId(), false);
    verify(tagDao, times(1)).findById(tagDto.getId());
    verify(interpreter, times(1)).toDto(tag);
  }

  @Test
  void createTagsOnlyByNameOrId2() {
    // given
    long id = 1;
    String name = "Tag";
    TagDto tagDto = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    // when
    Mockito.when(tagDao.isExists(tagDto.getId())).thenReturn(false);
    Mockito.when(tagDao.isExists(tagDto.getName())).thenReturn(true);
    Mockito.when(tagDao.findByName(tagDto.getName())).thenReturn(Optional.of(tag));
    tagService.createTagsOnlyByNameOrId(List.of(tagDto));

    // then
    verify(tagDao, times(1)).isExists(tagDto.getId());
    verify(tagDao, times(1)).isExists(tagDto.getName());
    verify(tagDao, times(1)).setDeleted(tagDto.getName(), false);
    verify(tagDao, times(1)).findByName(tagDto.getName());
    verify(interpreter, times(1)).toDto(tag);
  }

  @Test
  void createTagsOnlyByNameOrId3() {
    // given
    long id = 1;
    String name = "Tag";
    TagDto tagDto = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    // when
    Mockito.when(tagDao.isExists(tagDto.getId())).thenReturn(false);
    Mockito.when(tagDao.isExists(tagDto.getName())).thenReturn(false);
    Mockito.when(interpreter.fromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(Optional.of(tag));
    tagService.createTagsOnlyByNameOrId(List.of(tagDto));

    // then
    verify(tagDao, times(1)).isExists(tagDto.getId());
    verify(tagDao, times(1)).isExists(tagDto.getName());
    verify(tagDao, times(1)).save(tag);
    verify(interpreter, times(1)).toDto(tag);
  }
}
