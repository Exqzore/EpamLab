package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public List<TagDto> getAll() {
    return tagService.findAll();
  }

  @GetMapping("/{id}")
  public TagDto getOne(@PathVariable String id) {
    return tagService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TagDto create(@RequestBody TagDto tagDto) {
    return tagService.create(tagDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    tagService.removeById(id);
  }
}
