package com.epam.esm.service.mapper.impl;

import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class TagMapper implements Mapper<Tag, TagDto> {
  @Override
  public TagDto mapToDto(Tag entity, boolean mapNestedEntities) {
    TagDto tagDto = new TagDto();
    tagDto.setId(entity.getId());
    tagDto.setName(entity.getName());
    return tagDto;
  }

  @Override
  public Tag mapFromDto(TagDto dto) {
    Tag tag = new Tag();
    tag.setId(dto.getId());
    tag.setName(dto.getName());
    return tag;
  }
}
