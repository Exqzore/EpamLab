package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;

import java.util.List;

/** The interface Order service. */
public interface OrderService {
  /**
   * Find count of pages.
   *
   * @param size size of page
   * @return Count of pages
   */
  int getCountOfPages(int size);

  /**
   * Find by order id.
   *
   * @param id the order id
   * @return The founded order
   */
  OrderDto findById(Long id);

  /**
   * Find all orders.
   *
   * @param page number of page
   * @param size page size
   * @param sortParams parameters of sorting
   * @return A list of orders
   */
  List<OrderDto> findAll(int page, int size, List<String> sortParams);

  /**
   * Create order.
   *
   * @param userId owner id
   * @param orderDto the order to be placed in the database
   * @return The created certificate
   */
  OrderDto create(Long userId, OrderDto orderDto);

  /**
   * Remove order by id.
   *
   * @param id the order id
   */
  void removeById(Long id);
}
