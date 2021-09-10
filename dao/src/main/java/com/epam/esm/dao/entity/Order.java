package com.epam.esm.dao.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "cost")
  private Double cost;

  @Column(name = "create_date")
  private LocalDateTime createDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "order_certificate_membership",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "certificate_id"))
  private List<Certificate> certificates = new ArrayList<>();

  public Order() {}

  public Order(
      Long id, Double cost, LocalDateTime createDate, User user, List<Certificate> certificates) {
    this.id = id;
    this.cost = cost;
    this.createDate = createDate;
    this.user = user;
    this.certificates = certificates;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Certificate> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<Certificate> certificates) {
    this.certificates = certificates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Order order = (Order) o;

    return id != null ? id.equals(order.id) : order.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Order{");
    sb.append("id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
