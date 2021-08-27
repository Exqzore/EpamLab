package com.epam.esm.service.impl;

import com.epam.esm.constant.CustomHttpStatus;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.specification.certificate.FindAllCertificatesSpecification;
import com.epam.esm.dao.specification.certificate.FindCertificatesByPartAndTagNameSpecification;
import com.epam.esm.dao.specification.certificate.FindCertificatesByPartOfNameOrDescriptionSpecification;
import com.epam.esm.dao.specification.certificate.FindCertificatesByTagNameSpecification;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CreationException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.util.CertificateFormatInterpreter;
import com.epam.esm.validator.CertificateDtoValidator;
import com.epam.esm.validator.StringIdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
  private static final String ORDER_BY = " ORDER BY ";
  private static final String SORT_PARAMS_PAIR_DELIMITER = ":";
  private static final String SORT_TYPE_ASC = "ASC";
  private static final String SORT_TYPE_DESC = "DESC";
  private static final String SORT_BY_NAME = "name";
  private static final String SORT_BY_CREATE_DATE = "create_date";
  private static final String SORT_BY_PRICE = "price";
  private static final String SORT_BY_DURATION = "duration";
  private static final String SORT_BY_LAST_UPDATE_DATE = "last_update_date";
  private final String NOT_FOUND_CERTIFICATE = "Requested certificate not found (id = %s)";
  private final String NOT_FOUND_TAG = "Requested tag not found (id = %s)";
  private final String NOT_CREATED_CERTIFICATE = "Failed to create certificate";
  private final String NOT_UPDATED_CERTIFICATE = "Failed to update certificate";

  private final CertificateDao certificateDao;
  private final TagService tagService;
  private final CertificateFormatInterpreter certificateInterpreter;

  @Autowired
  public CertificateServiceImpl(
      CertificateDao certificateDao,
      TagService tagService,
      CertificateFormatInterpreter certificateInterpreter) {
    this.certificateDao = certificateDao;
    this.tagService = tagService;
    this.certificateInterpreter = certificateInterpreter;
  }

  private static String calculatePattern(String string) {
    StringBuilder stringBuilder = new StringBuilder("%");
    stringBuilder.append(string).append("%");
    return stringBuilder.toString();
  }

  private static String calculateSortParams(List<String> sortParams) {
    StringBuilder stringBuilder = new StringBuilder();
    if (sortParams != null) {
      sortParams.forEach(
          o -> {
            String[] pair = o.split(SORT_PARAMS_PAIR_DELIMITER);
            if (pair.length != 2
                || !List.of(
                        SORT_BY_NAME,
                        SORT_BY_PRICE,
                        SORT_BY_DURATION,
                        SORT_BY_CREATE_DATE,
                        SORT_BY_LAST_UPDATE_DATE)
                    .contains(pair[0])
                || !List.of(SORT_TYPE_ASC, SORT_TYPE_DESC)
                    .contains(pair[1].toUpperCase(Locale.ROOT))) {
              return;
            }
            stringBuilder.append(pair[0]).append(" ").append(pair[1]).append(", ");
          });
    }
    if (!stringBuilder.isEmpty()) {
      stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
      stringBuilder.insert(0, ORDER_BY);
    }
    return stringBuilder.toString();
  }

  @Override
  @Transactional
  public List<CertificateDto> findByCriteria(
      String partOfNameOrDescription, String tagName, List<String> sortParams) {
    List<Certificate> certificates;
    String sortOptions = calculateSortParams(sortParams);
    String part = calculatePattern(partOfNameOrDescription);
    if (partOfNameOrDescription != null && tagName != null) {
      certificates =
          certificateDao.findBySpecification(
              new FindCertificatesByPartAndTagNameSpecification(tagName, part), sortOptions);
    } else if (partOfNameOrDescription != null) {
      certificates =
          certificateDao.findBySpecification(
              new FindCertificatesByPartOfNameOrDescriptionSpecification(part), sortOptions);
    } else if (tagName != null) {
      certificates =
          certificateDao.findBySpecification(
              new FindCertificatesByTagNameSpecification(tagName), sortOptions);
    } else {
      certificates =
          certificateDao.findBySpecification(new FindAllCertificatesSpecification(), sortOptions);
    }
    certificates.forEach(c -> c.setTags(Collections.emptyList()));
    List<CertificateDto> certificateDtoList =
        certificates.stream().map(certificateInterpreter::toDto).collect(Collectors.toList());
    certificateDtoList.forEach(o -> o.setTags(tagService.findByCertificateId(o.getId())));
    return certificateDtoList;
  }

  @Override
  @Transactional
  public CertificateDto findById(String id) {
    List<String> errors = StringIdValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.CERTIFICATE);
    }
    long valueId = Long.parseLong(id);
    Certificate certificate =
        certificateDao
            .findById(valueId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        List.of(NOT_FOUND_CERTIFICATE.formatted(valueId)),
                        CustomHttpStatus.NOT_FOUND.CERTIFICATE));
    CertificateDto certificateDto = certificateInterpreter.toDto(certificate);
    certificateDto.setTags(tagService.findByCertificateId(valueId));
    return certificateDto;
  }

  @Override
  @Transactional
  public CertificateDto create(CertificateDto certificateDto) {
    List<String> errors = CertificateDtoValidator.createCertificateDtoValidate(certificateDto);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.CERTIFICATE);
    }
    errors = tagsIsExists(certificateDto.getTags());
    if (!errors.isEmpty()) {
      throw new NotFoundException(errors, CustomHttpStatus.NOT_FOUND.TAG);
    }
    Certificate certificate =
        certificateDao
            .save(certificateInterpreter.fromDto(certificateDto))
            .orElseThrow(
                () ->
                    new CreationException(
                        NOT_CREATED_CERTIFICATE, CustomHttpStatus.CONFLICT.CERTIFICATE));
    certificate.setTags(Collections.emptyList());
    certificateDto.setTags(new HashSet<>(certificateDto.getTags()).stream().toList());
    CertificateDto certificateDtoResult = certificateInterpreter.toDto(certificate);
    certificateDtoResult.setTags(tagService.createTagsOnlyByNameOrId(certificateDto.getTags()));
    certificateDtoResult
        .getTags()
        .forEach(
            tagDto ->
                certificateDao.addTagToCertificate(certificateDtoResult.getId(), tagDto.getId()));
    return certificateDtoResult;
  }

  @Override
  public void removeById(String id) {
    List<String> errors = StringIdValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.CERTIFICATE);
    }
    certificateDao.removeById(Long.parseLong(id));
  }

  @Override
  @Transactional
  public CertificateDto update(String id, CertificateDto certificateDto) {
    List<String> errors = StringIdValidator.idValidate(id);
    errors.addAll(CertificateDtoValidator.updateCertificateDtoValidate(certificateDto));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors, CustomHttpStatus.BAD_REQUEST.CERTIFICATE);
    }
    errors = tagsIsExists(certificateDto.getTags());
    if (!errors.isEmpty()) {
      throw new NotFoundException(errors, CustomHttpStatus.NOT_FOUND.TAG);
    }
    long valueId = Long.parseLong(id);
    certificateDto.setId(valueId);
    Certificate certificate =
        certificateDao
            .update(certificateInterpreter.fromDto(certificateDto))
            .orElseThrow(
                () ->
                    new CreationException(
                        NOT_UPDATED_CERTIFICATE, CustomHttpStatus.CONFLICT.CERTIFICATE));
    certificate.setTags(Collections.emptyList());
    certificateDto.setTags(new HashSet<>(certificateDto.getTags()).stream().toList());
    certificateDao.removeAllTagsByCertificateId(valueId);
    CertificateDto certificateDtoResult = certificateInterpreter.toDto(certificate);
    certificateDtoResult.setTags(tagService.createTagsOnlyByNameOrId(certificateDto.getTags()));
    certificateDtoResult
        .getTags()
        .forEach(
            tagDto ->
                certificateDao.addTagToCertificate(certificateDtoResult.getId(), tagDto.getId()));
    return certificateDtoResult;
  }

  private List<String> tagsIsExists(List<TagDto> tagDtoList) {
    List<String> errors = new ArrayList<>();
    tagDtoList.forEach(
        tag -> {
          if (tag.getId() != null) {
            if (!tagService.isExists(tag.getId())) {
              errors.add(NOT_FOUND_TAG.formatted(tag.getId()));
            }
          }
        });
    return errors;
  }
}
