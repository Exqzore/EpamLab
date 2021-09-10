package com.epam.esm.service.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
  private Long id;
  private String name;
  private String surname;
  private List<OrderDto> orders = new ArrayList<>();

  public UserDto() {}

  public UserDto(Long id, String name, String surname, List<OrderDto> orders) {
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

  public List<OrderDto> getOrders() {
    return orders;
  }

  public void setOrders(List<OrderDto> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDto userDto = (UserDto) o;

    if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
    if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
    if (surname != null ? !surname.equals(userDto.surname) : userDto.surname != null) return false;
    return orders != null ? orders.equals(userDto.orders) : userDto.orders == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (surname != null ? surname.hashCode() : 0);
    result = 31 * result + (orders != null ? orders.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UserDto{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", orders=").append(orders);
    sb.append('}');
    return sb.toString();
  }
}
