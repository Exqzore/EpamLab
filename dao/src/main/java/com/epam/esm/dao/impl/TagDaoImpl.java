package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.dao.impl.sorting.SortParameterInserter;
import com.epam.esm.dao.impl.sorting.SortingParameter;
import com.epam.esm.dao.impl.sorting.TagSortBy;
import com.epam.esm.dao.impl.util.SortingCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@EntityScan(basePackages = "com.epam.esm.dao.entity")
public class TagDaoImpl implements TagDao, SortParameterInserter {
  private static final String NAME_PARAMETER = "name";
  private static final String ID_PARAMETER = "id";
  private static final String IS_DELETED_PARAMETER = "isDeleted";
  private static final String FIND_WILDLY_USED = """
    SELECT t.id, t.name, t.is_deleted FROM orders o
    INNER JOIN order_certificate_membership ocm ON ocm.order_id = o.id
    INNER JOIN certificates c ON c.id = ocm.certificate_id
    INNER JOIN tag_certificate_membership tcm ON tcm.certificate_id = c.id
    INNER JOIN tags t ON t.id = tcm.tag_id
    WHERE o.user_id IN (
      SELECT tmp.user_id FROM (
        SELECT SUM(orders.cost) sumCost, user_id
        FROM orders
        GROUP BY user_id
        ORDER BY sumCost DESC LIMIT 1
      ) AS tmp
    )
    GROUP BY t.id
    ORDER BY COUNT(t.id) DESC LIMIT 1
    """;
  private final SortingCalculator<TagSortBy> sortingCalculator;
  private EntityManager entityManager;

  @Autowired
  public TagDaoImpl(SortingCalculator<TagSortBy> sortingCalculator) {
    this.sortingCalculator = sortingCalculator;
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Tag> findAll(int page, int size, List<String> sortParams) {
    List<SortingParameter<TagSortBy>> sorting =
            sortingCalculator.calculateSortParams(TagSortBy.class, sortParams);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> root = cq.from(Tag.class);
    cq.select(root).where(cb.equal(root.get(IS_DELETED_PARAMETER), false));
    addSortingParams(cq, cb, root, sorting);
    return entityManager.createQuery(cq).setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
  }

  @Override
  public List<Tag> findByCertificateId(long certificateId, int page, int size, List<String> sortParams) {
    List<SortingParameter<TagSortBy>> sorting =
            sortingCalculator.calculateSortParams(TagSortBy.class, sortParams);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb
            .createQuery(Tag.class);
    Root<Certificate> root = cq.from(Certificate.class);
    cq.where(cb.equal(root.get(ID_PARAMETER), certificateId));
    Join<Certificate, Tag> tags = root.join("tags");
    cq.select(tags);
    addSortingParams(cq, cb, tags, sorting);
    return entityManager.createQuery(cq).setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
  }


  @Override
  public Tag findById(long id) {
    Tag result = null;
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> root = cq.from(Tag.class);
    cq.select(root).where(cb.equal(root.get(ID_PARAMETER), id), cb.equal(root.get(IS_DELETED_PARAMETER), false));
    TypedQuery<Tag> typedQuery = entityManager.createQuery(cq);
    if (!typedQuery.getResultList().isEmpty()) {
      result = typedQuery.getSingleResult();
    }
    return result;
  }

  @Override
  public Tag findByName(String name) {
    Tag result = null;
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> root = cq.from(Tag.class);
    cq.select(root).where(cb.equal(root.get(NAME_PARAMETER), name), cb.equal(root.get(IS_DELETED_PARAMETER), false));
    TypedQuery<Tag> typedQuery = entityManager.createQuery(cq);
    if (!typedQuery.getResultList().isEmpty()) {
      result = typedQuery.getSingleResult();
    }
    return result;
  }

  @Override
  public Tag save(Tag tag) {
    tag.setId(null);
    Tag result;
    if (findByName(tag.getName()) != null) {
      result = null;
    } else {
      if (isDeletedExists(tag.getName())) {
        setDeleted(tag.getName(), false);
      } else {
        entityManager.persist(tag);
      }
      result = findByName(tag.getName());
    }
    return result;
  }

  @Override
  public void removeById(long id) throws NoSuchResultException {
    Tag tag = entityManager.find(Tag.class, id);
    if(tag != null) {
      entityManager.detach(tag);
      tag.setDeleted(true);
      entityManager.merge(tag);
    } else {
      throw new NoSuchResultException();
    }
  }

  @Override
  public void removeByName(String name) throws NoSuchResultException {
    Tag tag = findByName(name);
    if(tag != null) {
      entityManager.detach(tag);
      tag.setDeleted(true);
      entityManager.merge(tag);
    } else {
      throw new NoSuchResultException();
    }
  }

  @Override
  public void setDeleted(String name, boolean isDeleted) {
    Tag tag = findAnyByName(name);
    if(tag != null) {
      entityManager.detach(tag);
      tag.setDeleted(isDeleted);
      entityManager.merge(tag);
    }
  }

  @Override
  public void setDeleted(long id, boolean isDeleted) {
    Tag tag = entityManager.find(Tag.class, id);
    if(tag != null) {
      entityManager.detach(tag);
      tag.setDeleted(isDeleted);
      entityManager.merge(tag);
    }
  }

  @Override
  public boolean isDeletedExists(String name) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> root = cq.from(Tag.class);
    cq.select(root).where(cb.equal(root.get(NAME_PARAMETER), name), cb.equal(root.get(IS_DELETED_PARAMETER), true));
    return !entityManager.createQuery(cq).getResultList().isEmpty();
  }

  @Override
  public boolean isDeletedExists(long id) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> root = cq.from(Tag.class);
    cq.select(root).where(cb.equal(root.get(ID_PARAMETER), id), cb.equal(root.get(IS_DELETED_PARAMETER), true));
    return !entityManager.createQuery(cq).getResultList().isEmpty();
  }

  @Override
  public Tag findWildlyUsed() {
    Tag result = null;
    Query query = entityManager.createNativeQuery(FIND_WILDLY_USED, Tag.class);
    if (!query.getResultList().isEmpty()) {
      result = (Tag) query.getSingleResult();
    }
    return result;
  }

  @Override
  public int findCountOfRecords() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = cb.createQuery(Tag.class);
    Root<Tag> root = criteriaQuery.from(Tag.class);
    criteriaQuery.select(root).where(cb.equal(root.get(IS_DELETED_PARAMETER), false));
    return entityManager.createQuery(criteriaQuery).getResultList().size();
  }

  private Tag findAnyByName(String name) {
    Tag result = null;
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
    Root<Tag> root = cq.from(Tag.class);
    cq.select(root).where(cb.equal(root.get(NAME_PARAMETER), name));
    TypedQuery<Tag> typedQuery = entityManager.createQuery(cq);
    if (!typedQuery.getResultList().isEmpty()) {
      result = typedQuery.getSingleResult();
    }
    return result;
  }
}
