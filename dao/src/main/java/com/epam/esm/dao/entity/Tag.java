package com.epam.esm.dao.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
  private final Set<Certificate> certificates = new HashSet<>();

  public Tag() {}

  public Tag(Long id, String name, boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.isDeleted = isDeleted;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Tag tag = (Tag) o;

    return id != null ? id.equals(tag.id) : tag.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Tag{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
