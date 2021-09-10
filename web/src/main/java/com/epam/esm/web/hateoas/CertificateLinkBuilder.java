package com.epam.esm.web.hateoas;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.web.controller.CertificateController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateLinkBuilder {
  private static final int DEFAULT_PAGE = 1;
  private static final int DEFAULT_PAGE_SIZE = 10;

  private static final String DELETE_THIS = "deleteThis";
  private static final String UPDATE_THIS = "updateThis";
  private static final String GET_ALL = "getAll";
  private static final String PREVIOUS_PAGE = "previousPage";
  private static final String NEXT_PAGE = "nextPage";
  private static final String FIRST_PAGE = "firstPage";
  private static final String LAST_PAGE = "lastPage";
  private static final String DEFAULT_SORT_PARAMS = "name:asc";

  public static LinkBuilder<CertificateDto> buildForOne(CertificateDto certificateDto) {
    LinkBuilder<CertificateDto> certificateDtoLinkBuilder = new LinkBuilder<>(certificateDto);
    certificateDtoLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CertificateController.class)
                    .delete(certificateDto.getId()))
            .withRel(DELETE_THIS),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CertificateController.class)
                    .update(certificateDto.getId(), certificateDto))
            .withRel(UPDATE_THIS),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CertificateController.class)
                    .getAll(
                        List.of(DEFAULT_SORT_PARAMS),
                        "",
                        Collections.emptyList(),
                        DEFAULT_PAGE,
                        DEFAULT_PAGE_SIZE))
            .withRel(GET_ALL));
    return certificateDtoLinkBuilder;
  }

  public static LinkBuilder<List<LinkBuilder<CertificateDto>>> buildForAll(
      List<CertificateDto> certificateDtoList,
      List<String> sortParams,
      String part,
      List<String> tagNames,
      int page,
      int size,
      int countPages) {
    sortParams = sortParams == null ? List.of(DEFAULT_SORT_PARAMS) : sortParams;
    tagNames = tagNames == null ? Collections.emptyList() : tagNames;
    part = part == null ? "" : part;
    List<LinkBuilder<CertificateDto>> linkBuilderList =
        certificateDtoList.stream()
            .map(
                certificateDto -> {
                  LinkBuilder<CertificateDto> certificateDtoLinkBuilder =
                      new LinkBuilder<>(certificateDto);
                  certificateDtoLinkBuilder.add(
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(CertificateController.class)
                                  .delete(certificateDto.getId()))
                          .withRel(DELETE_THIS),
                      WebMvcLinkBuilder.linkTo(
                              WebMvcLinkBuilder.methodOn(CertificateController.class)
                                  .update(certificateDto.getId(), certificateDto))
                          .withRel(UPDATE_THIS));
                  return certificateDtoLinkBuilder;
                })
            .collect(Collectors.toList());
    LinkBuilder<List<LinkBuilder<CertificateDto>>> listLinkBuilder =
        new LinkBuilder<>(linkBuilderList);
    listLinkBuilder.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CertificateController.class)
                    .getAll(sortParams, part, tagNames, 1, size))
            .withRel(FIRST_PAGE),
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(CertificateController.class)
                    .getAll(sortParams, part, tagNames, countPages, size))
            .withRel(LAST_PAGE));
    if (page > 2) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(CertificateController.class)
                      .getAll(sortParams, part, tagNames, page - 1, size))
              .withRel(PREVIOUS_PAGE));
    }
    if (page < countPages - 1) {
      listLinkBuilder.add(
          WebMvcLinkBuilder.linkTo(
                  WebMvcLinkBuilder.methodOn(CertificateController.class)
                      .getAll(sortParams, part, tagNames, page + 1, size))
              .withRel(NEXT_PAGE));
    }
    return listLinkBuilder;
  }
}
