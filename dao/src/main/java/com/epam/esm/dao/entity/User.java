package com.epam.esm.dao.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private Set<Order> orders = new HashSet<>();

  public User() {}

  public User(Long id, String name, String surname, Set<Order> orders) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.orders = orders;
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

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    return id != null ? id.equals(user.id) : user.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
