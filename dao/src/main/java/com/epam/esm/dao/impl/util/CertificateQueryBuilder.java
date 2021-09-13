package com.epam.esm.dao.impl.util;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CertificateQueryBuilder {
  private static final String ID_PARAMETER = "id";
  private static final String NAME_PARAMETER = "name";
  private static final String DESCRIPTION_PARAMETER = "description";

  public static void build(
      CertificateSearchCriteria criteria,
      Root<Certificate> root,
      CriteriaQuery<Certificate> query,
      EntityManager entityManager) {
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
