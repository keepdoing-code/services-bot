package ru.keepdoing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.keepdoing.service.DialogProcessor;
import ru.keepdoing.service.MessageSender;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Component
@PropertySource("key.properties")
@PropertySource("proxy.properties")
public class BotController {

    private static Logger LOGGER = LoggerFactory.getLogger(BotController.class);
    private String PROXY_HOST;
    private int PROXY_PORT;
    private String PROXY_USER;
    private String PROXY_PASSWORD;

    private void loadProxyParams(Environment env) {
        PROXY_HOST = env.getProperty("PROXY_HOST");
        PROXY_PORT = Integer.parseInt(env.getProperty("PROXY_PORT"));
        PROXY_USER = env.getProperty("PROXY_USER");
        PROXY_PASSWORD = env.getProperty("PROXY_PASSWORD");
        LOGGER.info("Proxy params loaded: {}:{}@{}:{}", PROXY_USER, PROXY_PASSWORD, PROXY_HOST, PROXY_PORT);
    }

    public BotController(DialogProcessor dialogProcessor, Environment env) {
        loadProxyParams(env);
        try {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
                }
            });
            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            LOGGER.info("Bot started.");
            botsApi.registerBot(new BotProcessor(env, dialogProcessor, botOptions));
        } catch (TelegramApiException e) {
            LOGGER.info("Telegram exception - {}", e.getMessage());
        }
    }

    static class BotProcessor extends TelegramLongPollingBot {
        private String BOT_NAME;
        private String BOT_KEY;
        private DialogProcessor processor;

        public BotProcessor(Environment environment, DialogProcessor dialogProcessor, DefaultBotOptions botOptions) {
            super(botOptions);
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
