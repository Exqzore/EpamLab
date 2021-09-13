package com.epam.esm.service.mapper.impl;

import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<User, UserDto> {
  @Override
  public UserDto mapToDto(User entity, boolean mapNestedEntities) {
    Mapper<Order, OrderDto> mapper = new OrderMapper();
    UserDto userDto = new UserDto();
    userDto.setId(entity.getId());
    userDto.setName(entity.getName());
    userDto.setSurname(entity.getSurname());
    if (mapNestedEntities) {
      Set<Order> orders = entity.getOrders();
      List<OrderDto> orderDtoSet =
          orders.stream().map(order -> mapper.mapToDto(order, true)).collect(Collectors.toList());
      userDto.setOrders(orderDtoSet);
    } else {
      userDto.setOrders(Collections.emptyList());
    }
    return userDto;
  }

  @Override
  public User mapFromDto(UserDto dto) {
    Mapper<Order, OrderDto> mapper = new OrderMapper();
    User user = new User();
    user.setId(dto.getId());
    user.setName(dto.getName());
    user.setSurname(dto.getSurname());
    List<OrderDto> orderDtoSet = dto.getOrders();
    Set<Order> orders = orderDtoSet.stream().map(mapper::mapFromDto).collect(Collectors.toSet());
    user.setOrders(orders);
    return user;
  }
}
