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

class TagServiceImplTest {
  @InjectMocks private TagServiceImpl tagService;
  @Mock private TagDao tagDao;
  @Mock private TagFormatInterpreter interpreter;
  @Mock private FindAllTagsSpecification findAllTagsSpecification;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void findAll() {
    //    //    List<TagDto> tagDtoListExpected = List.of(new TagDto(1L, "tag1"), new TagDto(2L,
    // "tag2"));
    //    //    List<Tag> tagsExpected = List.of(new Tag(1L, "tag1"), new Tag(2L, "tag2"));
    //    List<TagDto> tagDtoListExpected = Collections.emptyList();
    //    List<Tag> tagsExpected = Collections.emptyList();
    //
    //    Mockito.when(tagDao.findBySpecification(null)).thenReturn(tagsExpected);
    //    List<TagDto> actual = tagService.findAll();
    //    assertEquals(tagDtoListExpected, actual);
    //    Mockito.verify(tagDao, Mockito.times(1)).findBySpecification(null);
  }

  //    @Override
  //    public List<TagDto> findByCertificateId(long certificateId) {
  //      return tagDao
  //              .findBySpecification(new FindTagsByCertificateIdSpecification(certificateId))
  //              .stream()
  //              .map(interpreter::toDto)
  //              .collect(Collectors.toList());
  //    }

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
    Mockito.verify(tagDao, Mockito.times(1))
        .findBySpecification(findTagsByCertificateIdSpecification);
  }

  @Test
  void findByIdPositive() {
    // given
    long id = 10;
    String name = "Tag";
    TagDto expectedDto = new TagDto(id, name);
    Tag expected = new Tag(id, name);

    Mockito.when(tagDao.findById(id)).thenReturn(Optional.of(expected));
    Mockito.when(interpreter.toDto(expected)).thenReturn(expectedDto);
    TagDto actual = tagService.findById(String.valueOf(id));
    assertEquals(expectedDto, actual);
    Mockito.verify(interpreter, Mockito.times(1)).toDto(expected);
    Mockito.verify(tagDao, Mockito.times(1)).findById(id);
  }

  @Test
  void findByIdNegative() {
    long id = 10;

    Mockito.when(tagDao.findById(id)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> tagService.findById(String.valueOf(id)));
    Mockito.verify(tagDao, Mockito.times(1)).findById(id);
  }

  @Test
  void createPositive() {
    long id = 1;
    String name = "Tag";
    TagDto expectedTagDto = new TagDto(id, name);
    Tag expectedTag = new Tag(id, name);
    TagDto tagDto = new TagDto();
    tagDto.setName(name);
    Tag tag = new Tag();
    tag.setName(name);

    Mockito.when(interpreter.fromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(Optional.of(expectedTag));
    Mockito.when(interpreter.toDto(expectedTag)).thenReturn(expectedTagDto);
    TagDto actual = tagService.create(tagDto);
    assertEquals(expectedTagDto, actual);
    Mockito.verify(interpreter, Mockito.times(1)).fromDto(tagDto);
    Mockito.verify(tagDao, Mockito.times(1)).save(tag);
    Mockito.verify(interpreter, Mockito.times(1)).toDto(expectedTag);
  }

  @Test
  void createNegative() {
    String name = "Tag";
    TagDto tagDto = new TagDto();
    tagDto.setName(name);
    Tag tag = new Tag();
    tag.setName(name);

    Mockito.when(interpreter.fromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(Optional.empty());
    assertThrows(CreationException.class, () -> tagService.create(tagDto));
    Mockito.verify(interpreter, Mockito.times(1)).fromDto(tagDto);
    Mockito.verify(tagDao, Mockito.times(1)).save(tag);
  }

  @Test
  void removeById() {
    long id = 1;

    tagService.removeById(String.valueOf(id));
    Mockito.verify(tagDao, Mockito.times(1)).removeById(id);
  }

  @Test
  void isExistByName() {
    String name = Mockito.anyString();

    tagService.isExists(name);
    Mockito.verify(tagDao, Mockito.times(1)).isExists(name);
  }

  @Test
  void isExistById() {
    long id = Mockito.anyLong();

    tagService.isExists(id);
    Mockito.verify(tagDao, Mockito.times(1)).isExists(id);
  }

  @Test
  void createTagsOnlyByNameOrId1() {
    long id = 1;
    String name = "Tag";
    TagDto tagDto = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    Mockito.when(tagDao.isExists(tagDto.getId())).thenReturn(true);
    Mockito.when(tagDao.findById(tagDto.getId())).thenReturn(Optional.of(tag));
    tagService.createTagsOnlyByNameOrId(List.of(tagDto));
    Mockito.verify(tagDao, Mockito.times(1)).isExists(tagDto.getId());
    Mockito.verify(tagDao, Mockito.times(1)).setDeleted(tagDto.getId(), false);
    Mockito.verify(tagDao, Mockito.times(1)).findById(tagDto.getId());
    Mockito.verify(interpreter, Mockito.times(1)).toDto(tag);
  }

  @Test
  void createTagsOnlyByNameOrId2() {
    long id = 1;
    String name = "Tag";
    TagDto tagDto = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    Mockito.when(tagDao.isExists(tagDto.getId())).thenReturn(false);
    Mockito.when(tagDao.isExists(tagDto.getName())).thenReturn(true);
    Mockito.when(tagDao.findByName(tagDto.getName())).thenReturn(Optional.of(tag));
    tagService.createTagsOnlyByNameOrId(List.of(tagDto));
    Mockito.verify(tagDao, Mockito.times(1)).isExists(tagDto.getId());
    Mockito.verify(tagDao, Mockito.times(1)).isExists(tagDto.getName());
    Mockito.verify(tagDao, Mockito.times(1)).setDeleted(tagDto.getName(), false);
    Mockito.verify(tagDao, Mockito.times(1)).findByName(tagDto.getName());
    Mockito.verify(interpreter, Mockito.times(1)).toDto(tag);
  }

  @Test
  void createTagsOnlyByNameOrId3() {
    long id = 1;
    String name = "Tag";
    TagDto tagDto = new TagDto(id, name);
    Tag tag = new Tag(id, name);

    Mockito.when(tagDao.isExists(tagDto.getId())).thenReturn(false);
    Mockito.when(tagDao.isExists(tagDto.getName())).thenReturn(false);
    Mockito.when(interpreter.fromDto(tagDto)).thenReturn(tag);
    Mockito.when(tagDao.save(tag)).thenReturn(Optional.of(tag));
    tagService.createTagsOnlyByNameOrId(List.of(tagDto));
    Mockito.verify(tagDao, Mockito.times(1)).isExists(tagDto.getId());
    Mockito.verify(tagDao, Mockito.times(1)).isExists(tagDto.getName());
    Mockito.verify(tagDao, Mockito.times(1)).save(tag);
    Mockito.verify(interpreter, Mockito.times(1)).toDto(tag);
  }
}
