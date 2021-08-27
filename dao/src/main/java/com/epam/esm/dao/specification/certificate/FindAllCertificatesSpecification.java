package com.epam.esm.dao.specification.certificate;

import com.epam.esm.dao.specification.Specification;
import org.springframework.stereotype.Component;

public class FindAllCertificatesSpecification implements Specification {
    private final String FIND_ALL_CERTIFICATES = """
            SELECT id, name, description, price, duration, create_date, last_update_date FROM certificates
            """;

    @Override
    public String getSql() {
        return FIND_ALL_CERTIFICATES;
    }

    @Override
    public Object[] getArgument() {
        return new Object[]{};
    }
}
