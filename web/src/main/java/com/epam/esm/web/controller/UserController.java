package com.epam.esm.web.controller;

import com.epam.esm.service.UserService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.web.hateoas.LinkBuilder;
import com.epam.esm.web.hateoas.UserLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  private static final String DEFAULT_PAGE = "1";
  private static final String DEFAULT_PAGE_SIZE = "10";
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public HttpEntity<LinkBuilder<List<LinkBuilder<UserDto>>>> getAll(
      @RequestParam(value = "sort", required = false) List<String> sortParams,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
    int countPages = userService.getCountOfPages(size);
    if (page > countPages) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.UNDEFINED.getCode(),
              LanguageManager.getMessage(LocaleMessages.PAGE_NOT_FOUND)));
    }
    return new ResponseEntity<>(
        UserLinkBuilder.buildForAll(
            userService.findAll(page, size, sortParams), sortParams, page, size, countPages),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public HttpEntity<LinkBuilder<UserDto>> getOne(@PathVariable Long id) {
    return new ResponseEntity<>(
        UserLinkBuilder.buildForOne(userService.findById(id)), HttpStatus.OK);
  }

  @GetMapping("/{id}/orders")
  public HttpEntity<LinkBuilder<List<LinkBuilder<OrderDto>>>> getOrders(
      @RequestParam(value = "sort", required = false) List<String> sortParams,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
      @PathVariable Long id) {
    int countPages = userService.getCountOfPagesOfUserOrders(id, size);
    if (page > countPages) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.UNDEFINED.getCode(),
              LanguageManager.getMessage(LocaleMessages.PAGE_NOT_FOUND)));
    }
    return new ResponseEntity<>(
        UserLinkBuilder.buildForOrders(
            userService.findOrdersByUserId(id, page, size, sortParams),
            id,
            page,
            size,
            sortParams,
            countPages),
        HttpStatus.OK);
  }
}
