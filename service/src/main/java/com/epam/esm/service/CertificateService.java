package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.util.List;

/** The interface Certificate service. */
public interface CertificateService {
  /**
   * Find all certificates.
   *
   * @param partOfNameOrDescription part of name or description by which the search is performed
   * @param tagName name of the tag by which the search is performed
   * @param sortParams sorting options
   * @return A list of certificates
   */
  List<CertificateDto> findByCriteria(
      String partOfNameOrDescription, String tagName, List<String> sortParams);

  /**
   * Find by certificate id.
   *
   * @param id the certificate id
   * @return The founded certificate
   */
  CertificateDto findById(String id);

  /**
   * Create certificate.
   *
   * @param certificateDto the certificate to be placed in the database
   * @return The created certificate
   */
  CertificateDto create(CertificateDto certificateDto);

  /**
   * Remove certificate by id.
   *
   * @param id the certificate id
   */
  void removeById(String id);

  /**
   * Update certificate.
   *
   * @param certificateDto the certificate to be updated in the database
   * @return The updated certificate
   */
  CertificateDto update(String id, CertificateDto certificateDto);
}
