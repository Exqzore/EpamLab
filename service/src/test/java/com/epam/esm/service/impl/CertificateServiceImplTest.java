package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.mapper.impl.CertificateMapper;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.CertificateDtoValidator;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.PaginateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CertificateServiceImplTest {
  private CertificateDao certificateDao;
  private Mapper<Certificate, CertificateDto> mapper;
  private IdValidator idValidator;
  private PaginateValidator paginateValidator;
  private CertificateDtoValidator certificateDtoValidator;

  private CertificateService certificateService;

  @BeforeEach
  public void init() {
    certificateDao = Mockito.mock(CertificateDao.class);
    TagService tagService = Mockito.mock(TagService.class);
    mapper = Mockito.mock(CertificateMapper.class);
    Mapper<Tag, TagDto> tagMapper = Mockito.mock(TagMapper.class);
    idValidator = Mockito.mock(IdValidator.class);
    paginateValidator = Mockito.mock(PaginateValidator.class);
    certificateDtoValidator = Mockito.mock(CertificateDtoValidator.class);

    certificateService =
        new CertificateServiceImpl(
            certificateDao,
            tagService,
            mapper,
            tagMapper,
            idValidator,
            paginateValidator,
            certificateDtoValidator);
  }

  @Test
  void getCountOfPages() {
    // given
    int size = 80;
    int expected = 2;

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(certificateDao.findCountOfRecords()).thenReturn(122);
    int actual = certificateService.getCountOfPages(size);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(certificateDao, Mockito.times(1)).findCountOfRecords();
  }

  @Test
  void findByCriteria() {
    // given
    String partOfNameOrDescription = "";
    List<String> tagNames = new ArrayList<>();
    List<String> sortParams = new ArrayList<>();
    int page = 1;
    int size = 50;
    List<CertificateDto> expected = new ArrayList<>();

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(paginateValidator.pageValidate(page)).thenReturn(Collections.emptyList());
    List<CertificateDto> actual =
        certificateService.findByCriteria(
            partOfNameOrDescription, tagNames, sortParams, page, size);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(paginateValidator, Mockito.times(1)).pageValidate(page);
  }

  @Test
  void findById() {
    // given
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Certificate certificate =
        new Certificate(id, "", "", BigDecimal.ZERO, 1, dateTime, dateTime, Collections.emptySet());
    CertificateDto expected =
        new CertificateDto(
            id, "", "", 0D, 1, dateTime.toString(), dateTime.toString(), Collections.emptyList());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(certificateDao.findById(id)).thenReturn(certificate);
    Mockito.when(mapper.mapToDto(certificate, true)).thenReturn(expected);
    CertificateDto actual = certificateService.findById(id);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(certificateDao, Mockito.times(1)).findById(id);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(certificate, true);
  }

  @Test
  void create() {
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Certificate certificate =
        new Certificate(id, "", "", BigDecimal.ZERO, 1, dateTime, dateTime, Collections.emptySet());
    CertificateDto certificateDto =
        new CertificateDto(
            id, "", "", 0D, 1, dateTime.toString(), dateTime.toString(), Collections.emptyList());

    // when
    Mockito.when(certificateDtoValidator.creationValidate(certificateDto))
        .thenReturn(Collections.emptyList());
    Mockito.when(mapper.mapFromDto(certificateDto)).thenReturn(certificate);
    Mockito.when(certificateDao.save(certificate)).thenReturn(certificate);
    Mockito.when(mapper.mapToDto(certificate, false)).thenReturn(certificateDto);
    CertificateDto actual = certificateService.create(certificateDto);

    // then
    Assertions.assertEquals(certificateDto, actual);
    Mockito.verify(certificateDtoValidator, Mockito.times(1)).creationValidate(certificateDto);
    Mockito.verify(mapper, Mockito.times(1)).mapFromDto(certificateDto);
    Mockito.verify(certificateDao, Mockito.times(1)).save(certificate);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(certificate, false);
  }

  @Test
  void removeById() throws NoSuchResultException {
    // given
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Certificate certificate =
        new Certificate(id, "", "", BigDecimal.ZERO, 1, dateTime, dateTime, Collections.emptySet());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(certificateDao.findById(id)).thenReturn(certificate);
    certificateService.removeById(id);

    // then
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(certificateDao, Mockito.times(1)).findById(id);
    Mockito.verify(certificateDao, Mockito.times(1)).removeById(id);
  }

  @Test
  void update() {
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Certificate certificate =
        new Certificate(id, "", "", BigDecimal.ZERO, 1, dateTime, dateTime, Collections.emptySet());
    CertificateDto certificateDto =
        new CertificateDto(
            id, "", "", 0D, 1, dateTime.toString(), dateTime.toString(), Collections.emptyList());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(certificateDtoValidator.updateValidate(certificateDto))
        .thenReturn(Collections.emptyList());
    Mockito.when(certificateDao.findById(id)).thenReturn(certificate);
    Mockito.when(certificateDao.update(certificate)).thenReturn(certificate);
    Mockito.when(mapper.mapToDto(certificate, false)).thenReturn(certificateDto);
    CertificateDto actual = certificateService.update(id, certificateDto);

    // then
    Assertions.assertEquals(certificateDto, actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(certificateDtoValidator, Mockito.times(1)).updateValidate(certificateDto);
    Mockito.verify(certificateDao, Mockito.times(1)).findById(id);
    Mockito.verify(certificateDao, Mockito.times(1)).update(certificate);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(certificate, false);
  }
}
