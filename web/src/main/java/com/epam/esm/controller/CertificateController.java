package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificate")
public class CertificateController {
  private final CertificateService certificateService;

  @Autowired
  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping
  public List<CertificateDto> getAll(
      @RequestParam(value = "sort", required = false) List<String> sortParams,
      @RequestParam(value = "part", required = false) String partOfNameOrDescription,
      @RequestParam(value = "tagName", required = false) String tagName) {
    return certificateService.findByCriteria(partOfNameOrDescription, tagName, sortParams);
  }

  @GetMapping("/{id}")
  public CertificateDto getOne(@PathVariable String id) {
    return certificateService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CertificateDto create(@RequestBody CertificateDto certificateDto) {
    return certificateService.create(certificateDto);
  }

  @PatchMapping("/{id}")
  public CertificateDto update(
      @PathVariable String id, @RequestBody CertificateDto certificateDto) {
    return certificateService.update(id, certificateDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    certificateService.removeById(id);
  }
}
