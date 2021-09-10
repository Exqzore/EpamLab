package com.epam.esm.dao.impl.sorting;

public enum UserSortBy {
    NAME, SURNAME;

    @Override
    public String toString() {
        return switch (this) {
            case NAME -> "name";
            case SURNAME -> "surname";
        };
    }
}
