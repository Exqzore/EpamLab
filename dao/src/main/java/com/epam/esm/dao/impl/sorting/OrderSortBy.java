package com.epam.esm.dao.impl.sorting;

public enum OrderSortBy {
    COST, DATE;

    @Override
    public String toString() {
        return switch (this) {
            case COST -> "cost";
            case DATE -> "createDate";
        };
    }
}
