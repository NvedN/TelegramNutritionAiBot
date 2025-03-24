package nvn.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nvn.bot.DietTelegramBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotService {

  public void sendMessage(Long chatId, String text, DietTelegramBot bot) {
    SendMessage message = SendMessage.builder().chatId(chatId.toString()).text(text).build();
    try {
      bot.execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send message to chatId={}", chatId, e);
    }
  }

  public void sendKeyboard(
      Long chatId, String text, DietTelegramBot bot, ReplyKeyboardMarkup keyboard) {
    SendMessage message =
        SendMessage.builder().chatId(chatId.toString()).text(text).replyMarkup(keyboard).build();
    try {
      bot.execute(message);
    } catch (TelegramApiException e) {
      log.error("Failed to send keyboard message to chatId={}", chatId, e);
    }
  }

  public String downloadPhotoAsBase64(String fileId, DietTelegramBot bot) {
    try {
      var file = bot.execute(new GetFile(fileId));
      String fileUrl = file.getFileUrl(bot.getBotToken());
      try (InputStream inputStream = URI.create(fileUrl).toURL().openStream()) {
        return Base64.getEncoder().encodeToString(inputStream.readAllBytes());
      }
    } catch (TelegramApiException | IOException e) {
      log.error("Error downloading photo fileId={}", fileId, e);
      throw new RuntimeException("Could not download photo", e);
    }
  }
}
