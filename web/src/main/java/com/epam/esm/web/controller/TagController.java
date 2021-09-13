package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.web.hateoas.LinkBuilder;
import com.epam.esm.web.hateoas.TagLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
  private static final String DEFAULT_PAGE = "1";
  private static final String DEFAULT_PAGE_SIZE = "10";

  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public HttpEntity<LinkBuilder<List<LinkBuilder<TagDto>>>> getAll(
      @RequestParam(value = "sort", required = false) List<String> sortParams,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
    int countPages = tagService.getCountOfPages(size);
    if (page > countPages) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.UNDEFINED.getCode(),
              LanguageManager.getMessage(LocaleMessages.PAGE_NOT_FOUND)));
    }
    return new ResponseEntity<>(
        TagLinkBuilder.buildForAll(
            tagService.findAll(page, size, sortParams), page, size, sortParams, countPages),
        HttpStatus.OK);
  }

  @GetMapping("/wildlyUsed")
  public HttpEntity<LinkBuilder<TagDto>> getWildlyUsed() {
    return new ResponseEntity<>(
        TagLinkBuilder.buildForOne(tagService.findWildlyUsed()), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public HttpEntity<LinkBuilder<TagDto>> getOne(@PathVariable Long id) {
    return new ResponseEntity<>(TagLinkBuilder.buildForOne(tagService.findById(id)), HttpStatus.OK);
  }

  @PostMapping
  public HttpEntity<LinkBuilder<TagDto>> create(@RequestBody TagDto tagDto) {
    return new ResponseEntity<>(
        TagLinkBuilder.buildForOne(tagService.create(tagDto)), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public TagDto delete(@PathVariable Long id) {
    tagService.removeById(id);
    return null;
  }
}
