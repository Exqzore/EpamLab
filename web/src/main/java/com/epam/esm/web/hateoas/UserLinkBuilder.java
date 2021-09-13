package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class UserLinkBuilder {
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PAGE_SIZE = 10;

  private static final String DELETE_THIS = "deleteThis";
  private static final String GET_OWNER = "getOwner";
  private static final String GET_ALL = "getAll";
  private static final String GET_ORDERS = "getOrders";
  private static final String PREVIOUS_PAGE = "previousPage";
  private static final String NEXT_PAGE = "nextPage";
  private static final String FIRST_PAGE = "firstPage";
  private static final String LAST_PAGE = "lastPage";
  private static final String DEFAULT_USER_SORT_PARAMS = "name:asc";
  private static final String DEFAULT_ORDER_SORT_PARAMS = "date:asc";

  public static LinkBuilder<UserDto> buildForOne(UserDto userDto) {
    LinkBuilder<UserDto> userDtoLinkBuilder = new LinkBuilder<>(userDto);
    userDtoLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                    .getAll(List.of(DEFAULT_USER_SORT_PARAMS), DEFAULT_PAGE, DEFAULT_PAGE_SIZE))
            .withRel(GET_ALL),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                    .getOrders(
                        List.of(DEFAULT_ORDER_SORT_PARAMS),
                        DEFAULT_PAGE,
                        DEFAULT_PAGE_SIZE,
                        userDto.getId()))
            .withRel(GET_ORDERS));
    return userDtoLinkBuilder;
  }

  public static LinkBuilder<List<LinkBuilder<UserDto>>> buildForAll(
      List<UserDto> userDtoList, List<String> sortParams, int page, int size, int countPages) {
    sortParams = sortParams == null ? List.of(DEFAULT_USER_SORT_PARAMS) : sortParams;
    List<LinkBuilder<UserDto>> linkBuilderList =
        userDtoList.stream()
            .map(
                userDto -> {
                  LinkBuilder<UserDto> userDtoLinkBuilder = new LinkBuilder<>(userDto);
                  userDtoLinkBuilder.add(
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(UserController.class)
                                  .getOrders(
                                      List.of(DEFAULT_ORDER_SORT_PARAMS),
                                      DEFAULT_PAGE,
                                      DEFAULT_PAGE_SIZE,
                                      userDto.getId()))
                          .withRel(GET_ORDERS));
                  return userDtoLinkBuilder;
                })
            .collect(Collectors.toList());
    LinkBuilder<List<LinkBuilder<UserDto>>> listLinkBuilder = new LinkBuilder<>(linkBuilderList);
    listLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class).getAll(sortParams, 1, size))
            .withRel(FIRST_PAGE),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                    .getAll(sortParams, countPages, size))
            .withRel(LAST_PAGE));
    if (page > 2) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(UserController.class)
                      .getAll(sortParams, page - 1, size))
              .withRel(PREVIOUS_PAGE));
    }
    if (page < countPages - 1) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(UserController.class)
                      .getAll(sortParams, page + 1, size))
              .withRel(NEXT_PAGE));
    }
    return listLinkBuilder;
  }

  public static LinkBuilder<List<LinkBuilder<OrderDto>>> buildForOrders(
      List<OrderDto> orderDtoList,
      Long id,
      int page,
      int size,
      List<String> sortParams,
      int countPages) {
    List<LinkBuilder<OrderDto>> linkBuilderList =
        orderDtoList.stream()
            .map(
                orderDto -> {
                  LinkBuilder<OrderDto> orderDtoLinkBuilder = new LinkBuilder<>(orderDto);
                  orderDtoLinkBuilder.add(
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(OrderController.class)
                                  .delete(orderDto.getId()))
                          .withRel(DELETE_THIS));
                  return orderDtoLinkBuilder;
                })
            .collect(Collectors.toList());
    LinkBuilder<List<LinkBuilder<OrderDto>>> listLinkBuilder = new LinkBuilder<>(linkBuilderList);
    listLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getOne(id))
            .withRel(GET_OWNER),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class).getOrders(sortParams, 1, size, id))
            .withRel(FIRST_PAGE),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                    .getOrders(sortParams, countPages, size, id))
            .withRel(LAST_PAGE));
    if (page > 2) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(UserController.class)
                      .getOrders(sortParams, page - 1, size, id))
              .withRel(PREVIOUS_PAGE));
    }
    if (page < countPages - 1) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(UserController.class)
                      .getOrders(sortParams, page + 1, size, id))
              .withRel(NEXT_PAGE));
    }
    return listLinkBuilder;
  }
}
