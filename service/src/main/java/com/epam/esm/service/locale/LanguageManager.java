package com.epam.esm.service.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LanguageManager {
  private static MessageSource messageSource;

  @Autowired
  public LanguageManager(MessageSource messageSource) {
    LanguageManager.messageSource = messageSource;
  }

  public static String getMessage(LocaleMessages localeMessages) {
    return messageSource.getMessage(
            localeMessages.getPropertyPath(),
            null,
            LocaleContextHolder.getLocale());
  }
}
