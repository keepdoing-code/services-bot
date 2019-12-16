package ru.keepdoing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.keepdoing.service.DialogProcessor;
import ru.keepdoing.service.MessageSender;

@Component
@PropertySource("classpath:key.properties")
public class BotController {
    private static Logger LOGGER = LoggerFactory.getLogger(BotController.class);

    public BotController(ApplicationContext ctx, DialogProcessor dialogProcessor) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        LOGGER.info("Bot started.");
        try {
            botsApi.registerBot(new BotProcessor(ctx.getEnvironment(), dialogProcessor));
        } catch (TelegramApiException e) {
            LOGGER.info("Telegram exception - {}", e.getMessage());
        }
    }


    static class BotProcessor extends TelegramLongPollingBot {
        private String BOT_NAME;
        private String BOT_KEY;
        private DialogProcessor processor;

        public BotProcessor(Environment environment, DialogProcessor dialogProcessor) {
            processor = dialogProcessor;
            BOT_KEY = environment.getProperty("bot.key");
            BOT_NAME = environment.getProperty("bot.name");
            LOGGER.info("{} {}", BOT_KEY, BOT_NAME);
        }

        @Override
        public void onUpdateReceived(Update update) {
            MessageSender.setSender(this);
            LOGGER.info("Update received.");
            if (update.hasCallbackQuery()) {
                processor.processCallbackUpdate(update);
            } else if (update.hasMessage() && update.getMessage().hasText()) {
                processor.processUpdate(update);
            }
        }

        @Override
        public String getBotUsername() {
            return BOT_NAME;
        }

        @Override
        public String getBotToken() {
            return BOT_KEY;
        }
    }


}
