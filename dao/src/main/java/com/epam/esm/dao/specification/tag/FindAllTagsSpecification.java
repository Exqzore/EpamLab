package com.epam.esm.dao.specification.tag;

import com.epam.esm.dao.specification.Specification;
import org.springframework.stereotype.Component;

@Component
public class FindAllTagsSpecification implements Specification {
  private final String FIND_ALL_TAGS = "SELECT id, name FROM tags WHERE is_deleted=false";

  @Override
  public String getSql() {
    return FIND_ALL_TAGS;
  }

  @Override
  public Object[] getArgument() {
    return new Object[] {};
  }
}
