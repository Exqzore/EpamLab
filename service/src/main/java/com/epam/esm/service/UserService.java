package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;

import java.util.List;

/** The interface User service. */
public interface UserService extends GeneralService<UserDto> {
  /**
   * Find count of pages.
   *
   * @param size size of page
   * @return Count of pages
   */
  int getCountOfPagesOfUserOrders(Long id, int size);

  /**
   * Find all users.
   *
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of users
   */
  List<UserDto> findAll(int page, int size, List<String> sortParams);

  /**
   * Find orders by user id.
   *
   * @param id user id
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of orders
   */
  List<OrderDto> findOrdersByUserId(Long id, int page, int size, List<String> sortParams);
}
