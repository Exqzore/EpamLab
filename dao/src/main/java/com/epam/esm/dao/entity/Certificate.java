package com.epam.esm.dao.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "certificates")
public class Certificate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private Double price;

  @Column(name = "duration")
  private Integer duration;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @Column(name = "last_update_date")
  private LocalDateTime lastUpdateDate;

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.MERGE})
  @JoinTable(
      name = "tag_certificate_membership",
      joinColumns = @JoinColumn(name = "certificate_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  public Certificate() {}

  public Certificate(
      Long id,
      String name,
      String description,
      Double price,
      Integer duration,
      LocalDateTime createDate,
      LocalDateTime lastUpdateDate,
      Set<Tag> tags) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.duration = duration;
    this.createDate = createDate;
    this.lastUpdateDate = lastUpdateDate;
    this.tags = tags;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Certificate that = (Certificate) o;

    return id != null ? id.equals(that.id) : that.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Certificate{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
