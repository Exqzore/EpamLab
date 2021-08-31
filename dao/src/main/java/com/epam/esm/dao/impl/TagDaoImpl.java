package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.specification.Specification;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
  private static final BeanPropertyRowMapper<Tag> rowMapper =
      new BeanPropertyRowMapper<>(Tag.class);
  private static final String FIND_BY_ID =
      "SELECT id, name FROM tags WHERE id=? AND is_deleted=false";
  private static final String FIND_BY_NAME =
      "SELECT id, name FROM tags WHERE name=? AND is_deleted=false";
  private static final String SAVE =
      "INSERT INTO tags (name) VALUES(?) ON CONFLICT (name) DO UPDATE SET is_deleted=false RETURNING id, name";
  private static final String REMOVE_BY_ID = "UPDATE tags SET is_deleted=true WHERE id=?";
  private static final String REMOVE_BY_NAME = "UPDATE tags SET is_deleted=true WHERE name=?";
  private static final String SET_IS_DELETED_BY_NAME = "UPDATE tags SET is_deleted=? WHERE name=?";
  private static final String IS_EXISTS_BY_NAME = "SELECT id, name FROM tags WHERE name=?";
  private static final String SET_IS_DELETED_BY_ID = "UPDATE tags SET is_deleted=? WHERE id=?";
  private static final String IS_EXISTS_BY_ID = "SELECT id, name FROM tags WHERE id=?";
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public TagDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Tag> findBySpecification(Specification specification) {
    return jdbcTemplate.query(specification.getSql(), rowMapper, specification.getArguments());
  }

  @Override
  public Optional<Tag> findById(long id) {
    return jdbcTemplate.query(FIND_BY_ID, rowMapper, id).stream().findAny();
  }

  @Override
  public Optional<Tag> findByName(String name) {
    return jdbcTemplate.query(FIND_BY_NAME, rowMapper, name).stream().findAny();
  }

  @Override
  public Optional<Tag> save(Tag tag) {
    return jdbcTemplate.query(SAVE, rowMapper, tag.getName()).stream().findAny();
  }

  @Override
  public void removeById(long id) {
    jdbcTemplate.update(REMOVE_BY_ID, id);
  }

  @Override
  public void removeByName(String name) {
    jdbcTemplate.update(REMOVE_BY_NAME, name);
  }

  @Override
  public void setDeleted(String name, boolean isDeleted) {
    jdbcTemplate.update(SET_IS_DELETED_BY_NAME, isDeleted, name);
  }

  @Override
  public void setDeleted(long id, boolean isDeleted) {
    jdbcTemplate.update(SET_IS_DELETED_BY_ID, isDeleted, id);
  }

  @Override
  public boolean isExists(String name) {
    return jdbcTemplate
        .query(IS_EXISTS_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name)
        .stream()
        .findAny()
        .isPresent();
  }

  @Override
  public boolean isExists(long id) {
    return jdbcTemplate.query(IS_EXISTS_BY_ID, new BeanPropertyRowMapper<>(Tag.class), id).stream()
        .findAny()
        .isPresent();
  }
}
