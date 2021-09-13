package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLinkBuilder {
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PAGE_SIZE = 10;

  private static final String DELETE_THIS = "deleteThis";
  private static final String GET_OWNER = "getOwner";
  private static final String GET_ALL = "getAll";
  private static final String PREVIOUS_PAGE = "previousPage";
  private static final String NEXT_PAGE = "nextPage";
  private static final String FIRST_PAGE = "firstPage";
  private static final String LAST_PAGE = "lastPage";
  private static final String DEFAULT_SORT_PARAMS = "date:asc";

  public static LinkBuilder<OrderDto> buildForOne(OrderDto orderDto) {
    LinkBuilder<OrderDto> orderDtoLinkBuilder = new LinkBuilder<>(orderDto);
    orderDtoLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).delete(orderDto.getId()))
            .withRel(DELETE_THIS),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class).getOne(orderDto.getUserId()))
            .withRel(GET_OWNER),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class)
                    .getAll(List.of(DEFAULT_SORT_PARAMS), DEFAULT_PAGE, DEFAULT_PAGE_SIZE))
            .withRel(GET_ALL));
    return orderDtoLinkBuilder;
  }

  public static LinkBuilder<List<LinkBuilder<OrderDto>>> buildForAll(
      List<OrderDto> orderDtoList, List<String> sortParams, int page, int size, int countPages) {
    sortParams = sortParams == null ? List.of(DEFAULT_SORT_PARAMS) : sortParams;
    List<LinkBuilder<OrderDto>> linkBuilderList =
        orderDtoList.stream()
            .map(
                orderDto -> {
                  LinkBuilder<OrderDto> orderDtoLinkBuilder = new LinkBuilder<>(orderDto);
                  orderDtoLinkBuilder.add(
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(OrderController.class)
                                  .delete(orderDto.getId()))
                          .withRel(DELETE_THIS),
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(UserController.class)
                                  .getOne(orderDto.getUserId()))
                          .withRel(GET_OWNER));
                  return orderDtoLinkBuilder;
                })
            .collect(Collectors.toList());
    LinkBuilder<List<LinkBuilder<OrderDto>>> listLinkBuilder = new LinkBuilder<>(linkBuilderList);
    listLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).getAll(sortParams, 1, size))
            .withRel(FIRST_PAGE),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class)
                    .getAll(sortParams, countPages, size))
            .withRel(LAST_PAGE));
    if (page > 2) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(OrderController.class)
                      .getAll(sortParams, page - 1, size))
              .withRel(PREVIOUS_PAGE));
    }
    if (page < countPages - 1) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(OrderController.class)
                      .getAll(sortParams, page + 1, size))
              .withRel(NEXT_PAGE));
    }
    return listLinkBuilder;
  }
}
