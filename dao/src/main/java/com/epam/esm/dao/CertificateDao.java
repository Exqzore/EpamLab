package com.epam.esm.dao;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.dao.impl.util.CertificateSearchCriteria;

import java.util.List;

/** The interface Certificate dao. */
public interface CertificateDao {
  /**
   * Find certificates by criteria.
   *
   * @param certificateSearchCriteria search criteria
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of certificates
   */
  List<Certificate> findByCriteria(
      CertificateSearchCriteria certificateSearchCriteria,
      int page,
      int size,
      List<String> sortParams);

  /**
   * Find by certificate id.
   *
   * @param id the certificate id
   * @return The founded certificate
   */
  Certificate findById(long id);

  /**
   * Create certificate.
   *
   * @param certificate the certificate to be placed in the database
   * @return The created certificate
   */
  Certificate save(Certificate certificate);

  /**
   * Update certificate.
   *
   * @param certificate the certificate to be updated in the database
   * @return The updated certificate
   */
  Certificate update(Certificate certificate);

  /**
   * Remove certificate by id.
   *
   * @param id the certificate id
   */
  void removeById(long id) throws NoSuchResultException;

  /**
   * Find count of certificates on db.
   *
   * @return Count of certificates
   */
  int findCountOfRecords();
}
