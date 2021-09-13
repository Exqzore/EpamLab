package com.epam.esm.dao.impl.util;

import com.epam.esm.dao.impl.sorting.SortType;
import com.epam.esm.dao.impl.sorting.SortingParameter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortingCalculator<T extends Enum<T>> {
  private static final String SORT_PARAMS_PAIR_DELIMITER = ":";

  public List<SortingParameter<T>> calculateSortParams(
      Class<T> enumClass, List<String> sortParams) {
    List<SortingParameter<T>> sorting = new ArrayList<>();
    if (sortParams != null) {
      sortParams.forEach(
          params -> {
            String[] pair = params.split(SORT_PARAMS_PAIR_DELIMITER);
            if (pair.length != 2) {
              return;
            }
            String sortBy = pair[0].toUpperCase().replaceAll("\\s", "");
            String sortType = pair[1].toUpperCase().replaceAll("\\s", "");
            if (!Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.toList())
                    .contains(sortBy)
                || !Arrays.stream(SortType.values())
                    .map(Enum::name)
                    .collect(Collectors.toList())
                    .contains(sortType)) {
              return;
            }
            sorting.add(
                new SortingParameter<>(T.valueOf(enumClass, sortBy), SortType.valueOf(sortType)));
          });
    }
    return sorting;
  }
}
