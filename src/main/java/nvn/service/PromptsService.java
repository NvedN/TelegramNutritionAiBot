package nvn.service;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptsService {

  private final MessageSource messageSource;

  public String get(String key, Locale locale, Object... args) {
    return messageSource.getMessage(key, args, locale);
  }
}
