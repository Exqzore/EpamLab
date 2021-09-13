package com.epam.esm.dao.impl.sorting;

public enum CertificateSortBy {
    NAME, PRICE, DURATION, DATE;

    @Override
    public String toString() {
        return switch (this) {
            case NAME -> "name";
            case DATE -> "createDate";
            case DURATION -> "duration";
            case PRICE -> "price";
        };
    }
}
