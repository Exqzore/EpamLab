package com.epam.esm.dao.specification;

/** Specification of sql request. */
public interface Specification {
  /**
   * Get sql.
   *
   * @return Sql request
   */
  String getSql();

  /**
   * Get arguments of request.
   *
   * @return arguments
   */
  Object[] getArguments();
}
