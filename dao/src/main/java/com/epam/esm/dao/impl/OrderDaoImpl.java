package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.dao.impl.sorting.OrderSortBy;
import com.epam.esm.dao.impl.sorting.SortingParameter;
import com.epam.esm.dao.impl.util.SortParameterInserter;
import com.epam.esm.dao.impl.util.SortingCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
  private final SortingCalculator<OrderSortBy> sortingCalculator;
  private EntityManager entityManager;

  @Autowired
  public OrderDaoImpl(SortingCalculator<OrderSortBy> sortingCalculator) {
    this.sortingCalculator = sortingCalculator;
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Order findById(long id) {
    return entityManager.find(Order.class, id);
  }

  @Override
  public List<Order> findAll(int page, int size, List<String> sortParams) {
    List<SortingParameter<OrderSortBy>> sorting =
        sortingCalculator.calculateSortParams(OrderSortBy.class, sortParams);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> root = cq.from(Order.class);
    cq.select(root);
    SortParameterInserter.addSortingParams(cq, cb, root, sorting);
    return entityManager
        .createQuery(cq)
        .setFirstResult((page - 1) * size)
        .setMaxResults(size)
        .getResultList();
  }

  @Override
  public Order save(Order order) {
    entityManager.persist(order);
    entityManager.flush();
    return order;
  }

  @Override
  public int findCountOfRecords() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> criteriaQuery = cb.createQuery(Order.class);
    criteriaQuery.select(criteriaQuery.from(Order.class));
    return entityManager.createQuery(criteriaQuery).getResultList().size();
  }

  @Override
  public void removeById(long id) throws NoSuchResultException {
    Order order = entityManager.find(Order.class, id);
    if (order == null) {
      throw new NoSuchResultException();
    }
    entityManager.remove(order);
  }
}
