package com.epam.esm.web.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.web.hateoas.LinkBuilder;
import com.epam.esm.web.hateoas.OrderLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private static final String DEFAULT_PAGE = "1";
  private static final String DEFAULT_PAGE_SIZE = "10";

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public HttpEntity<LinkBuilder<List<LinkBuilder<OrderDto>>>> getAll(
      @RequestParam(value = "sort", required = false) List<String> sortParams,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
    int countPages = orderService.findPaginated(size);
    if (page > countPages) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.UNDEFINED.getCode(),
              LanguageManager.getMessage(LocaleMessages.PAGE_NOT_FOUND)));
    }
    return new ResponseEntity<>(
        OrderLinkBuilder.buildForAll(
            orderService.findAll(page, size, sortParams), sortParams, page, size, countPages),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public HttpEntity<LinkBuilder<OrderDto>> getOne(@PathVariable Long id) {
    return new ResponseEntity<>(
        OrderLinkBuilder.buildForOne(orderService.findById(id)), HttpStatus.OK);
  }

  @PostMapping
  public HttpEntity<LinkBuilder<OrderDto>> create(
      @RequestParam Long userId, @RequestBody OrderDto orderDto) {
    return new ResponseEntity<>(
        OrderLinkBuilder.buildForOne(orderService.create(userId, orderDto)), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public OrderDto delete(@PathVariable Long id) {
    orderService.removeById(id);
    return null;
  }
}
