package com.epam.esm.service;

import com.epam.esm.service.dto.CertificateDto;

import java.util.List;

/** The interface Certificate service. */
public interface CertificateService extends GeneralService<CertificateDto>, Creatable<CertificateDto>, Deletable {
  /**
   * Find all certificates.
   *
   * @param partOfNameOrDescription part of name or description by which the search is performed
   * @param tagNames names of the tags by which the search is performed
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of certificates
   */
  List<CertificateDto> findByCriteria(
      String partOfNameOrDescription,
      List<String> tagNames,
      List<String> sortParams,
      int page,
      int size);

  /**
   * Update certificate.
   *
   * @param certificateDto the certificate to be updated in the database
   * @return The updated certificate
   */
  CertificateDto update(Long id, CertificateDto certificateDto);
}
