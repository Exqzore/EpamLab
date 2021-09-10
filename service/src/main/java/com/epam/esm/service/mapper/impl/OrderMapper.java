package com.epam.esm.service.mapper.impl;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.User;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements Mapper<Order, OrderDto> {
  @Override
  public OrderDto mapToDto(Order entity, boolean mapNestedEntities) {
    Mapper<Certificate, CertificateDto> mapper = new CertificateMapper();
    OrderDto orderDto = new OrderDto();
    orderDto.setId(entity.getId());
    orderDto.setCost(entity.getCost());
    orderDto.setUserId(entity.getUser().getId());
    orderDto.setCreateDate(entity.getCreateDate().toString());
    if (mapNestedEntities) {
      List<Certificate> certificates = entity.getCertificates();
      List<CertificateDto> certificateDtoList =
          certificates.stream()
              .map(certificate -> mapper.mapToDto(certificate, true))
              .collect(Collectors.toList());
      orderDto.setCertificates(certificateDtoList);
    } else {
      orderDto.setCertificates(Collections.emptyList());
    }
    return orderDto;
  }

  @Override
  public Order mapFromDto(OrderDto dto) {
    Mapper<Certificate, CertificateDto> mapper = new CertificateMapper();
    Order order = new Order();
    order.setId(dto.getId());
    order.setCost(dto.getCost());
    User user = new User();
    user.setId(dto.getUserId());
    order.setUser(user);
    List<CertificateDto> certificateDtoList = dto.getCertificates();
    List<Certificate> certificates =
        certificateDtoList.stream().map(mapper::mapFromDto).collect(Collectors.toList());
    order.setCertificates(certificates);
    return order;
  }
}
