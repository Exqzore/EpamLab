package com.epam.esm.service.impl.util;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagFormatInterpreter {
  public Tag fromDto(TagDto tagDto) {
    return new Tag(tagDto.getId(), tagDto.getName());
  }

  public TagDto toDto(Tag tag) {
    return new TagDto(tag.getId(), tag.getName());
  }
}
