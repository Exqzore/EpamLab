package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dao.impl.sorting.OrderSortBy;
import com.epam.esm.dao.impl.sorting.SortingParameter;
import com.epam.esm.dao.impl.sorting.UserSortBy;
import com.epam.esm.dao.impl.util.SortParameterInserter;
import com.epam.esm.dao.impl.util.SortingCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
  private static final String ID_PARAMETER = "id";
  private final SortingCalculator<UserSortBy> sortingCalculator;
  private final SortingCalculator<OrderSortBy> ordersSortingCalculator;
  private EntityManager entityManager;

  @Autowired
  public UserDaoImpl(
      SortingCalculator<UserSortBy> sortingCalculator,
      SortingCalculator<OrderSortBy> ordersSortingCalculator) {
    this.sortingCalculator = sortingCalculator;
    this.ordersSortingCalculator = ordersSortingCalculator;
  }

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public User findById(long id) {
    return entityManager.find(User.class, id);
  }

  @Override
  public List<User> findAll(int page, int size, List<String> sortParams) {
    List<SortingParameter<UserSortBy>> sorting =
        sortingCalculator.calculateSortParams(UserSortBy.class, sortParams);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> cq = cb.createQuery(User.class);
    Root<User> root = cq.from(User.class);
    cq.select(root);
    SortParameterInserter.addSortingParams(cq, cb, root, sorting);
    return entityManager
        .createQuery(cq)
        .setFirstResult((page - 1) * size)
        .setMaxResults(size)
        .getResultList();
  }

  @Override
  public List<Order> findUserOrders(long userId, int page, int size, List<String> sortParams) {
    List<SortingParameter<OrderSortBy>> sorting =
        ordersSortingCalculator.calculateSortParams(OrderSortBy.class, sortParams);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<User> root = cq.from(User.class);
    Join<User, Order> orders = root.join("orders");
    cq.select(orders).where(cb.equal(root.get(ID_PARAMETER), userId));
    SortParameterInserter.addSortingParams(cq, cb, orders, sorting);
    return entityManager
        .createQuery(cq)
        .setFirstResult((page - 1) * size)
        .setMaxResults(size)
        .getResultList();
  }

  @Override
  public int findCountOfRecords() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
    criteriaQuery.select(criteriaQuery.from(User.class));
    return entityManager.createQuery(criteriaQuery).getResultList().size();
  }

  @Override
  public int findCountOfRecordsOfUserOrders(long userId) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<User> root = cq.from(User.class);
    Join<User, Order> orders = root.join("orders");
    cq.select(orders).where(cb.equal(root.get(ID_PARAMETER), userId));
    return entityManager.createQuery(cq).getResultList().size();
  }
}
