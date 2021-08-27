package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CreationException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.util.CertificateFormatInterpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CertificateServiceImplTest {
  @InjectMocks private CertificateServiceImpl certificateService;
  @Mock private CertificateDao certificateDao;
  @Mock private TagService tagService;
  @Mock private CertificateFormatInterpreter interpreter;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void findByCriteria() {}

  @Test
  void findByIdPositive() {
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> tagDtoList = List.of(new TagDto(2L, "tag2"));
    List<Tag> tags = List.of(new Tag(2L, "tag2"));
    CertificateDto expectedDto = new CertificateDto();
    expectedDto.setId(id);
    expectedDto.setName(name);
    expectedDto.setDescription(description);
    expectedDto.setPrice(price);
    expectedDto.setDuration(duration);
    expectedDto.setTags(tagDtoList);
    Certificate expected = new Certificate();
    expected.setId(id);
    expected.setName(name);
    expected.setDescription(description);
    expected.setPrice(price);
    expected.setDuration(duration);
    expected.setTags(tags);

    Mockito.when(certificateDao.findById(id)).thenReturn(Optional.of(expected));
    Mockito.when(interpreter.toDto(expected)).thenReturn(expectedDto);
    Mockito.when(tagService.findByCertificateId(id)).thenReturn(tagDtoList);
    CertificateDto actual = certificateService.findById(String.valueOf(id));
    assertEquals(expectedDto, actual);
    Mockito.verify(interpreter, Mockito.times(1)).toDto(expected);
    Mockito.verify(tagService, Mockito.times(1)).findByCertificateId(id);
    Mockito.verify(certificateDao, Mockito.times(1)).findById(id);
  }

  @Test
  void findByIdNegative() {
    long id = 1;

    Mockito.when(certificateDao.findById(id)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> certificateService.findById(String.valueOf(id)));
    Mockito.verify(certificateDao, Mockito.times(1)).findById(id);
  }

  @Test
  void createPositive() {
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> expectedTagDtoList = List.of(new TagDto(2L, "tag2"));
    List<Tag> expectedTags = List.of(new Tag(2L, "tag2"));
    TagDto tagDto = new TagDto();
    tagDto.setName("tag2");
    List<TagDto> tagDtoList = List.of(tagDto);
    List<Tag> tags = List.of(new Tag(2L, "tag2"));
    CertificateDto expectedDto = new CertificateDto();
    expectedDto.setId(id);
    expectedDto.setName(name);
    expectedDto.setDescription(description);
    expectedDto.setPrice(price);
    expectedDto.setDuration(duration);
    expectedDto.setTags(expectedTagDtoList);
    Certificate expected = new Certificate();
    expected.setId(id);
    expected.setName(name);
    expected.setDescription(description);
    expected.setPrice(price);
    expected.setDuration(duration);
    expected.setTags(expectedTags);
    Certificate certificate = new Certificate();
    certificate.setId(id);
    certificate.setName(name);
    certificate.setDescription(description);
    certificate.setPrice(price);
    certificate.setDuration(duration);
    certificate.setTags(tags);
    CertificateDto certificateDto = new CertificateDto();
    certificateDto.setName(name);
    certificateDto.setDescription(description);
    certificateDto.setPrice(price);
    certificateDto.setDuration(duration);
    certificateDto.setTags(tagDtoList);

    Mockito.when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    Mockito.when(certificateDao.save(certificate)).thenReturn(Optional.of(expected));
    Mockito.when(interpreter.toDto(expected)).thenReturn(expectedDto);
    Mockito.when(tagService.createTagsOnlyByNameOrId(tagDtoList)).thenReturn(expectedTagDtoList);
    CertificateDto actual = certificateService.create(certificateDto);
    assertEquals(expectedDto, actual);
    Mockito.verify(interpreter, Mockito.times(1)).fromDto(certificateDto);
    Mockito.verify(certificateDao, Mockito.times(1)).save(certificate);
    Mockito.verify(interpreter, Mockito.times(1)).toDto(expected);
    Mockito.verify(tagService, Mockito.times(1)).createTagsOnlyByNameOrId(tagDtoList);
  }

  @Test
  void createNegative() {
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    TagDto tagDto = new TagDto();
    tagDto.setName("tag2");
    List<TagDto> tagDtoList = List.of(tagDto);
    List<Tag> tags = List.of(new Tag(2L, "tag2"));
    Certificate certificate = new Certificate();
    certificate.setId(id);
    certificate.setName(name);
    certificate.setDescription(description);
    certificate.setPrice(price);
    certificate.setDuration(duration);
    certificate.setTags(tags);
    CertificateDto certificateDto = new CertificateDto();
    certificateDto.setName(name);
    certificateDto.setDescription(description);
    certificateDto.setPrice(price);
    certificateDto.setDuration(duration);
    certificateDto.setTags(tagDtoList);

    Mockito.when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    Mockito.when(certificateDao.save(certificate)).thenReturn(Optional.empty());
    assertThrows(CreationException.class, () -> certificateService.create(certificateDto));
    Mockito.verify(interpreter, Mockito.times(1)).fromDto(certificateDto);
    Mockito.verify(certificateDao, Mockito.times(1)).save(certificate);
  }

  @Test
  void removeById() {
    long id = 1;

    certificateService.removeById(String.valueOf(id));
    Mockito.verify(certificateDao, Mockito.times(1)).removeById(id);
  }

  @Test
  void updatePositive() {
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> expectedTagDtoList = List.of(new TagDto(2L, "tag2"));
    List<Tag> expectedTags = List.of(new Tag(2L, "tag2"));
    TagDto tagDto = new TagDto();
    tagDto.setName("tag2");
    List<TagDto> tagDtoList = List.of(tagDto);
    List<Tag> tags = List.of(new Tag(2L, "tag2"));
    CertificateDto expectedDto = new CertificateDto();
    expectedDto.setId(id);
    expectedDto.setName(name);
    expectedDto.setDescription(description);
    expectedDto.setPrice(price);
    expectedDto.setDuration(duration);
    expectedDto.setTags(expectedTagDtoList);
    Certificate expected = new Certificate();
    expected.setId(id);
    expected.setName(name);
    expected.setDescription(description);
    expected.setPrice(price);
    expected.setDuration(duration);
    expected.setTags(expectedTags);
    Certificate certificate = new Certificate();
    certificate.setId(id);
    certificate.setName(name);
    certificate.setDescription(description);
    certificate.setPrice(price);
    certificate.setDuration(duration);
    certificate.setTags(tags);
    CertificateDto certificateDto = new CertificateDto();
    certificateDto.setName(name);
    certificateDto.setDescription(description);
    certificateDto.setPrice(price);
    certificateDto.setDuration(duration);
    certificateDto.setTags(tagDtoList);

    Mockito.when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    Mockito.when(certificateDao.update(certificate)).thenReturn(Optional.of(expected));
    Mockito.when(interpreter.toDto(expected)).thenReturn(expectedDto);
    Mockito.when(tagService.createTagsOnlyByNameOrId(tagDtoList)).thenReturn(expectedTagDtoList);
    CertificateDto actual = certificateService.update(String.valueOf(id), certificateDto);
    assertEquals(expectedDto, actual);
    Mockito.verify(interpreter, Mockito.times(1)).fromDto(certificateDto);
    Mockito.verify(certificateDao, Mockito.times(1)).update(certificate);
    Mockito.verify(interpreter, Mockito.times(1)).toDto(expected);
    Mockito.verify(tagService, Mockito.times(1)).createTagsOnlyByNameOrId(tagDtoList);
  }

  @Test
  void updateNegative() {
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    TagDto tagDto = new TagDto();
    tagDto.setName("tag2");
    List<TagDto> tagDtoList = List.of(tagDto);
    List<Tag> tags = List.of(new Tag(2L, "tag2"));
    Certificate certificate = new Certificate();
    certificate.setId(id);
    certificate.setName(name);
    certificate.setDescription(description);
    certificate.setPrice(price);
    certificate.setDuration(duration);
    certificate.setTags(tags);
    CertificateDto certificateDto = new CertificateDto();
    certificateDto.setName(name);
    certificateDto.setDescription(description);
    certificateDto.setPrice(price);
    certificateDto.setDuration(duration);
    certificateDto.setTags(tagDtoList);

    Mockito.when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    Mockito.when(certificateDao.update(certificate)).thenReturn(Optional.empty());
    assertThrows(CreationException.class, () -> certificateService.update(String.valueOf(id) ,certificateDto));
    Mockito.verify(interpreter, Mockito.times(1)).fromDto(certificateDto);
    Mockito.verify(certificateDao, Mockito.times(1)).update(certificate);
  }
}
