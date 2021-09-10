package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.controller.TagController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TagLinkBuilder {
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PAGE_SIZE = 10;

  private static final String DELETE_THIS = "deleteThis";
  private static final String GET_WILDLY_USED = "getWildlyUsed";
  private static final String GET_ALL = "getAll";
  private static final String NEXT_PAGE = "nextPage";
  private static final String PREVIOUS_PAGE = "previousPage";
  private static final String FIRST_PAGE = "firstPage";
  private static final String LAST_PAGE = "lastPage";

  public static LinkBuilder<TagDto> buildForOne(TagDto tagDto) {
    LinkBuilder<TagDto> tagDtoLinkBuilder = new LinkBuilder<>(tagDto);
    tagDtoLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TagController.class).delete(tagDto.getId()))
            .withRel(DELETE_THIS),
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).getWildlyUsed())
            .withRel(GET_WILDLY_USED),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TagController.class)
                    .getAll(Collections.emptyList(), DEFAULT_PAGE, DEFAULT_PAGE_SIZE))
            .withRel(GET_ALL));
    return tagDtoLinkBuilder;
  }

  public static LinkBuilder<List<LinkBuilder<TagDto>>> buildForAll(
      List<TagDto> tagDtoList, int page, int size, List<String> sortParams, int countPages) {
    sortParams = sortParams == null ? Collections.emptyList() : sortParams;
    List<LinkBuilder<TagDto>> linkBuilderList =
        tagDtoList.stream()
            .map(
                tagDto -> {
                  LinkBuilder<TagDto> tagDtoLinkBuilder = new LinkBuilder<>(tagDto);
                  tagDtoLinkBuilder.add(
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(TagController.class)
                                  .delete(tagDto.getId()))
                          .withRel(DELETE_THIS));
                  return tagDtoLinkBuilder;
                })
            .collect(Collectors.toList());
    LinkBuilder<List<LinkBuilder<TagDto>>> listLinkBuilder = new LinkBuilder<>(linkBuilderList);
    listLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).getWildlyUsed())
            .withRel(GET_WILDLY_USED),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TagController.class).getAll(sortParams, 1, size))
            .withRel(FIRST_PAGE),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TagController.class)
                    .getAll(sortParams, countPages, size))
            .withRel(LAST_PAGE));
    if (page > 2) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(TagController.class)
                      .getAll(sortParams, page - 1, size))
              .withRel(PREVIOUS_PAGE));
    }
    if (page < countPages - 1) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(TagController.class)
                      .getAll(sortParams, page + 1, size))
              .withRel(NEXT_PAGE));
    }
    return listLinkBuilder;
  }
}
