package com.epam.esm.dao;

import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.exception.NoSuchResultException;

import java.util.List;

/** The interface Order service. */
public interface OrderDao {
  /**
   * Find by order id.
   *
   * @param id the order id
   * @return The founded order
   */
  Order findById(long id);

  /**
   * Find all orders.
   *
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of orders
   */
  List<Order> findAll(int page, int size, List<String> sortParams);

  /**
   * Create order.
   *
   * @param order the order to be placed in the database
   * @return The created order
   */
  Order save(Order order);

  /**
   * Find count of orders on db.
   *
   * @return Count of orders
   */
  int findCountOfRecords();

  /**
   * Remove order by id.
   *
   * @param id the order id
   */
  void removeById(long id) throws NoSuchResultException;
}
