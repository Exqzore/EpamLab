package com.epam.esm.dao.specification.tag;

import com.epam.esm.dao.specification.Specification;

public class FindTagsByCertificateIdSpecification implements Specification {
    private static final String FIND_TAGS_BY_CERTIFICATE_ID = """
            SELECT t.id, t.name FROM tags t
            JOIN tag_certificate_membership tcm ON t.id=tcm.tag_id
            WHERE tcm.certificate_id=? GROUP BY t.id
            """;
    private final long certificateId;

    public FindTagsByCertificateIdSpecification(long certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String getSql() {
        return FIND_TAGS_BY_CERTIFICATE_ID;
    }

    @Override
    public Object[] getArguments() {
        return new Object[]{certificateId};
    }
}
