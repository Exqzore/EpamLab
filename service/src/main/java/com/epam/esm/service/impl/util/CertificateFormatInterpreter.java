package com.epam.esm.service.impl.util;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CertificateFormatInterpreter {
  public Certificate fromDto(CertificateDto certificateDto) {
    Certificate certificate = new Certificate();
    certificate.setId(certificateDto.getId());
    certificate.setName(certificateDto.getName());
    certificate.setDescription(certificateDto.getDescription());
    certificate.setPrice(certificateDto.getPrice());
    certificate.setDuration(certificateDto.getDuration());
    certificate.setTags(
        certificateDto.getTags().stream()
            .map(o -> new Tag(o.getId(), o.getName()))
            .collect(Collectors.toList()));
    return certificate;
  }

  public CertificateDto toDto(Certificate certificate) {
    CertificateDto certificateDto = new CertificateDto();
    certificateDto.setId(certificate.getId());
    certificateDto.setName(certificate.getName());
    certificateDto.setDescription(certificate.getDescription());
    certificateDto.setPrice(certificate.getPrice());
    certificateDto.setDuration(certificate.getDuration());
    certificateDto.setTags(
        certificate.getTags().stream()
            .map(o -> new TagDto(o.getId(), o.getName()))
            .collect(Collectors.toList()));
    certificateDto.setCreateDate(certificate.getCreateDate().toString());
    certificateDto.setLastUpdateDate(certificate.getLastUpdateDate().toString());
    return certificateDto;
  }
}
