package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.PaginateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class UserServiceImplTest {
  private UserDao userDao;
  private Mapper<User, UserDto> mapper;
  private IdValidator idValidator;
  private PaginateValidator paginateValidator;

  private UserService userService;

  @BeforeEach
  public void init() {
    userDao = Mockito.mock(UserDao.class);
    mapper = Mockito.mock(UserMapper.class);
    Mapper<Order, OrderDto> orderMapper = Mockito.mock(OrderMapper.class);
    idValidator = Mockito.mock(IdValidator.class);
    paginateValidator = Mockito.mock(PaginateValidator.class);

    userService = new UserServiceImpl(userDao, mapper, orderMapper, idValidator, paginateValidator);
  }

  @Test
  void getCountOfPages() {
    // given
    int size = 80;
    int expected = 2;

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(userDao.findCountOfRecords()).thenReturn(122);
    int actual = userService.getCountOfPages(size);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(userDao, Mockito.times(1)).findCountOfRecords();
  }

  @Test
  void getCountOfPagesOfUserOrders() {
    // given
    long id = 1;
    int size = 80;
    int expected = 2;

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(userDao.findCountOfRecordsOfUserOrders(id)).thenReturn(122);
    int actual = userService.getCountOfPagesOfUserOrders(id, size);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(userDao, Mockito.times(1)).findCountOfRecordsOfUserOrders(id);
  }

  @Test
  void findById() {
    // given
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    User user = new User(id, "", "", Collections.emptySet());
    UserDto expected = new UserDto(id, "", "", Collections.emptyList());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(userDao.findById(id)).thenReturn(user);
    Mockito.when(mapper.mapToDto(user, true)).thenReturn(expected);
    UserDto actual = userService.findById(id);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(userDao, Mockito.times(1)).findById(id);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(user, true);
  }

  @Test
  void findAll() {
    // given
    List<String> sortParams = new ArrayList<>();
    int page = 1;
    int size = 50;
    List<UserDto> expected = new ArrayList<>();

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(paginateValidator.pageValidate(page)).thenReturn(Collections.emptyList());
    Mockito.when(userDao.findAll(page, size, sortParams)).thenReturn(Collections.emptyList());
    List<UserDto> actual = userService.findAll(page, size, sortParams);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(paginateValidator, Mockito.times(1)).pageValidate(page);
    Mockito.verify(userDao, Mockito.times(1)).findAll(page, size, sortParams);
  }

  @Test
  void findOrdersByUserId() {
    // given
    long id = 1;
    List<String> sortParams = new ArrayList<>();
    int page = 1;
    int size = 50;
    List<OrderDto> expected = new ArrayList<>();

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(paginateValidator.pageValidate(page)).thenReturn(Collections.emptyList());
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(userDao.findUserOrders(id, page, size, sortParams)).thenReturn(Collections.emptyList());
    List<OrderDto> actual = userService.findOrdersByUserId(id, page, size, sortParams);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(paginateValidator, Mockito.times(1)).pageValidate(page);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(userDao, Mockito.times(1)).findUserOrders(id, page, size, sortParams);
  }
}
