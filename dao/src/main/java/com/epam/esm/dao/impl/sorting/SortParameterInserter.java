package com.epam.esm.dao.impl.sorting;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import java.util.List;

public interface SortParameterInserter {
    default <R, T extends Enum<T>, X, Z> void addSortingParams (
            CriteriaQuery<R> cq,
            CriteriaBuilder cb,
            From<X, Z> root,
            List<SortingParameter<T>> sorting) {
        sorting.forEach(sortingParameter -> {
            switch (sortingParameter.getSortType()){
                case ASC -> cq.orderBy(cb.asc(root.get(sortingParameter.getSortBy().toString())));
                case DESC -> cq.orderBy(cb.desc(root.get(sortingParameter.getSortBy().toString())));
            }
        });
    }
}
