package com.epam.esm.dao.specification.certificate;

import com.epam.esm.dao.specification.Specification;
import org.springframework.stereotype.Component;

public class FindCertificatesByPartAndTagNameSpecification implements Specification {
    private final String FIND_CERTIFICATES_BY_PART_OR_TAG_NAME = """
            SELECT c.id, c.name, c.description, c.price, c.duration, c.create_date, c.last_update_date
            FROM certificates c
            JOIN tag_certificate_membership tcm ON c.id=tcm.certificate_id
            JOIN tags t ON t.id=tcm.tag_id
            WHERE t.name=? AND (c.name LIKE ? OR c.description LIKE ?)
            """;
    private final String tagName;
    private final String partOfNameOrDescription;

    public FindCertificatesByPartAndTagNameSpecification(String tagName, String partOfNameOrDescription) {
        this.tagName = tagName;
        this.partOfNameOrDescription = partOfNameOrDescription;
    }

    @Override
    public String getSql() {
        return FIND_CERTIFICATES_BY_PART_OR_TAG_NAME;
    }

    @Override
    public Object[] getArgument() {
        return new Object[]{tagName, partOfNameOrDescription, partOfNameOrDescription};
    }
}
