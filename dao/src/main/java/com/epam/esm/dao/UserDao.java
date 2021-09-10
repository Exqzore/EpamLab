package com.epam.esm.dao;

import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;

import java.util.List;

/** The interface User service. */
public interface UserDao {
  /**
   * Find by user id.
   *
   * @param id the user id
   * @return The founded user
   */
  User findById(long id);

  /**
   * Find all users.
   *
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of users
   */
  List<User> findAll(int page, int size, List<String> sortParams);

  /**
   * Find all user orders.
   *
   * @param id user id
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of orders
   */
  List<Order> findUserOrders(long id, int page, int size, List<String> sortParams);

  /**
   * Find count of users on db.
   *
   * @return Count of users
   */
  int findCountOfRecords();

  /**
   * Find count of user orders on db by user id.
   *
   * @param userId the user id
   * @return Count of orders
   */
  int findCountOfRecordsOfUserOrders(long userId);
}
