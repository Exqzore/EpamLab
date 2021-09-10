package com.epam.esm.dao.impl.util;

import java.util.List;

public class CertificateSearchCriteria {
  private final String partOfNameOrDescription;
  private final List<String> tagNames;

  public CertificateSearchCriteria(String partOfNameOrDescription, List<String> tagNames) {
    this.partOfNameOrDescription = partOfNameOrDescription;
    this.tagNames = tagNames;
  }

  public String getPartOfNameOrDescription() {
    return partOfNameOrDescription;
  }

  public List<String> getTagNames() {
    return tagNames;
  }
}
