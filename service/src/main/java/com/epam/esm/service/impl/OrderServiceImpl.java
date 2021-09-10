package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.dao.exception.NoSuchResultException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.CreationException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.service.mapper.Mapper;
import com.epam.esm.service.validator.IdValidator;
import com.epam.esm.service.validator.OrderDtoValidator;
import com.epam.esm.service.validator.PaginateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
  private final OrderDao orderDao;
  private final UserService userService;
  private final CertificateService certificateService;
  private final Mapper<Order, OrderDto> mapper;
  private final Mapper<User, UserDto> userMapper;
  private final Mapper<Certificate, CertificateDto> certificateMapper;
  private final IdValidator idValidator;
  private final PaginateValidator paginateValidator;
  private final OrderDtoValidator orderDtoValidator;

  @Autowired
  public OrderServiceImpl(
      OrderDao orderDao,
      UserService userService,
      CertificateService certificateService,
      Mapper<Order, OrderDto> mapper,
      Mapper<User, UserDto> userMapper,
      Mapper<Certificate, CertificateDto> certificateMapper,
      IdValidator idValidator,
      PaginateValidator paginateValidator,
      OrderDtoValidator orderDtoValidator) {
    this.orderDao = orderDao;
    this.userService = userService;
    this.certificateService = certificateService;
    this.mapper = mapper;
    this.userMapper = userMapper;
    this.certificateMapper = certificateMapper;
    this.idValidator = idValidator;
    this.paginateValidator = paginateValidator;
    this.orderDtoValidator = orderDtoValidator;
  }

  @Override
  @Transactional
  public int findPaginated(int size) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    int countOfRecords = orderDao.findCountOfRecords();
    int countPages = countOfRecords % size == 0 ? countOfRecords / size : countOfRecords / size + 1;
    return countPages == 0 ? 1 : countPages;
  }

  @Override
  @Transactional
  public OrderDto findById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Order order = orderDao.findById(id);
    if (order == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.ORDER.getCode(),
              LanguageManager.getMessage(LocaleMessages.ORDER_NOT_FOUND).formatted(id)));
    }
    return mapper.mapToDto(order, true);
  }

  @Override
  @Transactional
  public List<OrderDto> findAll(int page, int size, List<String> sortParams) {
    List<ErrorMessageDto> errors = paginateValidator.sizeValidate(size);
    errors.addAll(paginateValidator.pageValidate(page));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    return orderDao.findAll(page, size, sortParams).stream()
        .map(order ->  mapper.mapToDto(order, false))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public OrderDto create(Long userId, OrderDto orderDto) {
    List<ErrorMessageDto> errors = idValidator.idValidate(userId);
    errors.addAll(orderDtoValidator.orderDtoValidate(orderDto));
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    UserDto user = userService.findById(userId);
    Order order = mapper.mapFromDto(orderDto);
    order.setCreateDate(LocalDateTime.now());
    order.setUser(userMapper.mapFromDto(user));
    order.setCertificates(
        order.getCertificates().stream()
            .map(
                certificate ->
                    certificateMapper.mapFromDto(certificateService.findById(certificate.getId())))
            .collect(Collectors.toList()));
    order.setCost(
        order.getCertificates().stream().map(Certificate::getPrice).reduce(0D, Double::sum));
    order = orderDao.save(order);
    if (order == null) {
      throw new CreationException(
          new ErrorMessageDto(
              CustomHttpStatus.Conflict.ORDER.getCode(),
              LanguageManager.getMessage(LocaleMessages.ORDER_NOT_CREATE)));
    }
    return mapper.mapToDto(order, false);
  }

  @Override
  @Transactional
  public void removeById(Long id) {
    List<ErrorMessageDto> errors = idValidator.idValidate(id);
    if (!errors.isEmpty()) {
      throw new ValidationException(errors);
    }
    Order order = orderDao.findById(id);
    if (order == null) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.ORDER.getCode(),
              LanguageManager.getMessage(LocaleMessages.ORDER_NOT_FOUND).formatted(id)));
    }
    try {
      orderDao.removeById(order.getId());
    } catch (NoSuchResultException exception) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.ORDER.getCode(),
              LanguageManager.getMessage(LocaleMessages.ORDER_NOT_FOUND).formatted(id)));
    }
  }
}
