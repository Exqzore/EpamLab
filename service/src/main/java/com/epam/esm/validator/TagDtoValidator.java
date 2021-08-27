package com.epam.esm.validator;

import com.epam.esm.dto.TagDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TagDtoValidator {
  private static final String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]{2,30}";

  private static final String NAME_IS_EMPTY = "Name should not be empty";
  private static final String NAME_IS_INVALID = "Name should be between 2 and 30 characters";
  private static final String ID_IS_INVALID = "Id cannot be less than 0";
  private static final String TAG_IS_INVALID = "Tag params cannot be empty";
  private static final String TAG_ALL_PARAMS =
          "When describing a tag, you can specify only one parameter";
  private static final String TAG_ID_IS_INVALID = "Tag id cannot be less than 0";
  private static final String TAG_NAME_IS_INVALID =
          "Tag name should be between 2 and 30 characters";

  public static List<String> tagDtoValidate(TagDto tagDto) {
    Long id = tagDto.getId();
    List<String> errors = new ArrayList<>(Collections.emptyList());
    if (id != null && id < 1) {
      errors.add(ID_IS_INVALID);
    }
    String name = tagDto.getName();
    if (name == null) {
      errors.add(NAME_IS_EMPTY);
    }
    if (name != null && !name.matches(NAME_REGEX)) {
      errors.add(NAME_IS_INVALID);
    }
    return errors;
  }

  public static List<String> tagsValidate(List<TagDto> tags) {
    List<String> errors = new ArrayList<>(Collections.emptyList());
    if (tags != null) {
      tags.forEach(
              tagDto -> {
                Long tagId = tagDto.getId();
                String tagName = tagDto.getName();
                if (tagId == null && tagName == null) {
                  errors.add(TAG_IS_INVALID);
                }
                if (tagId != null && tagName != null) {
                  errors.add(TAG_ALL_PARAMS);
                }
                if (tagId != null && tagId < 1) {
                  errors.add(TAG_ID_IS_INVALID);
                }
                if (tagName != null && !tagName.matches(NAME_REGEX)) {
                  errors.add(TAG_NAME_IS_INVALID);
                }
              });
    }
    return errors;
  }
}
