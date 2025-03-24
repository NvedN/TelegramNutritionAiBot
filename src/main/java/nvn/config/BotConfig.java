package nvn.config;

import lombok.extern.slf4j.Slf4j;
import nvn.bot.DietTelegramBot;
import nvn.service.dispatcher.UpdateDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class BotConfig {

  @Value("${telegram.bot.username}")
  private String botUsername;

  @Value("${telegram.bot.token}")
  private String botToken;

  @Bean
  public DietTelegramBot dietTelegramBot(UpdateDispatcher updateDispatcher) {
    DietTelegramBot bot = new DietTelegramBot(botUsername, botToken, updateDispatcher);
    try {
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
      telegramBotsApi.registerBot(bot);
      log.info("Telegram Bot successfully registered.");
    } catch (Exception e) {
      log.error("Exception during registration of Telegram Bot: {}", e.getMessage());
    }
    return bot;
  }
}
