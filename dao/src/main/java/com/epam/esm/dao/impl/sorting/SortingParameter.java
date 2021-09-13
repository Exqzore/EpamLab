package com.epam.esm.dao.impl.sorting;

public class SortingParameter<T extends Enum<T>> {
  private final T sortBy;
  private final SortType sortType;

  public SortingParameter(T sortBy, SortType sortType) {
    this.sortBy = sortBy;
    this.sortType = sortType;
  }

  public T getSortBy() {
    return sortBy;
  }

  public SortType getSortType() {
    return sortType;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SortingParameter{");
    sb.append("sortBy=").append(sortBy);
    sb.append(", sortType=").append(sortType);
    sb.append('}');
    return sb.toString();
  }
}
