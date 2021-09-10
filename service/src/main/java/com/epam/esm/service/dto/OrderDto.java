package com.epam.esm.service.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
  private Long id;
  private Double cost;
  private String createDate;
  private Long userId;
  private List<CertificateDto> certificates = new ArrayList<>();

  public OrderDto() {}

  public OrderDto(
      Long id, Double cost, String createDate, Long userId, List<CertificateDto> certificates) {
    this.id = id;
    this.cost = cost;
    this.createDate = createDate;
    this.userId = userId;
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

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<CertificateDto> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateDto> certificates) {
    this.certificates = certificates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrderDto orderDto = (OrderDto) o;

    if (id != null ? !id.equals(orderDto.id) : orderDto.id != null) return false;
    if (cost != null ? !cost.equals(orderDto.cost) : orderDto.cost != null) return false;
    if (createDate != null ? !createDate.equals(orderDto.createDate) : orderDto.createDate != null) return false;
    if (userId != null ? !userId.equals(orderDto.userId) : orderDto.userId != null) return false;
    return certificates != null ? certificates.equals(orderDto.certificates) : orderDto.certificates == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (cost != null ? cost.hashCode() : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("OrderDto{");
    sb.append("id=").append(id);
    sb.append(", cost=").append(cost);
    sb.append(", createDate='").append(createDate).append('\'');
    sb.append(", userId=").append(userId);
    sb.append(", certificates=").append(certificates);
    sb.append('}');
    return sb.toString();
  }
}
