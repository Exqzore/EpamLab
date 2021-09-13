package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.mapper.impl.CertificateMapper;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.OrderDtoValidator;
import com.epam.esm.service.validator.PaginateValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OrderServiceImplTest {
  private OrderDao orderDao;
  private UserService userService;
  private Mapper<Order, OrderDto> mapper;
  private Mapper<User, UserDto> userMapper;
  private IdValidator idValidator;
  private PaginateValidator paginateValidator;
  private OrderDtoValidator orderDtoValidator;

  private OrderService orderService;

  @BeforeEach
  public void init() {
    orderDao = Mockito.mock(OrderDao.class);
    userService = Mockito.mock(UserService.class);
    CertificateService certificateService = Mockito.mock(CertificateService.class);
    mapper = Mockito.mock(OrderMapper.class);
    userMapper = Mockito.mock(UserMapper.class);
    Mapper<Certificate, CertificateDto> certificateMapper = Mockito.mock(CertificateMapper.class);
    idValidator = Mockito.mock(IdValidator.class);
    paginateValidator = Mockito.mock(PaginateValidator.class);
    orderDtoValidator = Mockito.mock(OrderDtoValidator.class);

    orderService =
        new OrderServiceImpl(
            orderDao,
            userService,
                certificateService,
            mapper,
            userMapper,
                certificateMapper,
            idValidator,
            paginateValidator,
            orderDtoValidator);
  }

  @Test
  void getCountOfPages() {
    // given
    int size = 80;
    int expected = 2;

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(orderDao.findCountOfRecords()).thenReturn(122);
    int actual = orderService.getCountOfPages(size);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(orderDao, Mockito.times(1)).findCountOfRecords();
  }

  @Test
  void findById() {
    // given
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Order order =
        new Order(
            id,
            BigDecimal.ZERO,
            dateTime,
            new User(id, "", "", Collections.emptySet()),
            Collections.emptyList());
    OrderDto expected = new OrderDto(id, 0D, dateTime.toString(), id, Collections.emptyList());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(orderDao.findById(id)).thenReturn(order);
    Mockito.when(mapper.mapToDto(order, true)).thenReturn(expected);
    OrderDto actual = orderService.findById(id);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(orderDao, Mockito.times(1)).findById(id);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(order, true);
  }

  @Test
  void findAll() {
    // given
    List<String> sortParams = new ArrayList<>();
    int page = 1;
    int size = 50;
    List<OrderDto> expected = new ArrayList<>();

    // when
    Mockito.when(paginateValidator.sizeValidate(size)).thenReturn(Collections.emptyList());
    Mockito.when(paginateValidator.pageValidate(page)).thenReturn(Collections.emptyList());
    Mockito.when(orderDao.findAll(page, size, sortParams)).thenReturn(Collections.emptyList());
    List<OrderDto> actual = orderService.findAll(page, size, sortParams);

    // then
    Assertions.assertEquals(expected, actual);
    Mockito.verify(paginateValidator, Mockito.times(1)).sizeValidate(size);
    Mockito.verify(paginateValidator, Mockito.times(1)).pageValidate(page);
    Mockito.verify(orderDao, Mockito.times(1)).findAll(page, size, sortParams);
  }

  @Test
  void create() {
    // given
    long id = 1;
    UserDto userDto = new UserDto();
    userDto.setId(id);
    User user = new User();
    user.setId(id);
    LocalDateTime dateTime = LocalDateTime.now();
    Order order =
        new Order(
            id,
            BigDecimal.ZERO,
            dateTime,
            new User(id, "", "", Collections.emptySet()),
            Collections.emptyList());
    OrderDto orderDto = new OrderDto(id, 0D, dateTime.toString(), id, Collections.emptyList());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(orderDtoValidator.orderValidate(orderDto)).thenReturn(Collections.emptyList());
    Mockito.when(userService.findById(id)).thenReturn(userDto);
    Mockito.when(mapper.mapFromDto(orderDto)).thenReturn(order);
    Mockito.when(userMapper.mapFromDto(userDto)).thenReturn(user);
    Mockito.when(orderDao.save(order)).thenReturn(order);
    Mockito.when(mapper.mapToDto(order, false)).thenReturn(orderDto);
    OrderDto actual = orderService.create(id, orderDto);

    // then
    Assertions.assertEquals(orderDto, actual);
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(orderDtoValidator, Mockito.times(1)).orderValidate(orderDto);
    Mockito.verify(userService, Mockito.times(1)).findById(userDto.getId());
    Mockito.verify(mapper, Mockito.times(1)).mapFromDto(orderDto);
    Mockito.verify(userMapper, Mockito.times(1)).mapFromDto(userDto);
    Mockito.verify(orderDao, Mockito.times(1)).save(order);
    Mockito.verify(mapper, Mockito.times(1)).mapToDto(order, false);
  }

  @Test
  void removeById() throws NoSuchResultException {
    // given
    long id = 1;
    LocalDateTime dateTime = LocalDateTime.now();
    Order order =
        new Order(
            id,
            BigDecimal.ZERO,
            dateTime,
            new User(id, "", "", Collections.emptySet()),
            Collections.emptyList());

    // when
    Mockito.when(idValidator.idValidate(id)).thenReturn(Collections.emptyList());
    Mockito.when(orderDao.findById(id)).thenReturn(order);
    orderService.removeById(id);

    // then
    Mockito.verify(idValidator, Mockito.times(1)).idValidate(id);
    Mockito.verify(orderDao, Mockito.times(1)).findById(id);
    Mockito.verify(orderDao, Mockito.times(1)).removeById(id);
  }
}
