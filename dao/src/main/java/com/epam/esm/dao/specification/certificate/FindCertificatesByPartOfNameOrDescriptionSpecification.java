package com.epam.esm.dao.specification.certificate;

import com.epam.esm.dao.specification.Specification;
import org.springframework.stereotype.Component;

public class FindCertificatesByPartOfNameOrDescriptionSpecification implements Specification {
    private final String FIND_CERTIFICATES_BY_PART_OF_NAME_OR_DESCRIPTION = """
            SELECT id, name, description, price, duration, create_date, last_update_date FROM certificates
            WHERE name LIKE ? OR description LIKE ? GROUP BY id
            """;
    private final String part;

    public FindCertificatesByPartOfNameOrDescriptionSpecification(String part) {
        this.part = part;
    }

    @Override
    public String getSql() {
        return FIND_CERTIFICATES_BY_PART_OF_NAME_OR_DESCRIPTION;
    }

    @Override
    public Object[] getArgument() {
        return new Object[]{part, part};
    }
}
