package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.dao.impl.util.CertificateSearchCriteria;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.CreationException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.validator.CertificateDtoValidator;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.PaginateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
  private final CertificateDao certificateDao;
  private final TagService tagService;
  private final Mapper<Certificate, CertificateDto> mapper;
  private final Mapper<Tag, TagDto> tagMapper;
  private final IdValidator idValidator;
  private final PaginateValidator paginateValidator;
  private final CertificateDtoValidator certificateDtoValidator;

  @Autowired
  public CertificateServiceImpl(
      CertificateDao certificateDao,
      TagService tagService,
      Mapper<Certificate, CertificateDto> mapper,
      Mapper<Tag, TagDto> tagMapper,
      IdValidator idValidator,
      PaginateValidator paginateValidator,
      CertificateDtoValidator certificateDtoValidator) {
    this.certificateDao = certificateDao;
    this.tagService = tagService;
    this.mapper = mapper;
    this.tagMapper = tagMapper;
    this.idValidator = idValidator;
    this.paginateValidator = paginateValidator;
    this.certificateDtoValidator = certificateDtoValidator;
  }

  @Override
  public int findPaginated(int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    int countOfRecords = certificateDao.findCountOfRecords();
    int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
    return countPages == 0 ? 1 : countPages;
  }

  @Override
  public List<CertificateDto> findByCriteria(
      String partOfNameOrDescription,
      List<String> tagNames,
      List<String> sortParams,
      int page,
      int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(paginateValidator.pageValidate(page));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return certificateDao
        .findByCriteria(
            new CertificateSearchCriteria(partOfNameOrDescription, tagNames),
            page,
            size,
            sortParams)
        .stream()
        .map(certificate -> mapper.mapToDto(certificate, false))
        .collect(Collectors.toList());
  }

  @Override
  public CertificateDto findById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Certificate certificate = certificateDao.findById(id);
    if (certificate == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.CERTIFICATE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NOT_FOUND).formatted(id)));
    }
    return mapper.mapToDto(certificate, true);
  }

  @Override
  @Transactional
  public CertificateDto create(CertificateDto certificateDto) {
    List<ErrorMessageDto> errors =
        certificateDtoValidator.createCertificateDtoValidate(certificateDto);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    List<TagDto> tagDtoList = addCertificateTags(certificateDto.getTags());
    if (tagDtoList != null) {
      certificateDto.setTags(tagDtoList);
    }
    Certificate certificate = mapper.mapFromDto(certificateDto);
    certificate.setCreateDate(LocalDateTime.now());
    certificate.setLastUpdateDate(certificate.getCreateDate());
    certificate = certificateDao.save(certificate);
    if (certificate == null) {
      throw new CreationException(
          new ErrorMessageDto(
              CustomHttpStatus.Conflict.CERTIFICATE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NOT_CREATE)));
    }
    return mapper.mapToDto(certificate, false);
  }

  @Override
  @Transactional
  public void removeById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Certificate certificate = certificateDao.findById(id);
    if (certificate == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.CERTIFICATE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NOT_FOUND).formatted(id)));
    }
    try {
      certificateDao.removeById(certificate.getId());
    } catch (NoSuchResultException exception) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.CERTIFICATE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NOT_FOUND).formatted(id)));
    }
  }

  @Override
  @Transactional
  public CertificateDto update(Long id, CertificateDto certificateDto) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    errors.addAll(certificateDtoValidator.updateCertificateDtoValidate(certificateDto));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Certificate certificate = certificateDao.findById(id);
    if (certificate != null) {
      if (certificateDto.getName() != null) {
        certificate.setName(certificateDto.getName());
      }
      if (certificateDto.getDescription() != null) {
        certificate.setDescription(certificateDto.getDescription());
      }
      if (certificateDto.getPrice() != null) {
        certificate.setPrice(certificateDto.getPrice());
      }
      if (certificateDto.getDuration() != null) {
        certificate.setDuration(certificateDto.getDuration());
      }
      List<TagDto> tagDtoList = addCertificateTags(certificateDto.getTags());
      if (tagDtoList != null) {
        certificate.setTags(
            tagDtoList.stream().map(tagMapper::mapFromDto).collect(Collectors.toSet()));
      }
      certificate.setLastUpdateDate(LocalDateTime.now());
      certificate = certificateDao.update(certificate);
      if (certificate == null) {
        throw new CreationException(
            new ErrorMessageDto(
                CustomHttpStatus.Conflict.CERTIFICATE.getCode(),
                LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NOT_UPDATE)));
      }
      return mapper.mapToDto(certificate, false);
    } else {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.CERTIFICATE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NOT_FOUND).formatted(id)));
    }
  }

  private List<TagDto> addCertificateTags(List<TagDto> tagDtoList) {
    List<TagDto> result = null;
    if (tagDtoList != null) {
      tagDtoList.forEach(
          tagDto -> {
            if (tagDto.getId() != null && !tagService.isExists(tagDto.getId())) {
              throw new NotFoundException(
                  new ErrorMessageDto(
                      CustomHttpStatus.NotFound.TAG.getCode(),
                      LanguageManager.getMessage(LocaleMessages.TAG_NOT_FOUND)
                          .formatted(tagDto.getId())));
            }
          });
      result = tagService.createTagsOnlyByNameOrId(tagDtoList);
    }
    return result;
  }
}
