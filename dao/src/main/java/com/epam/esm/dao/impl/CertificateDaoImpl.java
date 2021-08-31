package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.specification.Specification;
import com.epam.esm.entity.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    private static final BeanPropertyRowMapper<Certificate> rowMapper = new BeanPropertyRowMapper<>(Certificate.class);
    private static final String FIND_BY_ID =
            "SELECT id, name, description, price, duration, create_date, last_update_date FROM certificates WHERE id=?";
    private static final String SAVE = """
            INSERT INTO certificates (name, description, price, duration)
            VALUES(?,?,?,?) RETURNING id, name, description, price, duration, create_date, last_update_date
            """;
    private static final String UPDATE = """
            UPDATE certificates
            SET
            name=COALESCE(?, name),
            description=COALESCE(?, description),
            price=COALESCE(?, price),
            duration=COALESCE(?, duration)
            WHERE id=?
            RETURNING id, name, description, price, duration, create_date, last_update_date
            """;
    private static final String REMOVE_BY_ID = "DELETE FROM certificates WHERE id=?";
    private static final String REMOVE_ALL_TAGS_BY_CERTIFICATE_ID =
            "DELETE FROM tag_certificate_membership WHERE certificate_id=?";
    private static final String ADD_TAG_TO_CERTIFICATE =
            "INSERT INTO tag_certificate_membership (tag_id, certificate_id) VALUES(?,?)";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Certificate> findBySpecification(Specification specification, String sortParams) {
        return jdbcTemplate.query(
                specification.getSql() + sortParams,
                rowMapper,
                specification.getArguments());
    }

    @Override
    public Optional<Certificate> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID, rowMapper, id).stream().findAny();
    }

    @Override
    public Optional<Certificate> save(Certificate certificate) {
        return jdbcTemplate.query(
                SAVE,
                rowMapper,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration()
        ).stream().findAny();
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        return jdbcTemplate.query(
                UPDATE,
                rowMapper,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getId()
        ).stream().findAny();
    }

    @Override
    public void removeById(long id) {
        jdbcTemplate.update(REMOVE_BY_ID, id);
    }

    @Override
    public void removeAllTagsByCertificateId(long id) {
        jdbcTemplate.update(REMOVE_ALL_TAGS_BY_CERTIFICATE_ID, id);
    }

    @Override
    public void addTagToCertificate(long certificateId, long tagId) {
        jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE, tagId, certificateId);
    }
}
