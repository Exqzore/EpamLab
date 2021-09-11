package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import com.epam.esm.web.hateoas.CertificateLinkBuilder;
import com.epam.esm.web.hateoas.LinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
  private static final String DEFAULT_PAGE = "1";
  private static final String DEFAULT_PAGE_SIZE = "10";

  private final CertificateService certificateService;

  @Autowired
  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping
  public HttpEntity<LinkBuilder<List<LinkBuilder<CertificateDto>>>> getAll(
      @RequestParam(value = "sort", required = false) List<String> sortParams,
      @RequestParam(value = "part", required = false) String partOfNameOrDescription,
      @RequestParam(value = "tagName", required = false) List<String> tagNames,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
    int countPages = certificateService.getCountOfPages(size);
    if (page > countPages) {
      throw new NotFoundException(
          new ErrorMessageDto(
              CustomHttpStatus.NotFound.UNDEFINED.getCode(),
              LanguageManager.getMessage(LocaleMessages.PAGE_NOT_FOUND)));
    }
    return new ResponseEntity<>(
        CertificateLinkBuilder.buildForAll(
            certificateService.findByCriteria(
                partOfNameOrDescription, tagNames, sortParams, page, size),
            sortParams,
            partOfNameOrDescription,
            tagNames,
            page,
            size,
            countPages),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public HttpEntity<LinkBuilder<CertificateDto>> getOne(@PathVariable Long id) {
    return new ResponseEntity<>(
        CertificateLinkBuilder.buildForOne(certificateService.findById(id)), HttpStatus.OK);
  }

  @PostMapping
  public HttpEntity<LinkBuilder<CertificateDto>> create(
      @RequestBody CertificateDto certificateDto) {
    return new ResponseEntity<>(
        CertificateLinkBuilder.buildForOne(certificateService.create(certificateDto)),
        HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public HttpEntity<LinkBuilder<CertificateDto>> update(
      @PathVariable Long id, @RequestBody CertificateDto certificateDto) {
    return new ResponseEntity<>(
        CertificateLinkBuilder.buildForOne(certificateService.update(id, certificateDto)),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public CertificateDto delete(@PathVariable Long id) {
    certificateService.removeById(id);
    return null;
  }
}
