package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.specification.certificate.FindAllCertificatesSpecification;
import com.epam.esm.dao.specification.certificate.FindCertificatesByPartAndTagNameSpecification;
import com.epam.esm.dao.specification.certificate.FindCertificatesByPartOfNameOrDescriptionSpecification;
import com.epam.esm.dao.specification.certificate.FindCertificatesByTagNameSpecification;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

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
  void findByCriteria1() {
    // given
    FindCertificatesByTagNameSpecification findCertificatesByTagNameSpecification =
        mock(FindCertificatesByTagNameSpecification.class);
    List<Certificate> certificates = Collections.emptyList();
    List<CertificateDto> expected = Collections.emptyList();

    // when
    when(certificateDao.findBySpecification(findCertificatesByTagNameSpecification, ""))
        .thenReturn(certificates);
    List<CertificateDto> actual = certificateService.findByCriteria(null, "", null);

    // then
    assertEquals(expected, actual);
    verify(certificateDao, times(1))
        .findBySpecification(findCertificatesByTagNameSpecification, "");
  }

  @Test
  void findByCriteria2() {
    // given
    FindCertificatesByPartOfNameOrDescriptionSpecification
        findCertificatesByPartOfNameOrDescriptionSpecification =
            mock(FindCertificatesByPartOfNameOrDescriptionSpecification.class);
    List<Certificate> certificates = Collections.emptyList();
    List<CertificateDto> expected = Collections.emptyList();

    // when
    when(
            certificateDao.findBySpecification(
                findCertificatesByPartOfNameOrDescriptionSpecification, ""))
        .thenReturn(certificates);
    List<CertificateDto> actual = certificateService.findByCriteria("", null, null);

    // then
    assertEquals(expected, actual);
    verify(certificateDao, times(1))
        .findBySpecification(findCertificatesByPartOfNameOrDescriptionSpecification, "");
  }

  @Test
  void findByCriteria3() {
    // given
    FindCertificatesByPartAndTagNameSpecification FindCertificatesByPartAndTagNameSpecification =
        mock(FindCertificatesByPartAndTagNameSpecification.class);
    List<Certificate> certificates = Collections.emptyList();
    List<CertificateDto> expected = Collections.emptyList();

    // when
    when(
            certificateDao.findBySpecification(FindCertificatesByPartAndTagNameSpecification, ""))
        .thenReturn(certificates);
    List<CertificateDto> actual = certificateService.findByCriteria("", "", null);

    // then
    assertEquals(expected, actual);
    verify(certificateDao, times(1))
        .findBySpecification(FindCertificatesByPartAndTagNameSpecification, "");
  }

  @Test
  void findByCriteria4() {
    // given
    FindAllCertificatesSpecification findAllCertificatesSpecification =
        mock(FindAllCertificatesSpecification.class);
    List<Certificate> certificates = Collections.emptyList();
    List<CertificateDto> expected = Collections.emptyList();

    // when
    when(certificateDao.findBySpecification(findAllCertificatesSpecification, ""))
        .thenReturn(certificates);
    List<CertificateDto> actual = certificateService.findByCriteria("", "", null);

    // then
    assertEquals(expected, actual);
    verify(certificateDao, times(1))
        .findBySpecification(findAllCertificatesSpecification, "");
  }

  @Test
  void findByIdPositive() {
    // given
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> tagDtoList = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();
    CertificateDto expected = new CertificateDto();
    expected.setId(id);
    expected.setName(name);
    expected.setDescription(description);
    expected.setPrice(price);
    expected.setDuration(duration);
    expected.setTags(tagDtoList);
    Certificate tag = new Certificate();
    tag.setName(name);
    tag.setDescription(description);
    tag.setPrice(price);
    tag.setDuration(duration);
    tag.setTags(tags);

    // when
    when(certificateDao.findById(id)).thenReturn(Optional.of(tag));
    when(interpreter.toDto(tag)).thenReturn(expected);
    when(tagService.findByCertificateId(expected.getId())).thenReturn(tagDtoList);
    CertificateDto actual = certificateService.findById(String.valueOf(id));

    // then
    assertEquals(expected, actual);
    verify(interpreter, times(1)).toDto(tag);
    verify(tagService, times(1)).findByCertificateId(expected.getId());
    verify(certificateDao, times(1)).findById(id);
  }

  @Test
  void findByIdNegative() {
    // given
    long id = 1;

    // when
    when(certificateDao.findById(id)).thenReturn(Optional.empty());

    // then
    assertThrows(NotFoundException.class, () -> certificateService.findById(String.valueOf(id)));
    verify(certificateDao, Mockito.times(1)).findById(id);
  }

  @Test
  void createPositive() {
    // given
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> expectedTagDtoList = Collections.emptyList();
    List<TagDto> tagDtoList = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();
    CertificateDto expected = new CertificateDto();
    expected.setId(id);
    expected.setName(name);
    expected.setDescription(description);
    expected.setPrice(price);
    expected.setDuration(duration);
    expected.setTags(expectedTagDtoList);
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

    // when
    when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    when(certificateDao.save(certificate)).thenReturn(Optional.of(certificate));
    when(interpreter.toDto(certificate)).thenReturn(expected);
    when(tagService.createTagsOnlyByNameOrId(tagDtoList)).thenReturn(expectedTagDtoList);
    CertificateDto actual = certificateService.create(certificateDto);

    // then
    assertEquals(expected, actual);
    verify(interpreter, times(1)).fromDto(certificateDto);
    verify(certificateDao, times(1)).save(certificate);
    verify(interpreter, times(1)).toDto(certificate);
    verify(tagService, times(1)).createTagsOnlyByNameOrId(tagDtoList);
  }

  @Test
  void createNegative() {
    // given
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> tagDtoList = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();
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

    // when
    when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    when(certificateDao.save(certificate)).thenReturn(Optional.empty());

    // then
    assertThrows(CreationException.class, () -> certificateService.create(certificateDto));
    verify(interpreter, times(1)).fromDto(certificateDto);
    verify(certificateDao, times(1)).save(certificate);
  }

  @Test
  void removeById() {
    // given
    long id = 1;

    // when
    certificateService.removeById(String.valueOf(id));

    // then
    verify(certificateDao, times(1)).removeById(id);
  }

  @Test
  void updatePositive() {
    // given
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> expectedTagDtoList = Collections.emptyList();
    List<TagDto> tagDtoList = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();
    CertificateDto expected = new CertificateDto();
    expected.setId(id);
    expected.setName(name);
    expected.setDescription(description);
    expected.setPrice(price);
    expected.setDuration(duration);
    expected.setTags(expectedTagDtoList);
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

    // when
    when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    when(certificateDao.update(certificate)).thenReturn(Optional.of(certificate));
    when(interpreter.toDto(certificate)).thenReturn(expected);
    when(tagService.createTagsOnlyByNameOrId(tagDtoList)).thenReturn(expectedTagDtoList);
    CertificateDto actual = certificateService.update(String.valueOf(id), certificateDto);

    // then
    assertEquals(expected, actual);
    verify(interpreter, times(1)).fromDto(certificateDto);
    verify(certificateDao, times(1)).update(certificate);
    verify(interpreter, times(1)).toDto(certificate);
    verify(tagService, times(1)).createTagsOnlyByNameOrId(tagDtoList);
  }

  @Test
  void updateNegative() {
    // given
    long id = 1;
    String name = "name";
    String description = "description";
    Integer duration = 5;
    Double price = 1.5;
    List<TagDto> tagDtoList = Collections.emptyList();
    List<Tag> tags = Collections.emptyList();
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

    // when
    when(interpreter.fromDto(certificateDto)).thenReturn(certificate);
    when(certificateDao.update(certificate)).thenReturn(Optional.empty());

    // then
    assertThrows(
        CreationException.class,
        () -> certificateService.update(String.valueOf(id), certificateDto));
    verify(interpreter, times(1)).fromDto(certificateDto);
    verify(certificateDao, times(1)).update(certificate);
  }
}
