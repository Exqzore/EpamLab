package com.epam.esm.dao;

import com.epam.esm.dao.specification.Specification;
import com.epam.esm.entity.Certificate;

import java.util.List;
import java.util.Optional;

/** The interface Certificate dao. */
public interface CertificateDao {
  /**
   * Find certificates by specification.
   *
   * @return A list of certificates
   */
  List<Certificate> findBySpecification(Specification specification, String sortParams);

  /**
   * Find by certificate id.
   *
   * @param id the certificate id
   * @return The founded certificate
   */
  Optional<Certificate> findById(long id);

  /**
   * Create certificate.
   *
   * @param certificate the certificate to be placed in the database
   * @return The created certificate
   */
  Optional<Certificate> save(Certificate certificate);

  /**
   * Update certificate.
   *
   * @param certificate the certificate to be updated in the database
   * @return The updated certificate
   */
  Optional<Certificate> update(Certificate certificate);

  /**
   * Remove certificate by id.
   *
   * @param id the certificate id
   */
  void removeById(long id);

  /**
   * Remove all tags of certificate.
   *
   * @param id the certificate id
   */
  void removeAllTagsByCertificateId(long id);

  /**
   * Adds a tag to the certificate .
   *
   * @param certificateId the certificate id
   * @param tagId the tag id
   */
  void addTagToCertificate(long certificateId, long tagId);
}
