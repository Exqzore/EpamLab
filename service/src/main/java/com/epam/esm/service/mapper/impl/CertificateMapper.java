package com.epam.esm.service.mapper.impl;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CertificateMapper implements Mapper<Certificate, CertificateDto> {
  @Override
  public CertificateDto mapToDto(Certificate entity, boolean mapNestedEntities) {
    Mapper<Tag, TagDto> mapper = new TagMapper();
    CertificateDto certificateDto = new CertificateDto();
    certificateDto.setId(entity.getId());
    certificateDto.setName(entity.getName());
    certificateDto.setDescription(entity.getDescription());
    certificateDto.setPrice(entity.getPrice().doubleValue());
    certificateDto.setDuration(entity.getDuration());
    if (mapNestedEntities) {
      Set<Tag> tags = entity.getTags();
      List<TagDto> tagDtoList =
          tags.stream().map(tag -> mapper.mapToDto(tag, true)).collect(Collectors.toList());
      certificateDto.setTags(tagDtoList);
    } else {
      certificateDto.setTags(Collections.emptyList());
    }
    certificateDto.setCreateDate(entity.getCreateDate().toString());
    certificateDto.setLastUpdateDate(entity.getLastUpdateDate().toString());
    return certificateDto;
  }

  @Override
  public Certificate mapFromDto(CertificateDto dto) {
    Mapper<Tag, TagDto> mapper = new TagMapper();
    Certificate certificate = new Certificate();
    certificate.setId(dto.getId());
    certificate.setName(dto.getName());
    certificate.setDescription(dto.getDescription());
    certificate.setPrice(BigDecimal.valueOf(dto.getPrice()));
    certificate.setDuration(dto.getDuration());
    List<TagDto> tagDtoList = dto.getTags();
    if (tagDtoList != null) {
      Set<Tag> tags = tagDtoList.stream().map(mapper::mapFromDto).collect(Collectors.toSet());
      certificate.setTags(tags);
    }
    return certificate;
  }
}
