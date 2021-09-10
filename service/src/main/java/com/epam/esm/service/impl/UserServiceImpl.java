package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.PaginateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
  private final UserDao userDao;
  private final Mapper<User, UserDto> mapper;
  private final Mapper<Order, OrderDto> orderMapper;
  private final IdValidator idValidator;
  private final PaginateValidator paginateValidator;

  @Autowired
  public UserServiceImpl(
      UserDao userDao,
      Mapper<User, UserDto> mapper,
      Mapper<Order, OrderDto> orderMapper,
      IdValidator idValidator,
      PaginateValidator paginateValidator) {
    this.userDao = userDao;
    this.mapper = mapper;
    this.orderMapper = orderMapper;
    this.idValidator = idValidator;
    this.paginateValidator = paginateValidator;
  }

  @Override
  public int findPaginated(int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    int countOfRecords = userDao.findCountOfRecords();
    int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
    return countPages == 0 ? 1 : countPages;
  }

  @Override
  public int findUserOrdersPaginated(Long id, int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(idValidator.idValidate(id));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    int countOfRecords = userDao.findCountOfRecordsOfUserOrders(id);
    int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
    return countPages == 0 ? 1 : countPages;
  }

  @Override
  public UserDto findById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    User user = userDao.findById(id);
    if (user == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.USER.getCode(),
              LanguageManager.getMessage(LocaleMessages.USER_NOT_FOUND).formatted(id)));
    }
    return mapper.mapToDto(user, true);
  }

  @Override
  public List<UserDto> findAll(int page, int size, List<String> sortParams) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(paginateValidator.pageValidate(page));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return userDao.findAll(page, size, sortParams).stream()
        .map(user -> mapper.mapToDto(user, false))
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderDto> findOrdersByUserId(Long id, int page, int size, List<String> sortParams) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(paginateValidator.pageValidate(page));
    errors.addAll(idValidator.idValidate(id));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return userDao.findUserOrders(id, page, size, sortParams).stream()
        .map(order -> orderMapper.mapToDto(order, false))
        .collect(Collectors.toList());
  }
}
