package com.epam.esm.web.hateoas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

public class LinkBuilder<T> extends RepresentationModel<LinkBuilder<T>> {
  private final T content;

  @JsonCreator
  public LinkBuilder(@JsonProperty("content") T content) {
    this.content = content;
  }

  public T getContent() {
    return content;
  }
}
