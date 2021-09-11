package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.dao.impl.sorting.CertificateSortBy;
import com.epam.esm.dao.impl.sorting.SortParameterInserter;
import com.epam.esm.dao.impl.sorting.SortingParameter;
import com.epam.esm.dao.impl.util.CertificateSearchCriteria;
import com.epam.esm.dao.impl.util.SortingCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@EntityScan(basePackages = "com.epam.esm.dao.entity")
public class CertificateDaoImpl implements CertificateDao, SortParameterInserter {
  private static final String ID_PARAMETER = "id";
  private static final String NAME_PARAMETER = "name";
  private static final String DESCRIPTION_PARAMETER = "description";

  private final SortingCalculator<CertificateSortBy> sortingCalculator;
  private EntityManager entityManager;

  @Autowired
  public CertificateDaoImpl(SortingCalculator<CertificateSortBy> sortingCalculator) {
    this.sortingCalculator = sortingCalculator;
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Certificate> findByCriteria(
      CertificateSearchCriteria certificateSearchCriteria,
      int page,
      int size,
      List<String> sortParams) {
    List<SortingParameter<CertificateSortBy>> sorting =
        sortingCalculator.calculateSortParams(CertificateSortBy.class, sortParams);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Certificate> cq = cb.createQuery(Certificate.class);
    Root<Certificate> root = cq.from(Certificate.class);
    cq.select(root);
    buildQuery(certificateSearchCriteria, root, cq);
    addSortingParams(cq, cb, root, sorting);
    return entityManager
        .createQuery(cq)
        .setFirstResult((page - 1) * size)
        .setMaxResults(size)
        .getResultList();
  }

  @Override
  public Certificate findById(long id) {
    return entityManager.find(Certificate.class, id);
  }

  @Override
  public Certificate save(Certificate certificate) {
    entityManager.persist(certificate);
    entityManager.flush();
    return certificate;
  }

  @Override
  public Certificate update(Certificate certificate) {
    certificate = entityManager.merge(certificate);
    entityManager.flush();
    return certificate;
  }

  @Override
  public void removeById(long id) throws NoSuchResultException {
    Certificate certificate = entityManager.find(Certificate.class, id);
    if (certificate == null) {
      throw new NoSuchResultException();
    }
    entityManager.remove(certificate);
  }

  @Override
  public int findCountOfRecords() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Certificate> criteriaQuery = cb.createQuery(Certificate.class);
    criteriaQuery.select(criteriaQuery.from(Certificate.class));
    return entityManager.createQuery(criteriaQuery).getResultList().size();
  }

  private void buildQuery(
      CertificateSearchCriteria criteria,
      Root<Certificate> root,
      CriteriaQuery<Certificate> query) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    List<Predicate> predicates = new ArrayList<>();
    String partOfNameOrDescription = criteria.getPartOfNameOrDescription();
    if (partOfNameOrDescription != null) {
      predicates.add(builder.like(root.get(NAME_PARAMETER), "%" + partOfNameOrDescription + "%"));
      predicates.add(
          builder.like(root.get(DESCRIPTION_PARAMETER), "%" + partOfNameOrDescription + "%"));
    }
    if (criteria.getTagNames() != null) {
      Join<Certificate, Tag> tags = root.join("tags");
      CriteriaBuilder.In<String> inTags = builder.in(tags.get("name"));
      for (String tagName : criteria.getTagNames()) {
        inTags.value(tagName);
      }
      predicates.add(inTags);
      query.groupBy(root.get(ID_PARAMETER));
      query.having(
          builder.equal(builder.count(root.get(ID_PARAMETER)), criteria.getTagNames().size()));
    }
    Predicate[] predArray = new Predicate[predicates.size()];
    predicates.toArray(predArray);
    query.where(predArray);
  }
}
